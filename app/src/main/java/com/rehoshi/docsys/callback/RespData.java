package com.rehoshi.docsys.callback;

public class RespData<T> {
    private boolean success;
    private int code;
    private T data;
    private String msg;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public interface Code {
        int SUCCEED = 200 ;
        int ERROR = 500 ;
        int TOKEN_TIME_OUT = 1000 ;
    }
}
