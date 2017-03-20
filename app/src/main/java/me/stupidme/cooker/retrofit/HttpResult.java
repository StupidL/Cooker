package me.stupidme.cooker.retrofit;

/**
 * 该类承载服务器返回的消息
 * Created by StupidL on 2017/3/19.
 */

public class HttpResult<T> {

    /**
     * 服务器返回消息的状态码
     */
    private int resultCode;

    /**
     * 服务器返回消息的提示内容
     */
    private String resultMessage;

    /**
     * 服务器返回的数据，是一个JSON数组
     */
    private T data;


    public int getResultCode() {
        return resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public T getData() {
        return data;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public void setData(T data) {
        this.data = data;
    }
}
