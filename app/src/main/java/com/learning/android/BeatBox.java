package com.learning.android;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.widget.SeekBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BeatBox {
    private static final String TAG = "BeatBox";
    private static final String SOUNDS_FOLDER = "sample_sounds";
    private static final int MAX_SOUNDS = 5;

    private AssetManager assetManager;
    private List<Sound> sounds = new ArrayList<>();
    private SoundPool soundPool;
    private static float soundRate;

    public BeatBox(Context context){
        assetManager = context.getAssets();
        soundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        loadSounds();
    }

    private void loadSounds(){
        String[] soundNames = new String[0];
        try {
            soundNames = assetManager.list(SOUNDS_FOLDER);
            Log.i(TAG, "Found " + soundNames.length +" sonds");
        } catch (IOException ioe){
            Log.e(TAG, "could not list assets", ioe);
        }

        for (String filename : soundNames){
            try {
                String assetPath = SOUNDS_FOLDER + "/" + filename;
                Sound sound = new Sound(assetPath);
                load(sound);
                sounds.add(sound);
            } catch (IOException ioe){
                Log.e(TAG, "Could not load sound " +filename, ioe);
            }
        }
    }

    private void load(Sound sound) throws IOException {
        AssetFileDescriptor afd = assetManager.openFd(sound.getAssetPath());
        int soundId = soundPool.load(afd, 1);
        sound.setSoundId(soundId);
    }

    public void play(Sound sound){
        Integer soundId = sound.getSoundId();
        if (soundId == null){
            return;
        }
        soundPool.play(soundId, 1.0f, 1.0f, 1, 0, soundRate);
    }

    public static  void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
        if (fromUser){
            soundRate = 1.0f + ((float)(progress - 5) / 10);
            Log.d("BeatBox", "Got progress change of: " + progress + ", changed rate to: " + soundRate);
        }else{
            Log.d("BeatBox", "Got progress change of: " + progress + " but not from user");
        }
    }

    public void release(){
        soundPool.release();
    }

    public List<Sound> getSounds(){
        return sounds;
    }
}
