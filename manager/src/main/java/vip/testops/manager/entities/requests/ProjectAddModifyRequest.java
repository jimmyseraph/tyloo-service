package vip.testops.manager.entities.requests;

import lombok.Data;

@Data
public class ProjectAddModifyRequest {
    private Long projectId;
    private String projectName;
    private String description;
}
