package ravtech.co.il.httpclient.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result<T> {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private T data;
    @SerializedName("error_code")
    @Expose
    private Integer errorCode;
    @SerializedName("errors")
    @Expose
    private String errors;

    public Boolean isSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    /**
     * @return The data
     */
    public T getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(T data) {
        this.data = data;
    }


    public Integer getErrorCode() {

        return errorCode;
    }


    public void setErrorCode(Integer errorCode) {

        this.errorCode = errorCode;
    }


    public String getErrors() {

        return errors;
    }


    public void setErrors(String errors) {

        this.errors = errors;
    }

}
