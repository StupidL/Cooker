package me.stupidme.cooker.model.http;

/**
 * A holder of response from server.
 *
 * @param <T> type of contents
 */

public class HttpResult<T> {

    /**
     * result code, may use status code of HTTP response instead of this in later version.
     */
    private int resultCode;

    /**
     * result message,  may use response message of HTTP instead of this in later version.
     */
    private String resultMessage;

    /**
     * data of server response.
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

    @Override
    public String toString() {
        return "resultCode: " + resultCode + "\n"
                + "resultMessage: " + resultMessage + "\n"
                + "data: " + data.toString() + "\n";
    }
}
