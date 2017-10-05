package jp.stage.stagelovemaker.model;

import android.graphics.Bitmap;

/**
 * Created by congn on 8/11/2017.
 */

public class SignUpModel {
    String email = "";
    String username = "";
    String password = "";
    String first_name = "";
    String last_name = "";
    String birthday = "";
    int gender = -1;
    Bitmap avatar;

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }

    public String getLink_avatar() {
        return link_avatar;
    }

    public void setLink_avatar(String link_avatar) {
        this.link_avatar = link_avatar;
    }

    String link_avatar;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}
