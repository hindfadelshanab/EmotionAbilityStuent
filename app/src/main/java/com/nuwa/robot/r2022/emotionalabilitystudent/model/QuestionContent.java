package com.nuwa.robot.r2022.emotionalabilitystudent.model;


public class QuestionContent       {
     String title ;
     String image ;
     String faceExpression ;
     String bodyMotion ;

    public QuestionContent() {
    }

    public QuestionContent(String title, String image, String faceExpression, String bodyMotion) {
        this.title = title;
        this.image = image;
        this.faceExpression = faceExpression;
        this.bodyMotion = bodyMotion;
    }

    //    protected QuestionContent(Parcel in) {
//        title = in.readString();
//        image = in.readString();
//        faceExpression = in.readString();
//        bodyMotion = in.readString();
//    }
//
//    public static final Creator<QuestionContent> CREATOR = new Creator<QuestionContent>() {
//        @Override
//        public QuestionContent createFromParcel(Parcel in) {
//            return new QuestionContent(in);
//        }
//
//        @Override
//        public QuestionContent[] newArray(int size) {
//            return new QuestionContent[size];
//        }
//    };
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeString(title);
//        parcel.writeString(image);
//        parcel.writeString(faceExpression);
//        parcel.writeString(bodyMotion);
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFaceExpression() {
        return faceExpression;
    }

    public void setFaceExpression(String faceExpression) {
        this.faceExpression = faceExpression;
    }

    public String getBodyMotion() {
        return bodyMotion;
    }

    public void setBodyMotion(String bodyMotion) {
        this.bodyMotion = bodyMotion;
    }

    @Override
    public String toString() {
        return "QuestionContent{" +
                "title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", faceExpression='" + faceExpression + '\'' +
                ", bodyMotion='" + bodyMotion + '\'' +
                '}';
    }
}
