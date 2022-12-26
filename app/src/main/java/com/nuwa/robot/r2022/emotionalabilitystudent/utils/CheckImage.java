package com.nuwa.robot.r2022.emotionalabilitystudent.utils;

import android.widget.ImageView;

import com.nuwa.robot.r2022.emotionalabilitystudent.R;

public  class CheckImage {

    public  static  void setImageByName(String imageName , ImageView ii ){
        switch (imageName) {

            case "playing1":
                ii.setBackgroundResource(R.drawable.playing1);
                break;
            case "playing2":
                ii.setBackgroundResource(R.drawable.playing2);
                break;
            case "writing1":
                ii.setBackgroundResource(R.drawable.writing1);
                break;
            case "writing2":
                ii.setBackgroundResource(R.drawable.writing2);
                break;
            case "eating1":
                ii.setBackgroundResource(R.drawable.eating1);
                break;
            case "eating2":
                ii.setBackgroundResource(R.drawable.eating2);
                break;
            case "cleaning1":
                ii.setBackgroundResource(R.drawable.cleaning1);
                break;
            case "cleaning2":
                ii.setBackgroundResource(R.drawable.cleaning2);
                break;
            case "reading1":
                ii.setBackgroundResource(R.drawable.reading1);
                break;
            case "reading2":
                ii.setBackgroundResource(R.drawable.reading2);
                break;
            case "listening1":
                ii.setBackgroundResource(R.drawable.listening1);
                break;
            case "listening2":
                ii.setBackgroundResource(R.drawable.listening2);
                break;
            case "sleeping1":
                ii.setBackgroundResource(R.drawable.sleeping1);
                break;
            case "sleeping2":
                ii.setBackgroundResource(R.drawable.sleeping2);
                break;
            case "driving1":
                ii.setBackgroundResource(R.drawable.driving1);
                break;
            case "driving2":
                ii.setBackgroundResource(R.drawable.driving2);
                break;

            case "cooking1":
                ii.setBackgroundResource(R.drawable.cooking1);
                break;
            case "cooking2":
                ii.setBackgroundResource(R.drawable.cooking2);
                break;
            case "wearing1":
                ii.setBackgroundResource(R.drawable.wearing1);
                break;
            case "wearing2":
                ii.setBackgroundResource(R.drawable.wearing2);
                break;

            case "eating11":
                ii.setBackgroundResource(R.drawable.eating11);
                break;
            case "eating12":
                ii.setBackgroundResource(R.drawable.eating12);
                break;
            case "showering1":
                ii.setBackgroundResource(R.drawable.showering1);
                break;
            case "showering2":
                ii.setBackgroundResource(R.drawable.showering2);
                break;
        }
    }
}
