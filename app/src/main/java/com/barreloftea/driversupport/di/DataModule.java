package com.barreloftea.driversupport.di;


import android.content.Context;

import com.barreloftea.driversupport.bleservice.BluetoothRepositoryImpl;
import com.barreloftea.driversupport.data.SharedPrefRepositoryImpl;
import com.barreloftea.driversupport.domain.usecases.interfaces.BluetoothRepository;
import com.barreloftea.driversupport.domain.usecases.interfaces.SharedPrefRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ActivityContext;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DataModule {

    @Provides
    @Singleton
    public static BluetoothRepository provideBluetoothRepository(){
        return new BluetoothRepositoryImpl();
    }

    @Provides
    @Singleton
    public static SharedPrefRepository provideSharedPrefRepository(@ApplicationContext Context context){
        return new SharedPrefRepositoryImpl(context);
    }

}
