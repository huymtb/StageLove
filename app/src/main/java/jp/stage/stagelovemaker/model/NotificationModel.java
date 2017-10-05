package jp.stage.stagelovemaker.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Created by congnguyen on 8/28/17.
 */

public class NotificationModel implements Parcelable {
    private String body;
    private String title;
    private int senderId;
    private String type;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public void setSenderId(String senderId) {
        if (!TextUtils.isEmpty(senderId)) {
            this.senderId = Integer.parseInt(senderId);
        } else {
            this.senderId = 0;
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.body);
        dest.writeString(this.title);
        dest.writeInt(this.senderId);
        dest.writeString(this.type);
    }

    public NotificationModel() {
    }

    protected NotificationModel(Parcel in) {
        this.body = in.readString();
        this.title = in.readString();
        this.senderId = in.readInt();
        this.type = in.readString();
    }

    public static final Parcelable.Creator<NotificationModel> CREATOR = new Parcelable.Creator<NotificationModel>() {
        @Override
        public NotificationModel createFromParcel(Parcel source) {
            return new NotificationModel(source);
        }

        @Override
        public NotificationModel[] newArray(int size) {
            return new NotificationModel[size];
        }
    };
}
