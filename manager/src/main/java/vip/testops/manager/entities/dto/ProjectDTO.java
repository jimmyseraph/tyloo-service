package vip.testops.manager.entities.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ProjectDTO {
    private Long projectId;
    private String projectName;
    private String description;
    private int status;
    private Date createTime;
    private Date updateTime;
}
