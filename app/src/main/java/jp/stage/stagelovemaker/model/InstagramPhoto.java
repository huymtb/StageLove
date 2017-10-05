package jp.stage.stagelovemaker.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by congn on 8/4/2017.
 */

public class InstagramPhoto implements Parcelable {
    private String id;
    private String low_resolution_url;
    private String thumbnail_url;
    private String standard_resolution_url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLow_resolution_url() {
        return low_resolution_url;
    }

    public void setLow_resolution_url(String low_resolution_url) {
        this.low_resolution_url = low_resolution_url;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public String getStandard_resolution_url() {
        return standard_resolution_url;
    }

    public void setStandard_resolution_url(String standard_resolution_url) {
        this.standard_resolution_url = standard_resolution_url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.low_resolution_url);
        dest.writeString(this.thumbnail_url);
        dest.writeString(this.standard_resolution_url);
    }

    public InstagramPhoto() {
    }

    protected InstagramPhoto(Parcel in) {
        this.id = in.readString();
        this.low_resolution_url = in.readString();
        this.thumbnail_url = in.readString();
        this.standard_resolution_url = in.readString();
    }

    public static final Parcelable.Creator<InstagramPhoto> CREATOR = new Parcelable.Creator<InstagramPhoto>() {
        @Override
        public InstagramPhoto createFromParcel(Parcel source) {
            return new InstagramPhoto(source);
        }

        @Override
        public InstagramPhoto[] newArray(int size) {
            return new InstagramPhoto[size];
        }
    };
}
