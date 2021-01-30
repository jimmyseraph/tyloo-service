package vip.testops.engine.entities.vto;

import lombok.Data;

import java.util.Date;

@Data
public class ExecResultVTO {
    private Long projectId;
    private Long caseId;
    private String caseName;
    private String requestHeaders;
    private String url;
    private String method;
    private String requestBody;
    private String responseHeaders;
    private String responseBody;
    private String status;
    private String message;
    private Long duration;
    private Date startTime;
}
