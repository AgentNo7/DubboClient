package com.inverse.plugin;

/**
 * @author keeper
 * @since 2021/1/14
 */
public class ExecuteResult<T> {

    private Boolean success;

    private T data;

    private String errMsg;

    public ExecuteResult(Boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public static <T> ExecuteResult<T> success(T data) {
        return new ExecuteResult<>(true, data);
    }

    public static <T> ExecuteResult<T> fail() {
        return new ExecuteResult<>(false, null);
    }

    public static <T> ExecuteResult<T> fail(String errMsg) {
        ExecuteResult<T> tExecuteResult = new ExecuteResult<>(false, null);
        tExecuteResult.errMsg = errMsg;
        return tExecuteResult;
    }
}
