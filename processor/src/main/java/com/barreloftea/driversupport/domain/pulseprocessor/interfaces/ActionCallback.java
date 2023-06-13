package com.barreloftea.driversupport.domain.pulseprocessor.interfaces;

public interface ActionCallback {
    public void onSuccess(Object data);

    public void onFail(int errorCode, String msg);
}
