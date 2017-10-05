package jp.stage.stagelovemaker.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by congn on 8/11/2017.
 */

public class SettingModel implements Parcelable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("notify_new_matches")
    @Expose
    private Boolean notifyNewMatches;
    @SerializedName("notify_messages")
    @Expose
    private Boolean notifyMessages;
    @SerializedName("notify_message_likes")
    @Expose
    private Boolean notifyMessageLikes;
    @SerializedName("notify_super_likes")
    @Expose
    private Boolean notifySuperLikes;
    @SerializedName("distance_unit")
    @Expose
    private String distanceUnit;
    @SerializedName("show_me_on_stage_maker")
    @Expose
    private Boolean showMeOnStageMaker;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("modified")
    @Expose
    private String modified;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getNotifyNewMatches() {
        return notifyNewMatches;
    }

    public void setNotifyNewMatches(Boolean notifyNewMatches) {
        this.notifyNewMatches = notifyNewMatches;
    }

    public Boolean getNotifyMessages() {
        return notifyMessages;
    }

    public void setNotifyMessages(Boolean notifyMessages) {
        this.notifyMessages = notifyMessages;
    }

    public Boolean getNotifyMessageLikes() {
        return notifyMessageLikes;
    }

    public void setNotifyMessageLikes(Boolean notifyMessageLikes) {
        this.notifyMessageLikes = notifyMessageLikes;
    }

    public Boolean getNotifySuperLikes() {
        return notifySuperLikes;
    }

    public void setNotifySuperLikes(Boolean notifySuperLikes) {
        this.notifySuperLikes = notifySuperLikes;
    }

    public String getDistanceUnit() {
        return distanceUnit;
    }

    public void setDistanceUnit(String distanceUnit) {
        this.distanceUnit = distanceUnit;
    }

    public Boolean getShowMeOnStageMaker() {
        return showMeOnStageMaker;
    }

    public void setShowMeOnStageMaker(Boolean showMeOnStageMaker) {
        this.showMeOnStageMaker = showMeOnStageMaker;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.notifyNewMatches);
        dest.writeValue(this.notifyMessages);
        dest.writeValue(this.notifyMessageLikes);
        dest.writeValue(this.notifySuperLikes);
        dest.writeString(this.distanceUnit);
        dest.writeValue(this.showMeOnStageMaker);
        dest.writeString(this.created);
        dest.writeString(this.modified);
    }

    public SettingModel() {
        notifyMessages = false;
        notifyNewMatches = false;
        notifyMessageLikes = false;
        notifySuperLikes = false;
        showMeOnStageMaker = false;
    }

    protected SettingModel(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.notifyNewMatches = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.notifyMessages = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.notifyMessageLikes = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.notifySuperLikes = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.distanceUnit = in.readString();
        this.showMeOnStageMaker = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.created = in.readString();
        this.modified = in.readString();
    }

    public static final Creator<SettingModel> CREATOR = new Creator<SettingModel>() {
        @Override
        public SettingModel createFromParcel(Parcel source) {
            return new SettingModel(source);
        }

        @Override
        public SettingModel[] newArray(int size) {
            return new SettingModel[size];
        }
    };
}
