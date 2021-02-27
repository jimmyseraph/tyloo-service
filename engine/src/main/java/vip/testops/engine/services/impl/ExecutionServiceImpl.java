package vip.testops.engine.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.testops.engine.common.Response;
import vip.testops.engine.entities.enums.SuiteStatues;
import vip.testops.engine.entities.vto.CaseVTO;
import vip.testops.engine.entities.vto.ExecResultVTO;
import vip.testops.engine.http.EasyRequest;
import vip.testops.engine.http.EasyResponse;
import vip.testops.engine.http.impl.OkHttpRequest;
import vip.testops.engine.parsers.AssertionParser;
import vip.testops.engine.parsers.CommandParser;
import vip.testops.engine.services.ExecutionService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ExecutionServiceImpl implements ExecutionService {

    @Override
    public void doExecuteCase(Long projectId, CaseVTO caseVTO, Response<ExecResultVTO> response) {
        ExecResultVTO execResultVTO = new ExecResultVTO();
        // 发送http请求
        EasyRequest easyRequest = new OkHttpRequest();
        easyRequest.setUrl(caseVTO.getUrl())
                .setMethod(caseVTO.getMethod())
                .setBody(EasyRequest.JSON, caseVTO.getBody());
        caseVTO.getHeaders().forEach(header -> easyRequest.addHeader(header.getName(), header.getValue()));
        try {
            long startTime = System.currentTimeMillis();
            EasyResponse easyResponse = easyRequest.execute();
            long endTime = System.currentTimeMillis();
            // 写入结果
            execResultVTO.setProjectId(projectId);
            execResultVTO.setCaseId(caseVTO.getCaseId());
            execResultVTO.setCaseName(caseVTO.getCaseName());
            execResultVTO.setUrl(caseVTO.getUrl());
            execResultVTO.setMethod(caseVTO.getMethod());
            execResultVTO.setRequestBody(caseVTO.getBody());
            execResultVTO.setRequestHeaders(caseVTO.getHeaders().toString());
            execResultVTO.setStartTime(new Date(startTime));
            execResultVTO.setDuration(endTime - startTime);
            execResultVTO.setResponseHeaders(easyResponse.getHeaders().toString());
            execResultVTO.setResponseBody(easyResponse.getBody());
            // 默认设置执行成功，之后通过断言判断是否确实成功，只要有一条断言失败，则状态置为失败
            execResultVTO.setStatus(SuiteStatues.PASS.getValue());
            // 断言处理
            List<String> assertResult = new ArrayList<>();
            // 实例化命令解析器
            CommandParser commandParser = new CommandParser(easyResponse);
            caseVTO.getAssertions().forEach(assertion -> {
                // 使用命令解析器解析断言中可能带有的命令
                String actual = commandParser.parse(assertion.getActual());
                String expected = commandParser.parse(assertion.getExpected());
                // 调用断言解析
                boolean res = AssertionParser.parse(actual, assertion.getOp(), expected);
                // 如果断言失败
                if(!res){
                    execResultVTO.setStatus(SuiteStatues.FAIL.getValue());
                }
                // 记录断言结果
                assertResult.add("actual: " + actual +
                        ", op: " + assertion.getOp() +
                        ", expected: " + expected +
                        " --> " + res);
            });
            execResultVTO.setMessage(assertResult.toString());
        } catch (IOException e) {
            execResultVTO.setStatus(SuiteStatues.FAIL.getValue());
            execResultVTO.setMessage(e.getMessage());
            e.printStackTrace();
        }
        response.dataSuccess(execResultVTO);
    }
}
