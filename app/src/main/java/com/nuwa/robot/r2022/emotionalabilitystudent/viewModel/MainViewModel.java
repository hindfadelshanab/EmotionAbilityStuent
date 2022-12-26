package com.nuwa.robot.r2022.emotionalabilitystudent.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.nuwa.robot.r2022.emotionalabilitystudent.utils.StateLiveData;

public class MainViewModel extends AndroidViewModel {
    private final StateLiveData<Integer> countLiveData = new StateLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
    }
    public void setCountLiveData(int  count) {
        countLiveData.postSuccess(count);
    }
    public StateLiveData<Integer> getCountLiveData() {
        return countLiveData;
    }

}
