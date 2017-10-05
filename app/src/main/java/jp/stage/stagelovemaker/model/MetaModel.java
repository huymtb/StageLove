package jp.stage.stagelovemaker.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by congn on 8/11/2017.
 */

public class MetaModel implements Parcelable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("birthday")
    @Expose
    private String birthday;
    @SerializedName("about_me")
    @Expose
    private String aboutMe;
    @SerializedName("current_work")
    @Expose
    private String currentWork;
    @SerializedName("school")
    @Expose
    private String school;
    @SerializedName("code_number")
    @Expose
    private String codeNumber;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getCurrentWork() {
        return currentWork;
    }

    public void setCurrentWork(String currentWork) {
        this.currentWork = currentWork;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getCodeNumber() {
        return codeNumber;
    }

    public void setCodeNumber(String codeNumber) {
        this.codeNumber = codeNumber;
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
        dest.writeString(this.birthday);
        dest.writeString(this.aboutMe);
        dest.writeString(this.currentWork);
        dest.writeString(this.school);
        dest.writeString(this.codeNumber);
        dest.writeString(this.created);
        dest.writeString(this.modified);
    }

    public MetaModel() {
    }

    protected MetaModel(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.birthday = in.readString();
        this.aboutMe = in.readString();
        this.currentWork = in.readString();
        this.school = in.readString();
        this.codeNumber = in.readString();
        this.created = in.readString();
        this.modified = in.readString();
    }

    public static final Parcelable.Creator<MetaModel> CREATOR = new Parcelable.Creator<MetaModel>() {
        @Override
        public MetaModel createFromParcel(Parcel source) {
            return new MetaModel(source);
        }

        @Override
        public MetaModel[] newArray(int size) {
            return new MetaModel[size];
        }
    };
}
