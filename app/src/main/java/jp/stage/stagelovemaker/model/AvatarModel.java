package jp.stage.stagelovemaker.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by congn on 8/14/2017.
 */

public class AvatarModel implements Parcelable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("number_index")
    @Expose
    private Integer numberIndex;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getNumberIndex() {
        return numberIndex;
    }

    public void setNumberIndex(Integer numberIndex) {
        this.numberIndex = numberIndex;
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
        dest.writeString(this.url);
        dest.writeValue(this.numberIndex);
        dest.writeString(this.created);
        dest.writeString(this.modified);
    }

    public AvatarModel() {
    }

    protected AvatarModel(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.url = in.readString();
        this.numberIndex = (Integer) in.readValue(Integer.class.getClassLoader());
        this.created = in.readString();
        this.modified = in.readString();
    }

    public static final Parcelable.Creator<AvatarModel> CREATOR = new Parcelable.Creator<AvatarModel>() {
        @Override
        public AvatarModel createFromParcel(Parcel source) {
            return new AvatarModel(source);
        }

        @Override
        public AvatarModel[] newArray(int size) {
            return new AvatarModel[size];
        }
    };
}
