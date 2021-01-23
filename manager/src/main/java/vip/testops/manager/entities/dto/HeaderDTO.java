package vip.testops.manager.entities.dto;

import lombok.Data;

@Data
public class HeaderDTO {
    private Long headerId;
    private String name;
    private String value;
    private Long caseId;
}
