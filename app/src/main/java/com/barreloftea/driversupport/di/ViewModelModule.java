package com.barreloftea.driversupport.di;

import com.barreloftea.driversupport.domain.usecases.GetConnectedDevicesUC;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;


@Module
@InstallIn(ViewModelComponent.class)
public class ViewModelModule {

    @Provides
    public static GetConnectedDevicesUC provideGetConnectedDevicesUC(){
        return new GetConnectedDevicesUC();
    }

}
