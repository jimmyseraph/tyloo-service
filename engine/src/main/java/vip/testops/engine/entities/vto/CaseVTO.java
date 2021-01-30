package vip.testops.engine.entities.vto;

import lombok.Data;
import vip.testops.engine.entities.dto.AssertionDTO;
import vip.testops.engine.entities.dto.HeaderDTO;

import java.util.List;

@Data
public class CaseVTO {
    private Long caseId;
    private String caseName;
    private String description;
    private String url;
    private String method;
    private List<HeaderDTO> headers;
    private String body;
    private List<AssertionDTO> assertions;
}
