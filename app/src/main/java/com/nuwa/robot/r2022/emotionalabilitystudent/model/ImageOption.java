package com.nuwa.robot.r2022.emotionalabilitystudent.model;

import android.os.Parcel;
import android.os.Parcelable;


public class ImageOption   implements Parcelable {

    private String image ;
    private  int isCorrect ;
    private String description;
    private boolean isForShow ;

    public ImageOption() {
    }

    protected ImageOption(Parcel in) {
        image = in.readString();
        isCorrect = in.readInt();
        description = in.readString();
        isForShow = in.readByte() != 0;
    }

    public static final Creator<ImageOption> CREATOR = new Creator<ImageOption>() {
        @Override
        public ImageOption createFromParcel(Parcel in) {
            return new ImageOption(in);
        }

        @Override
        public ImageOption[] newArray(int size) {
            return new ImageOption[size];
        }
    };

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(int isCorrect) {
        this.isCorrect = isCorrect;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isForShow() {
        return isForShow;
    }

    public void setForShow(boolean forShow) {
        isForShow = forShow;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(image);
        parcel.writeInt(isCorrect);
        parcel.writeString(description);
        parcel.writeByte((byte) (isForShow ? 1 : 0));
    }

    @Override
    public String toString() {
        return "ImageOption{" +
                "image='" + image + '\'' +
                ", isCorrect=" + isCorrect +
                ", description='" + description + '\'' +
                ", isForShow=" + isForShow +
                '}';
    }
}
