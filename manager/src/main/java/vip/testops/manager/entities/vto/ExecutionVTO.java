package vip.testops.manager.entities.vto;

import lombok.Data;

import java.util.List;

@Data
public class ExecutionVTO {
    private Long projectId;
    private String projectName;
    private List<CaseVTO> caseList;
}
