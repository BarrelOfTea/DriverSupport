package com.barreloftea.driversupport.presentation.ui.fragments.settingsflow

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.barreloftea.driversupport.domain.usecases.DeleteDataUC
import com.barreloftea.driversupport.domain.usecases.GetSoundVolumeUC
import com.barreloftea.driversupport.domain.usecases.SaveSignalSoundUC
import com.barreloftea.driversupport.domain.usecases.SaveSoundVolumeUC
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    getSoundVolumeUC: GetSoundVolumeUC,
    saveSoundVolumeUC: SaveSoundVolumeUC,
    saveSignalSoundUC: SaveSignalSoundUC,
    deleteDataUC: DeleteDataUC
): ViewModel() {

    private val getSoundVolume : GetSoundVolumeUC
    private val saveSoundVolume : SaveSoundVolumeUC
    private val saveSignalSound : SaveSignalSoundUC
    private val deleteData : DeleteDataUC

    var volumeLD = MutableLiveData<Int>()

    init {
        getSoundVolume = getSoundVolumeUC
        saveSoundVolume = saveSoundVolumeUC
        saveSignalSound = saveSignalSoundUC
        deleteData = deleteDataUC
    }

    fun saveSoundVolume(volume : Int){
        saveSoundVolume.execute(volume)
    }

    fun getSoundVolume(){
        volumeLD.value = getSoundVolume.execute()
    }

    fun saveSignalSoundResId(resid: Int){
        saveSignalSound.execute(resid)
    }

    fun deleteData(){
        deleteData.execute()
    }

}