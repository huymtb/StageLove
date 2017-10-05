package jp.stage.stagelovemaker.network;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by congn on 8/11/2017.
 */

public class ResponseModel {
    @SerializedName("status")
    @Expose
    private Integer status;

    @SerializedName("result")
    @Expose
    private JsonElement result;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public JsonElement getResult() {
        return result;
    }

    public void setResult(JsonElement result) {
        this.result = result;
    }
}
