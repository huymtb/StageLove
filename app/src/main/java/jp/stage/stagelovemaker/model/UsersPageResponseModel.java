package jp.stage.stagelovemaker.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by congn on 8/22/2017.
 */

public class UsersPageResponseModel implements Parcelable {
    @SerializedName("error_code")
    @Expose
    private Integer errorCode;
    @SerializedName("error_msg")
    @Expose
    private String errorMsg;
    @SerializedName("UsersPage")
    @Expose
    private UsersPageModel usersPage;

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

    public UsersPageModel getUsersPage() {
        return usersPage;
    }

    public void setUsersPage(UsersPageModel usersPage) {
        this.usersPage = usersPage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.errorCode);
        dest.writeString(this.errorMsg);
        dest.writeParcelable(this.usersPage, flags);
    }

    public UsersPageResponseModel() {
    }

    protected UsersPageResponseModel(Parcel in) {
        this.errorCode = (Integer) in.readValue(Integer.class.getClassLoader());
        this.errorMsg = in.readString();
        this.usersPage = in.readParcelable(UsersPageModel.class.getClassLoader());
    }

    public static final Parcelable.Creator<UsersPageResponseModel> CREATOR = new Parcelable.Creator<UsersPageResponseModel>() {
        @Override
        public UsersPageResponseModel createFromParcel(Parcel source) {
            return new UsersPageResponseModel(source);
        }

        @Override
        public UsersPageResponseModel[] newArray(int size) {
            return new UsersPageResponseModel[size];
        }
    };
}
