package vip.testops.engine.http.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Headers;
import okhttp3.Response;
import vip.testops.engine.http.EasyResponse;

import java.util.Objects;

public class OkHttpResponse implements EasyResponse {

    private Response response;
    private String bodyString;
    private String url;
    private Headers headers;

    public OkHttpResponse(String url, Response response) {
        this.url = url;
        this.response = response;
        this.bodyString = Objects.requireNonNull(response.body()).toString();
        this.headers = response.headers();
    }

    @Override
    public String getBody() {
        return this.bodyString;
    }

    @Override
    public <T> T getBody(Class<T> bodyType) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(this.bodyString, bodyType);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getHeader(String headerName) {
        return this.headers.get(headerName);
    }

    @Override
    public Headers getHeaders() {
        return this.headers;
    }

    @Override
    public int getCode() {
        return this.response.code();
    }
}
