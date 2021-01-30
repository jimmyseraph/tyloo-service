package vip.testops.engine.common;

import lombok.Data;

@Data
public class Response<T> {
    private Integer code;
    private String message;
    private T data;

    public void commonSuccess(){
        setCode(BaseCodeEnum.COMMON_SUCCESS.getCode());
        setMessage(BaseCodeEnum.COMMON_SUCCESS.getDescEN());
    }

    public void dataSuccess(T data){
        setCode(BaseCodeEnum.COMMON_SUCCESS.getCode());
        setMessage(BaseCodeEnum.COMMON_SUCCESS.getDescEN());
        setData(data);
    }

    public void paramMissError(String param){
        setCode(BaseCodeEnum.PARAM_MISS.getCode());
        setMessage(String.format(BaseCodeEnum.PARAM_MISS.getDescEN(), param));
    }

    public void paramIllegalError(String param){
        setCode(BaseCodeEnum.PARAM_ILLEGAL.getCode());
        setMessage(String.format(BaseCodeEnum.PARAM_ILLEGAL.getDescEN(), param));
    }

    public void serviceError(String reason){
        setCode(BaseCodeEnum.SERVICE_ERROR.getCode());
        setMessage(String.format(BaseCodeEnum.SERVICE_ERROR.getDescEN(), reason));
    }
}
