package vip.testops.engine.http;

import okhttp3.Headers;

public interface EasyResponse {

    String getBody();

    <T> T getBody(Class<T> bodyType);

    String getHeader(String headerName);

    Headers getHeaders();

    String getCookie(String cookieName);

    int getCode();
}
