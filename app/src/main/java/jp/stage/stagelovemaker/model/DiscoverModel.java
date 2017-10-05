package jp.stage.stagelovemaker.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by congn on 8/11/2017.
 */

public class DiscoverModel implements Parcelable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("from_age")
    @Expose
    private Integer fromAge;
    @SerializedName("to_age")
    @Expose
    private Integer toAge;
    @SerializedName("filter_distance")
    @Expose
    private Integer filterDistance;
    @SerializedName("filter_gender")
    @Expose
    private Integer filterGender;
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

    public Integer getFromAge() {
        return fromAge;
    }

    public void setFromAge(Integer fromAge) {
        this.fromAge = fromAge;
    }

    public Integer getToAge() {
        return toAge;
    }

    public void setToAge(Integer toAge) {
        this.toAge = toAge;
    }

    public Integer getFilterDistance() {
        return filterDistance;
    }

    public void setFilterDistance(Integer filterDistance) {
        this.filterDistance = filterDistance;
    }

    public Integer getFilterGender() {
        return filterGender;
    }

    public void setFilterGender(Integer filterGender) {
        this.filterGender = filterGender;
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
        dest.writeValue(this.fromAge);
        dest.writeValue(this.toAge);
        dest.writeValue(this.filterDistance);
        dest.writeValue(this.filterGender);
        dest.writeString(this.created);
        dest.writeString(this.modified);
    }

    public DiscoverModel() {
    }

    protected DiscoverModel(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.fromAge = (Integer) in.readValue(Integer.class.getClassLoader());
        this.toAge = (Integer) in.readValue(Integer.class.getClassLoader());
        this.filterDistance = (Integer) in.readValue(Integer.class.getClassLoader());
        this.filterGender = (Integer) in.readValue(Integer.class.getClassLoader());
        this.created = in.readString();
        this.modified = in.readString();
    }

    public static final Parcelable.Creator<DiscoverModel> CREATOR = new Parcelable.Creator<DiscoverModel>() {
        @Override
        public DiscoverModel createFromParcel(Parcel source) {
            return new DiscoverModel(source);
        }

        @Override
        public DiscoverModel[] newArray(int size) {
            return new DiscoverModel[size];
        }
    };
}
