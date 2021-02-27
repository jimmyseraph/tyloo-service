package vip.testops.engine.parsers;

import com.fasterxml.jackson.core.JsonProcessingException;
import vip.testops.engine.http.EasyResponse;
import vip.testops.engine.utils.Extractor;

/**
 * 命令解析器<br />
 * 支持code(), header(headerName), body(json-path exp)这些命令的字符串解析
 */
public class CommandParser {
    private static String BRACE_LEFT = "(";
    private static String BRACE_RIGHT = ")";

    private EasyResponse response;

    public CommandParser(EasyResponse response){
        this.response = response;
    }

    public String parse(String line){
        int indexLeft = line.indexOf(BRACE_LEFT);
        int indexRight = line.indexOf(BRACE_RIGHT);
        if(indexLeft == -1 || indexRight == -1){
            return line;
        }
        String command = line.substring(0, indexLeft);
        String param = line.substring(indexLeft + BRACE_LEFT.length(), indexRight);
        String result = "";
        try {
            result = executeCommand(command, param);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String executeCommand(String command, String param) throws JsonProcessingException {
        String result = "";
        switch (command){
            case "header":
                result = response.getHeader(param);
                break;
            case "body":
                // 调用json-path提取器获取值
                result = Extractor.jsonExtractor(response.getBody(), param);
                break;
            case "code":
                result = String.valueOf(response.getCode());
                break;
        }
        return  result;
    }
}
