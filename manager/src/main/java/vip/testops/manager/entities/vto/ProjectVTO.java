package vip.testops.manager.entities.vto;

import lombok.Data;

import java.util.List;

@Data
public class ProjectVTO {
    private Long projectId;
    private String projectName;
    private String description;
    private String status;
    private List<SuiteVTO> caseList;
}
