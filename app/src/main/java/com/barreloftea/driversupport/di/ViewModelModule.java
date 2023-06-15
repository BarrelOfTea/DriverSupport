package com.barreloftea.driversupport.di;

import com.barreloftea.driversupport.domain.usecases.DeleteDataUC;
import com.barreloftea.driversupport.domain.usecases.GetConnectedBTDevicesUC;
import com.barreloftea.driversupport.domain.usecases.GetSavedDevicesUC;
import com.barreloftea.driversupport.domain.usecases.GetSignalsActivationStateUC;
import com.barreloftea.driversupport.domain.usecases.GetSoundVolumeUC;
import com.barreloftea.driversupport.domain.usecases.SaveBluetoothDeviceUC;
import com.barreloftea.driversupport.domain.usecases.SaveSignalActivationStateUC;
import com.barreloftea.driversupport.domain.usecases.SaveSignalSoundUC;
import com.barreloftea.driversupport.domain.usecases.SaveSoundVolumeUC;
import com.barreloftea.driversupport.domain.usecases.SaveWiFiDeviceUC;
import com.barreloftea.driversupport.domain.pulseprocessor.interfaces.BluetoothRepository;
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

//    @Provides
//    public static GetBluetoothDevicesUC provideGetBluetoothDevicesUC(BluetoothRepository repository){
//        return new GetBluetoothDevicesUC(repository);
//    }

    @Provides
    public static SaveWiFiDeviceUC provideSaveWiFiDeviceUC(SharedPrefRepository repository){
        return new SaveWiFiDeviceUC(repository);
    }

    @Provides
    public static GetConnectedBTDevicesUC provideGetConnectedBTDevicesUC(BluetoothRepository repository){
        return new GetConnectedBTDevicesUC(repository);
    }

    @Provides
    public static SaveBluetoothDeviceUC provideSaveBluetoothDeviceUC(SharedPrefRepository repository){
        return new SaveBluetoothDeviceUC(repository);
    }

    @Provides
    public static SaveSoundVolumeUC provideSaveSoundVolumeUC(SharedPrefRepository prefRepository){
        return new SaveSoundVolumeUC(prefRepository);
    }

    @Provides
    public static GetSoundVolumeUC provideGetSoundVolumeUC(SharedPrefRepository prefRepository){
        return new GetSoundVolumeUC(prefRepository);
    }

    @Provides
    public static DeleteDataUC provideDeleteDataUC(SharedPrefRepository prefRepository){
        return new DeleteDataUC(prefRepository);
    }

    @Provides
    public static SaveSignalSoundUC provideSaveSignalSoundUC(SharedPrefRepository prefRepository){
        return new SaveSignalSoundUC(prefRepository);
    }

    @Provides
    public static GetSignalsActivationStateUC provideGetSignalsActivationStateUC(SharedPrefRepository repository){
        return new GetSignalsActivationStateUC(repository);
    }

    @Provides
    public static SaveSignalActivationStateUC provideSaveSignalActivationStateUC(SharedPrefRepository repository){
        return new SaveSignalActivationStateUC(repository);
    }

}
