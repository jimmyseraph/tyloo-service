package vip.testops.account.services;

import vip.testops.account.common.Response;
import vip.testops.account.entities.vto.AccountVTO;
import vip.testops.account.entities.vto.LoginVTO;

public interface AccountService {
    void doLogin(String email, String password, Response<LoginVTO> response);
    void doAuthorize(String token, Response<AccountVTO> response);
}
