package com.barreloftea.driversupport.di;




import com.alexvas.repository.VideoRepositoryImpl;
import com.alexvas.rtsp.widget.RtspSurfaceView;
import com.barreloftea.driversupport.domain.imageprocessor.interfaces.VideoRepository;
import com.barreloftea.driversupport.domain.imageprocessor.service.CameraService;
import com.barreloftea.driversupport.domain.processor.Processor;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class ServiceModule {

    @Provides
    //TODO i am not sure yet whether to make it a singleton or not
    public static CameraService provideCameraService(VideoRepository rep){
        return new CameraService(rep);
    }


    @Provides
    public static Processor provideProcessor(CameraService cameraService){
        return new Processor(cameraService);
    }

    @Provides
    //TODO i am not sure yet whether to make it a singleton or not
    public static VideoRepository provideVideoRepository(RtspSurfaceView view){
        return new VideoRepositoryImpl(view);
    }

    @Provides
    public static RtspSurfaceView provideRtspSurfaceView(){
        return new RtspSurfaceView();
    }

    //TODO installin: either in sindleton component or in servicecomponent

}
