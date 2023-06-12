package com.barreloftea.driversupport.domain.usecases;

import android.util.Log;

import com.barreloftea.driversupport.domain.usecases.interfaces.SharedPrefRepository;

public class GetSoundVolumeUC {

    SharedPrefRepository repository;

    public GetSoundVolumeUC(SharedPrefRepository repository){
        this.repository = repository;
    }


    public int execute(){
        //int vol = (int) (repository.getSavedSoundVolume()*100);
        int vol = (int) repository.getSavedSoundVolume();
        Log.v("aaa", "get vol "+vol);
        return vol;
    }

}
