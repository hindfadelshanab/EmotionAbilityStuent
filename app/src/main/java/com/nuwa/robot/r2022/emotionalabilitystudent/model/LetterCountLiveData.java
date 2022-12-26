package com.nuwa.robot.r2022.emotionalabilitystudent.model;

import androidx.annotation.MainThread;

import com.nuwa.robot.r2022.emotionalabilitystudent.utils.StateLiveData;

public class LetterCountLiveData extends StateLiveData<Integer> {
    private  static  LetterCountLiveData letterCountLiveData ;
    @MainThread
    public static LetterCountLiveData get(){
        if (letterCountLiveData == null)
            letterCountLiveData = new LetterCountLiveData();
        return letterCountLiveData;
    }

    public static LetterCountLiveData reset(){
        letterCountLiveData = new LetterCountLiveData();
        return letterCountLiveData;
    }


}
