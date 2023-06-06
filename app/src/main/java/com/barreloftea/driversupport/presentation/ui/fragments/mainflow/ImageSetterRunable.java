package com.barreloftea.driversupport.presentation.ui.fragments.mainflow;

import android.os.Handler;
import android.os.Message;

import com.barreloftea.driversupport.domain.processor.common.ImageBuffer;

public class ImageSetterRunable implements Runnable{

    private Handler h2;
    public ImageSetterRunable(Handler h) {
        this.h2 = h;
    }

    @Override
    public void run() {

        while(ImageBuffer.isProcessorRunning.get()) {
            h2.sendMessage(Message.obtain());
        }


    }

}
