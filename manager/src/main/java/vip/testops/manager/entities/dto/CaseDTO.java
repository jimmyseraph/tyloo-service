package vip.testops.manager.entities.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CaseDTO {
    private Long caseId;
    private String caseName;
    private String description;
    private String url;
    private String method;
    private String body;
    private Date createTime;
    private Date updateTime;
}
