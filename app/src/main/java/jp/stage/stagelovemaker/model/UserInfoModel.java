package jp.stage.stagelovemaker.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by congn on 8/11/2017.
 */

public class UserInfoModel implements Parcelable {
    @SerializedName("avatars")
    @Expose
    private List<AvatarModel> avatars = new ArrayList<>();
    @SerializedName("discover")
    @Expose
    private DiscoverModel discover;
    @SerializedName("setting")
    @Expose
    private SettingModel setting;
    @SerializedName("meta")
    @Expose
    private MetaModel meta;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("gender")
    @Expose
    private Boolean gender;
    @SerializedName("age")
    @Expose
    private Integer age;
    @SerializedName("latitude")
    @Expose
    private double latitude;
    @SerializedName("longitude")
    @Expose
    private double longitude;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("modified")
    @Expose
    private String modified;
    @SerializedName("instagram_user")
    @Expose
    private String instagramUser;

    public List<AvatarModel> getAvatars() {
        return avatars;
    }

    public void setAvatars(List<AvatarModel> avatars) {
        this.avatars = avatars;
    }

    public DiscoverModel getDiscover() {
        return discover;
    }

    public void setDiscover(DiscoverModel discover) {
        this.discover = discover;
    }

    public SettingModel getSetting() {
        return setting;
    }

    public void setSetting(SettingModel setting) {
        this.setting = setting;
    }

    public MetaModel getMeta() {
        return meta;
    }

    public void setMeta(MetaModel meta) {
        this.meta = meta;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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

    public String getInstagramUser() {
        return instagramUser;
    }

    public void setInstagramUser(String instagramUser) {
        this.instagramUser = instagramUser;
    }

    public String getNotificationTopic() {
        return "/topics/topic_" + id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.avatars);
        dest.writeParcelable(this.discover, flags);
        dest.writeParcelable(this.setting, flags);
        dest.writeParcelable(this.meta, flags);
        dest.writeValue(this.id);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.username);
        dest.writeString(this.email);
        dest.writeValue(this.gender);
        dest.writeValue(this.age);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.created);
        dest.writeString(this.modified);
        dest.writeString(this.instagramUser);
    }

    public UserInfoModel() {
    }

    protected UserInfoModel(Parcel in) {
        this.avatars = in.createTypedArrayList(AvatarModel.CREATOR);
        this.discover = in.readParcelable(DiscoverModel.class.getClassLoader());
        this.setting = in.readParcelable(SettingModel.class.getClassLoader());
        this.meta = in.readParcelable(MetaModel.class.getClassLoader());
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.username = in.readString();
        this.email = in.readString();
        this.gender = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.age = (Integer) in.readValue(Integer.class.getClassLoader());
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.created = in.readString();
        this.modified = in.readString();
        this.instagramUser = in.readString();
    }

    public static final Creator<UserInfoModel> CREATOR = new Creator<UserInfoModel>() {
        @Override
        public UserInfoModel createFromParcel(Parcel source) {
            return new UserInfoModel(source);
        }

        @Override
        public UserInfoModel[] newArray(int size) {
            return new UserInfoModel[size];
        }
    };
}
