package com.androiddev.artemqa.gototrip.common.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.felipecsl.asymmetricgridview.library.model.AsymmetricItem;

public class Photo  implements AsymmetricItem {
    private String mSourceId;
    private String mPhotoId;
    private String mPhotoDescription;
    private String mPhotoOriginalUrl;
    private String mPhotoThumbnailUrl;
    private int mColumnSpan;
    private int mRowSpan;
    public Photo() {
        mColumnSpan = 1;
        mRowSpan = 1;
    }

    public Photo(String sourceId, String photoId, String photoOriginalUrl, String photoThumbnailUrl) {
        mSourceId = sourceId;
        mPhotoId = photoId;
        mPhotoOriginalUrl = photoOriginalUrl;
        mPhotoThumbnailUrl = photoThumbnailUrl;
    }

    public String getSourceId() {
        return mSourceId;
    }

    public void setSourceId(String sourceId) {
        mSourceId = sourceId;
    }

    public String getPhotoId() {
        return mPhotoId;
    }

    public void setPhotoId(String photoId) {
        mPhotoId = photoId;
    }

    public String getPhotoDescription() {
        return mPhotoDescription;
    }

    public void setPhotoDescription(String photoDescription) {
        mPhotoDescription = photoDescription;
    }

    public String getPhotoOriginalUrl() {
        return mPhotoOriginalUrl;
    }

    public void setPhotoOriginalUrl(String photoOriginalUrl) {
        mPhotoOriginalUrl = photoOriginalUrl;
    }

    public String getPhotoThumbnailUrl() {
        return mPhotoThumbnailUrl;
    }

    public void setPhotoThumbnailUrl(String photoThumbnailUrl) {
        mPhotoThumbnailUrl = photoThumbnailUrl;
    }

    public void setColumnSpan( int columnSpan) {
        mRowSpan = columnSpan ;
    }

    public void setRowSpan(int rowSpan) {
        mRowSpan = rowSpan;
    }

    @Override
    public int getColumnSpan() {
        return mColumnSpan;
    }

    @Override
    public int getRowSpan() {
        return mRowSpan;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Photo(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mColumnSpan);
        dest.writeInt(mRowSpan);
//        dest.writeInt(position);

    }
    /* Parcelable interface implementation */
    public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {
        @Override public Photo createFromParcel(@NonNull Parcel in) {
            return new Photo(in);
        }

        @Override @NonNull public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    private void readFromParcel(Parcel in) {
        mColumnSpan = in.readInt();
        mRowSpan = in.readInt();
//        position = in.readInt();
    }

}
