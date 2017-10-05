package jp.stage.stagelovemaker.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by congn on 8/22/2017.
 */

public class UsersPageModel implements Parcelable {
    @SerializedName("total_record")
    @Expose
    private Integer totalRecord;
    @SerializedName("total_page")
    @Expose
    private Integer totalPage;
    @SerializedName("current_page")
    @Expose
    private String currentPage;
    @SerializedName("records")
    @Expose
    private List<UserInfoModel> records;

    public Integer getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(Integer totalRecord) {
        this.totalRecord = totalRecord;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public List<UserInfoModel> getRecords() {
        return records;
    }

    public void setRecords(List<UserInfoModel> records) {
        if(this.records == null){
            this.records = new ArrayList<>();
        }
        this.records.clear();
        this.records.addAll(records);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.totalRecord);
        dest.writeValue(this.totalPage);
        dest.writeString(this.currentPage);
        dest.writeTypedList(this.records);
    }

    public UsersPageModel() {
    }

    protected UsersPageModel(Parcel in) {
        this.totalRecord = (Integer) in.readValue(Integer.class.getClassLoader());
        this.totalPage = (Integer) in.readValue(Integer.class.getClassLoader());
        this.currentPage = in.readString();
        this.records = in.createTypedArrayList(UserInfoModel.CREATOR);
    }

    public static final Parcelable.Creator<UsersPageModel> CREATOR = new Parcelable.Creator<UsersPageModel>() {
        @Override
        public UsersPageModel createFromParcel(Parcel source) {
            return new UsersPageModel(source);
        }

        @Override
        public UsersPageModel[] newArray(int size) {
            return new UsersPageModel[size];
        }
    };
}
