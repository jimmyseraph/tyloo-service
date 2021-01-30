package vip.testops.engine.http;

import okhttp3.RequestBody;

import java.io.IOException;
import java.util.Map;

public interface EasyRequest {
    String PLAIN = "text/plain; charset=utf-8";
    String FORM = "application/x-www-form-urlencoded";
    String JSON = "application/json; charset=utf-8";
    String SOAP11 = "application/xml; charset=utf-8";
    String SOAP12 = "application/xml+soap; charset=utf-8";

    EasyRequest setUrl(String url);
    String getUrl();

    EasyRequest addQueryParam(String paramKey, String value);
    String getQueryParam(String paramKey);

    EasyRequest setQueryParam(Map<String, String> paramMap);

    EasyRequest addHeader(String headerName, String headerValue);

    EasyRequest setHeader(Map<String, String> headerMap);

    Map<String, String> getHeaders();
    String getHeader(String headerName);

    EasyRequest addCookie(String cookieName, String cookieValue);

    EasyRequest setCookie(Map<String, String> cookieMap);

    EasyRequest setBody(String mimeType, String content);
    EasyRequest setBody(RequestBody body);

    EasyRequest setMethod(String method);
    String getMethod();

    EasyResponse execute() throws IOException;

}
