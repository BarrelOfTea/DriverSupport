package com.barreloftea.driversupport.di;




import com.alexvas.repository.VideoRepositoryImpl;
import com.alexvas.rtsp.widget.RtspSurfaceView;
import com.barreloftea.driversupport.bleservice.repositories.LedRepositoryImpl;
import com.barreloftea.driversupport.bleservice.repositories.PulseRepositoryImpl;
import com.barreloftea.driversupport.domain.imageprocessor.interfaces.VideoRepository;
import com.barreloftea.driversupport.domain.imageprocessor.service.ImageProcessor;
import com.barreloftea.driversupport.domain.ledcontroller.interfaces.LedRepository;
import com.barreloftea.driversupport.domain.ledcontroller.service.LedController;
import com.barreloftea.driversupport.domain.processor.Processor;
import com.barreloftea.driversupport.domain.pulseprocessor.interfaces.PulseRepository;
import com.barreloftea.driversupport.domain.pulseprocessor.service.PulseProcessor;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class ServiceModule {

    @Provides
    //TODO i am not sure yet whether to make it a singleton or not
    public static ImageProcessor provideImageProcessor(VideoRepository rep){
        return new ImageProcessor(rep);
    }


//    @Provides
//    public static Processor provideProcessor(ImageProcessor cameraService){
//        return new Processor(cameraService);
//    }

//    @Provides
//    public static Processor provideProcessor(PulseProcessor pulseProcessor){
//        return new Processor(pulseProcessor);
//    }

    @Provides
    public static Processor provideProcessor(LedController c){
        return new Processor(c);
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

    @Provides
    public static PulseRepository providePulseRepository(){
        return new PulseRepositoryImpl();
    }

    @Provides
    public static LedRepository provideLedRepository(){
        return new LedRepositoryImpl();
    }

    //TODO installin: either in sindleton component or in servicecomponent

}
