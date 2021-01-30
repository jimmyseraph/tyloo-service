package vip.testops.engine.http.impl;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.testops.engine.http.EasyRequest;
import vip.testops.engine.http.EasyResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OkHttpRequest implements EasyRequest {
    private final Logger logger = LoggerFactory.getLogger(OkHttpRequest.class);

    private final OkHttpClient newClient = new OkHttpClient.Builder()
            .followRedirects(false)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();
    private String url;
    private String method;
    private Map<String, String> headers;
    private Map<String, String> cookies;
    private Map<String, String> queryParams;
    private RequestBody requestBody;

    public OkHttpRequest(){
        headers = new HashMap<>();
        cookies = new HashMap<>();
        queryParams = new HashMap<>();
        method = "GET";
        requestBody = null;
    }

    @Override
    public EasyRequest setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public String getUrl() {
        return this.url;
    }

    @Override
    public EasyRequest addQueryParam(String paramKey, String value) {
        this.queryParams.put(paramKey, value);
        return this;
    }

    @Override
    public String getQueryParam(String paramKey) {
        return this.queryParams.get(paramKey);
    }

    @Override
    public EasyRequest setQueryParam(Map<String, String> paramMap) {
        this.queryParams = paramMap;
        return this;
    }

    @Override
    public EasyRequest addHeader(String headerName, String headerValue) {
        this.headers.put(headerName, headerValue);
        return this;
    }

    @Override
    public EasyRequest setHeader(Map<String, String> headerMap) {
        this.headers = headerMap;
        return this;
    }

    @Override
    public Map<String, String> getHeaders() {

        return headers;
    }

    @Override
    public String getHeader(String headerName) {
        return this.headers.get(headerName);
    }

    @Override
    public EasyRequest addCookie(String cookieName, String cookieValue) {
        return null;
    }

    @Override
    public EasyRequest setCookie(Map<String, String> cookieMap) {
        return null;
    }

    @Override
    public EasyRequest setBody(String mimeType, String content) {
        return null;
    }

    @Override
    public EasyRequest setBody(RequestBody body) {
        return null;
    }

    @Override
    public EasyRequest setMethod(String method) {
        return null;
    }

    @Override
    public String getMethod() {
        return null;
    }

    @Override
    public EasyResponse execute() throws IOException {
        return null;
    }
}
