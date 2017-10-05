package jp.stage.stagelovemaker.network;

import com.google.gson.JsonElement;

/**
 * Created by congn on 8/7/2017.
 */

public class InstargramResponeModel {
    JsonElement data;

    public JsonElement getData() {
        return data;
    }

    public void setData(JsonElement data) {
        this.data = data;
    }
}
