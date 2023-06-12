package com.barreloftea.driversupport.domain.soundcontroller;

import android.content.Context;
import android.media.MediaPlayer;

import com.barreloftea.driversupport.domain.R;
import com.barreloftea.driversupport.domain.processor.common.Constants;

public class SoundController {

    private MediaPlayer player;
    boolean isActivated;

    public void init(Context context){
        this.player = MediaPlayer.create(context, R.raw.sirena);
        isActivated = true;
        player.setVolume(1f, 1f);
    }


    public void play(){
        if(!player.isPlaying()) player.start();
    }

    public void setVolume(float volume){
        player.setVolume(volume, volume);
    }

    public void pause(){
        if (player.isPlaying()) {
            player.pause();
            player.seekTo(0);
        }
    }

    public void destroy(){
        player.release();
        isActivated = false;
    }
}
