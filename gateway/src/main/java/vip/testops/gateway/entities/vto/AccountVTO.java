package vip.testops.gateway.entities.vto;

import lombok.Data;

@Data
public class AccountVTO {
    private Long accountId;
    private String AccountName;
    private String email;
}
