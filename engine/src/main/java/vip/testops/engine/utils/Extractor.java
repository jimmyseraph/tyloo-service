package vip.testops.engine.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

public class Extractor {

    /**
     * json path提取器
     * @param content 需要提前的json字符串
     * @param path json path表达式
     * @return 提取出来的字符串
     */
    public static String jsonExtractor(String content, String path) throws JsonProcessingException {
        Object res = JsonPath.read(content, path);
        if(res instanceof String) {
            return res.toString();
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(res);
        }
    }
}
