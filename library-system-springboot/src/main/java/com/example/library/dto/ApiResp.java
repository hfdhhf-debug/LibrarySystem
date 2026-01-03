package com.example.library.dto;

public class ApiResp<T> {
    private boolean success;
    private String message;
    private T data;

    public ApiResp() {}

    public ApiResp(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResp<T> ok(T data) { return new ApiResp<>(true, null, data); }
    public static <T> ApiResp<T> okMsg(String msg, T data) { return new ApiResp<>(true, msg, data); }
    public static <T> ApiResp<T> fail(String msg) { return new ApiResp<>(false, msg, null); }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}
