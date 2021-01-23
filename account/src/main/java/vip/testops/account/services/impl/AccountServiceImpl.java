package vip.testops.account.services.impl;

import com.auth0.jwt.interfaces.Claim;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import vip.testops.account.common.Response;
import vip.testops.account.entities.dto.AccountDTO;
import vip.testops.account.entities.vto.AccountVTO;
import vip.testops.account.entities.vto.LoginVTO;
import vip.testops.account.mappers.AccountMapper;
import vip.testops.account.services.AccountService;
import vip.testops.account.utils.DigestUtil;
import vip.testops.account.utils.JWTUtil;
import vip.testops.account.utils.StringUtil;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Value("${jwt.expire}")
    private int tokenExpire;

    @Value("${jwt.key}")
    private String jwtKey;

    private ObjectMapper objectMapper = new ObjectMapper();

    private AccountMapper accountMapper;

    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    public void setAccountMapper(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void doLogin(String email, String password, Response<LoginVTO> response) {
        // 检查email和password是否正确
        AccountDTO accountDTO = accountMapper.getUserByEmail(email);
        if(accountDTO == null){
            response.serviceError("email not valid");
            return;
        }
        try {
            password = DigestUtil.digest(password + accountDTO.getSalt(), "SHA-256");
        } catch (NoSuchAlgorithmException e) {
            response.serviceError("digest error");
            log.error("System error while checking password", e);
            return;
        }
        if(!password.equals(accountDTO.getPassword())){
            response.serviceError("password not valid");
            return;
        }

        // 更新最新登录时间
        int col = accountMapper.updateLastLoginTime(accountDTO.getAccountId());
        if(col == 0){
            response.serviceError("update login time error");
            return;
        }

        // 生成token
        Map<String, Object> claim = new HashMap<>();
        claim.put("id", accountDTO.getAccountId());
        claim.put("username", accountDTO.getAccountName());
        claim.put("email", accountDTO.getEmail());
        String secretKey = JWTUtil.getSecret(jwtKey);
        String token = JWTUtil.createToken(claim, secretKey, 30);

        // 保存token到缓存redis中
        stringRedisTemplate.opsForValue().set(token, secretKey, tokenExpire, TimeUnit.MINUTES);
        LoginVTO loginVTO = new LoginVTO();
        loginVTO.setToken(token);
        loginVTO.setUsername(accountDTO.getAccountName());
        response.dataSuccess(loginVTO);

    }

    @Override
    public void doAuthorize(String token, Response<AccountVTO> response) {
        // 检查token中是否存在token
        String secretKey = stringRedisTemplate.opsForValue().get(token);
        if(StringUtil.isEmptyOrNull(secretKey)) {
            response.serviceError("token is invalid");
            return;
        }
        log.info("get secret key from redis: {}", secretKey);
        Claim claim = JWTUtil.verifyToken(token, secretKey);
        AccountVTO accountVTO = new AccountVTO();
        accountVTO.setAccountId((Long)claim.asMap().get("id"));
        accountVTO.setAccountName(claim.asMap().get("username").toString());
        accountVTO.setEmail(claim.asMap().get("email").toString());
        // 刷新redis中token过期时间
        stringRedisTemplate.expire(token, tokenExpire, TimeUnit.MINUTES);

        response.dataSuccess(accountVTO);
    }
}
