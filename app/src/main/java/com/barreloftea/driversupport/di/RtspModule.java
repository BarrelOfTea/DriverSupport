package com.barreloftea.driversupport.di;


import com.alexvas.repository.VideoRepositoryImpl;
import com.alexvas.rtsp.widget.RtspSurfaceView;
import com.barreloftea.driversupport.cameraservice.interfaces.VideoRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class RtspModule {

    @Provides
    //@Singleton
    //TODO i am not sure yet whether to make it a singleton or not
    public static VideoRepository provideVideoRepository(RtspSurfaceView view){
        return new VideoRepositoryImpl(view);
    }

    @Provides
    public static RtspSurfaceView provideRtspSurfaceView(){
        return new RtspSurfaceView();
    }

}
