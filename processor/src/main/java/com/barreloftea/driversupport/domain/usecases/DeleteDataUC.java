package com.barreloftea.driversupport.domain.usecases;

import com.barreloftea.driversupport.domain.usecases.interfaces.SharedPrefRepository;

public class DeleteDataUC {

    SharedPrefRepository repository;

    public DeleteDataUC(SharedPrefRepository repository){
        this.repository = repository;
    }


    public void execute(){
        repository.deleteAll();
    }

}
