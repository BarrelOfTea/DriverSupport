package com.barreloftea.driversupport.domain.soundcontroller;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.barreloftea.driversupport.domain.R;
import com.barreloftea.driversupport.domain.processor.common.Constants;
import com.barreloftea.driversupport.domain.usecases.interfaces.SharedPrefRepository;

public class SoundController {


    public static final String TAG = SoundController.class.getSimpleName();

    SharedPrefRepository repository;

    public SoundController(SharedPrefRepository rep){
        repository = rep;
    }

    private MediaPlayer player;
    boolean isActivated;

    public void init(Context context){
        this.player = MediaPlayer.create(context, repository.getSignalSoundResId());
        isActivated = true;
        float vol = repository.getSavedSoundVolume();
        player.setVolume(vol, vol);
    }

    public void init(Context context, int resid){
        this.player = MediaPlayer.create(context, resid);
        isActivated = true;
        float vol = repository.getSavedSoundVolume();
        player.setVolume(vol, vol);
    }

    public void play(){
        if(!player.isPlaying()){
            player.start();
            Log.v(TAG, "sound controller started");
        }
    }

    public void setVolume(float volume){
        player.setVolume(volume, volume);
    }

    public void pause(){
        if (player.isPlaying()) {
            player.pause();
            player.seekTo(0);
            Log.v(TAG, "sound controller paused");
        }
    }

    public void destroy(){
        player.release();
        isActivated = false;
    }
}
