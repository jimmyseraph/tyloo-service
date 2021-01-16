package vip.testops.account.entities.dto;

import lombok.Data;

import java.util.Date;

@Data
public class AccountDTO {
    private Long accountId;
    private String accountName;
    private String email;
    private String salt;
    private String password;
    private Date createTime;
    private Date lastLoginTime;
}
