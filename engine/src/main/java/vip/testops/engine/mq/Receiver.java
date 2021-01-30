package vip.testops.engine.mq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vip.testops.engine.common.Response;
import vip.testops.engine.entities.enums.SuiteStatues;
import vip.testops.engine.entities.vto.ExecResultVTO;
import vip.testops.engine.entities.vto.ExecutionVTO;
import vip.testops.engine.mappers.ExecResultMapper;
import vip.testops.engine.services.ExecutionService;

@Component
@Slf4j
@RabbitListener(
        bindings = @QueueBinding(
                exchange = @Exchange(value = "${mq.config.exchange}", type = ExchangeTypes.DIRECT),
                key = "${mq.config.routing-key}",
                value = @Queue(value = "${mq.config.queue-name}", autoDelete = "false")
        )
)
public class Receiver {

    private ExecutionService executionService;
    private ExecResultMapper execResultMapper;

    @Autowired
    public void setExecutionService(ExecutionService executionService) {
        this.executionService = executionService;
    }

    @Autowired
    public void setExecResultMapper(ExecResultMapper execResultMapper) {
        this.execResultMapper = execResultMapper;
    }

    @RabbitHandler
    public void process(String msg){
        log.info("receive execution plan --> {}", msg);
        // 处理执行计划
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ExecutionVTO executionVTO = objectMapper.readValue(msg, ExecutionVTO.class);
            executionVTO.getCaseList().forEach(caseVTO -> {
                // 执行具体的case
                Response<ExecResultVTO> response = new Response<>();
                executionService.doExecuteCase(executionVTO.getProjectId(), caseVTO, response);
                // 获取案例执行的结果
                ExecResultVTO execResultVTO = response.getData();
                execResultMapper.addExecResult(execResultVTO);
                // 将case的执行结果回写到t_suite表中，通过接口调用方式
                Integer status = SuiteStatues.getByValue(execResultVTO.getStatus()).getKey();

            });
            // 将整个project执行结果回写到t_project表中，通过接口调用方式

        } catch (JsonProcessingException e) {
            log.error("system error while parsing execution plan.", e);
        }
    }
}
