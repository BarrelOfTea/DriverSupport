package com.barreloftea.driversupport.domain.usecases;


import android.util.Log;

import com.barreloftea.driversupport.domain.usecases.interfaces.SharedPrefRepository;

public class SaveSoundVolumeUC {

    SharedPrefRepository repository;

    public SaveSoundVolumeUC(SharedPrefRepository repository){
        this.repository = repository;
    }


    public void execute(int v){

        float vol = ((float) v ) / 100;
        Log.v("aaa", "set vol "+vol);
        repository.saveSoundVolume(v);
    }

}
