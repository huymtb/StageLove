package jp.stage.stagelovemaker.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by congn on 8/4/2017.
 */

public class UserInfo implements Parcelable {
    private int id;
    private String firstName;
    private String lastName;
    private int gender;
    private String birthday;
    private String about;
    private String currentWork;
    private String school;
    private ArrayList<String> avatars;
    private String instagram_username;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
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

    public ArrayList<String> getAvatars() {
        return avatars;
    }

    public void setAvatars(ArrayList<String> avatars) {
        this.avatars = avatars;
    }

    public String getInstagram_username() {
        return instagram_username;
    }

    public void setInstagram_username(String instagram_username) {
        this.instagram_username = instagram_username;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeInt(this.gender);
        dest.writeString(this.birthday);
        dest.writeString(this.about);
        dest.writeString(this.currentWork);
        dest.writeString(this.school);
        dest.writeStringList(this.avatars);
        dest.writeString(this.instagram_username);
    }

    public UserInfo() {
    }

    protected UserInfo(Parcel in) {
        this.id = in.readInt();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.gender = in.readInt();
        this.birthday = in.readString();
        this.about = in.readString();
        this.currentWork = in.readString();
        this.school = in.readString();
        this.avatars = in.createStringArrayList();
        this.instagram_username = in.readString();
    }

    public static final Parcelable.Creator<UserInfo> CREATOR = new Parcelable.Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
}
