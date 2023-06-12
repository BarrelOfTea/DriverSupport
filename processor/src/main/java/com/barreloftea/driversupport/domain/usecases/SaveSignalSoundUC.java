package com.barreloftea.driversupport.domain.usecases;

import com.barreloftea.driversupport.domain.usecases.interfaces.SharedPrefRepository;

public class SaveSignalSoundUC {

    SharedPrefRepository repository;

    public SaveSignalSoundUC(SharedPrefRepository repository){
        this.repository = repository;
    }


    public void execute(int resid){
        repository.saveSignalSoundResID(resid);
    }

}
