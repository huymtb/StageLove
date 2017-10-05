package jp.stage.stagelovemaker.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by congnguyen on 8/27/17.
 */

public class RoomResponseModel {
    @SerializedName("error_code")
    @Expose
    private Integer errorCode;
    @SerializedName("error_msg")
    @Expose
    private String errorMsg;
    @SerializedName("infoRoom")
    @Expose
    private InfoRoomModel infoRoom;

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public InfoRoomModel getInfoRoom() {
        return infoRoom;
    }

    public void setInfoRoom(InfoRoomModel infoRoom) {
        this.infoRoom = infoRoom;
    }
}
