package com.barreloftea.driversupport.di;




import android.content.Context;

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
import com.barreloftea.driversupport.domain.soundcontroller.SoundController;
import com.barreloftea.driversupport.domain.usecases.interfaces.SharedPrefRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class ServiceModule {

    @Provides
    @Singleton
    //TODO i am not sure yet whether to make it a singleton or not
    public static ImageProcessor provideImageProcessor(VideoRepository rep){
        return new ImageProcessor(rep);
    }


    @Provides
    @Singleton
    public static Processor provideProcessor(ImageProcessor imageProcessor, SoundController soundController){
        return new Processor(imageProcessor, soundController);
    }

//    @Provides
//    public static Processor provideProcessor(PulseProcessor pulseProcessor){
//        return new Processor(pulseProcessor);
//    }

//    @Provides
//    public static Processor provideProcessor(LedController c){
//        return new Processor(c);
//    }

    @Provides
    @Singleton
    public static PulseProcessor providePulseProcessor(){
        return new PulseProcessor();
    }

    @Provides
    @Singleton
    public static LedController provideLedController(LedRepository ledRepository){
        return new LedController(ledRepository);
    }
    @Provides
    @Singleton
    //TODO i am not sure yet whether to make it a singleton or not
    public static VideoRepository provideVideoRepository(RtspSurfaceView view){
        return new VideoRepositoryImpl(view);
    }

    @Provides
    @Singleton
    public static RtspSurfaceView provideRtspSurfaceView(){
        return new RtspSurfaceView();
    }

    @Provides
    @Singleton
    public static PulseRepository providePulseRepository(){
        return new PulseRepositoryImpl();
    }

    @Provides
    @Singleton
    public static LedRepository provideLedRepository(){
        return new LedRepositoryImpl();
    }

    @Provides
    public static SoundController provideSoundController(SharedPrefRepository repository){
        return new SoundController(repository);
    }

}
