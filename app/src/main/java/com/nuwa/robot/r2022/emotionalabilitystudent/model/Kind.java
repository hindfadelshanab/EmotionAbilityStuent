package com.nuwa.robot.r2022.emotionalabilitystudent.model;

import android.os.Parcel;
import android.os.Parcelable;


public class Kind  implements Parcelable {
    private String kindTitle ;
    private String kindImage ;

    protected Kind(Parcel in) {
        kindTitle = in.readString();
        kindImage = in.readString();
    }

    public Kind() {
    }

    public static final Creator<Kind> CREATOR = new Creator<Kind>() {
        @Override
        public Kind createFromParcel(Parcel in) {
            return new Kind(in);
        }

        @Override
        public Kind[] newArray(int size) {
            return new Kind[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(kindTitle);
        parcel.writeString(kindImage);
    }

    public String getKindTitle() {
        return kindTitle;
    }

    public void setKindTitle(String kindTitle) {
        this.kindTitle = kindTitle;
    }

    public String getKindImage() {
        return kindImage;
    }

    public void setKindImage(String kindImage) {
        this.kindImage = kindImage;
    }
}
