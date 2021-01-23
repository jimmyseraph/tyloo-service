package vip.testops.manager.entities.vto;

import lombok.Data;

@Data
public class SuiteVTO {
    private Long caseId;
    private String caseName;
    private String description;
    private String status;
}
