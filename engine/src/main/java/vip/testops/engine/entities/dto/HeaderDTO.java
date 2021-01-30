package vip.testops.engine.entities.dto;

import lombok.Data;

@Data
public class HeaderDTO {
    private Long headerId;
    private String name;
    private String value;
    private Long caseId;
}
