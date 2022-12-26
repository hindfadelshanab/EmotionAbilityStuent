package com.nuwa.robot.r2022.emotionalabilitystudent.utils;

import androidx.lifecycle.MutableLiveData;

public class StateLiveData<T> extends MutableLiveData<StateData<T>> {

    public void postLoading() {
        postValue(new StateData<T>().loading());
    }


    public void postError(Throwable throwable) {
        postValue(new StateData<T>().error(throwable));
    }

    /**
     * Use this to put the Data on a SUCCESS DataStatus
     * @param data
     */
    public void postSuccess(T data) {
        postValue(new StateData<T>().success(data));
    }

    /**
     * Use this to put the Data on a COMPLETE DataStatus
     */
    public void postComplete() {
        postValue(new StateData<T>().complete());
    }

    @Override
    public void setValue(StateData<T> value) {
        super.setValue(value);
    }
}
