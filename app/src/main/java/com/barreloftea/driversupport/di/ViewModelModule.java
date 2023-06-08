package com.barreloftea.driversupport.di;

import com.barreloftea.driversupport.domain.usecases.GetBluetoothDevicesUC;
import com.barreloftea.driversupport.domain.usecases.GetSavedDevicesUC;
import com.barreloftea.driversupport.domain.usecases.interfaces.BluetoothRepository;
import com.barreloftea.driversupport.domain.usecases.interfaces.SharedPrefRepository;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;


@Module
@InstallIn(ViewModelComponent.class)
public class ViewModelModule {

    @Provides
    public static GetSavedDevicesUC provideGetSavedDevicesUC(SharedPrefRepository repository){
        return new GetSavedDevicesUC(repository);
    }

    @Provides
    public static GetBluetoothDevicesUC provideGetBluetoothDevicesUC(BluetoothRepository repository){
        return new GetBluetoothDevicesUC(repository);
    }


}
