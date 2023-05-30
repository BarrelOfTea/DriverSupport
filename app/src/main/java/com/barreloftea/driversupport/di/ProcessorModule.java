package com.barreloftea.driversupport.di;



import android.provider.MediaStore;

import com.alexvas.repository.VideoRepositoryImpl;
import com.barreloftea.driversupport.cameraservice.interfaces.VideoRepository;
import com.barreloftea.driversupport.cameraservice.service.CameraService;
import com.barreloftea.driversupport.processor.Processor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;
import dagger.hilt.components.SingletonComponent;

@Module
//@InstallIn(ViewModelComponent.class)
public class ProcessorModule {

    @Provides
    //@Singleton
    //TODO i am not sure yet whether to make it a singleton or not
    public static CameraService provideCameraService(VideoRepository rep){
        return new CameraService(rep);
    }


    @Provides
    @Singleton
    public static Processor provideProcessor(CameraService cameraService){
        return new Processor(cameraService);
    }

}
