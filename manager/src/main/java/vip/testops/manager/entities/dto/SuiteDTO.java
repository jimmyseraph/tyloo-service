package vip.testops.manager.entities.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SuiteDTO {
    private Long suiteId;
    private Long projectId;
    private Long caseId;
    private int status;
    private Long duration;
    private Date lastRun;
}
