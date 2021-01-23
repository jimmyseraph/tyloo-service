package vip.testops.manager.entities.dto;

import lombok.Data;

@Data
public class AssertionDTO {
    private Long assertionId;
    private String actual;
    private String op;
    private String expected;
    private Long caseId;
}
