package com.pablojimenezmateo.rainyrainsounds;

import android.media.AudioAttributes;
import android.media.SoundPool;
import android.util.Log;

public class SoundPoolSingleton {
    static final int concurrent_sounds = 20;
    private static SoundPoolSingleton instance;
    public SoundPool soundPool;

    private SoundPoolSingleton() {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(
                        AudioAttributes.USAGE_MEDIA)
                .setContentType(
                        AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(concurrent_sounds)
                .setAudioAttributes(
                        audioAttributes)
                .build();
    }

    public static SoundPoolSingleton getInstance() {
        if (instance == null) {
            synchronized (SoundPoolSingleton.class) {
                if (instance == null) {
                    instance = new SoundPoolSingleton();
                }
            }
        }
        Log.d("RainyRain", "Returning SoundPool instance");
        return instance;
    }

    public void release() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
            instance = null;
        }
    }
}
