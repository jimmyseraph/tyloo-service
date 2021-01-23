package vip.testops.manager.entities.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DetailDTO {
    private Long caseId;
    private String caseName;
    private String description;
    private Integer status;
}
