package com.pablojimenezmateo.rainyrainsounds;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    SoundPool soundPool;

    // Store the sound IDs
    Map<String, Integer> soundIds = new HashMap<>();

    // Store what is currently playing
    Map<String, Integer> streamIds = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int concurrent_sounds = 20, number_of_thunders = 11;
        int soundIdAux;
        Random rand = new Random();

        // Hide app namebar
        Objects.requireNonNull(getSupportActionBar()).hide();

        AudioAttributes
                audioAttributes
                = new AudioAttributes
                .Builder()
                .setUsage(
                        AudioAttributes
                                .USAGE_MEDIA)
                .setContentType(
                        AudioAttributes
                                .CONTENT_TYPE_MUSIC)
                .build();

        soundPool = new SoundPool
                .Builder()
                .setMaxStreams(concurrent_sounds)
                .setAudioAttributes(
                        audioAttributes)
                .build();

        // Load the sounds
        soundIdAux = soundPool.load(this, R.raw.calpo_rain, 1);
        soundIds.put("rain", soundIdAux);

        soundIdAux = soundPool.load(this, R.raw.church_bell, 1);
        soundIds.put("church_bell", soundIdAux);

        // Load the thunders
        for (int i=0; i<number_of_thunders; i++) {
            int resId = getResources().getIdentifier("thunder" + i, "raw","com.pablojimenezmateo.rainyrainsounds");
            soundIdAux = soundPool.load(this, resId, 1);
            soundIds.put("thunder"+i, soundIdAux);
        }

        // Prepare the random thunder runner
        RandomThunderRunner runner = new RandomThunderRunner(() -> {
            String thunder_name = "thunder" + rand.nextInt(number_of_thunders);

            // If it is still playing, delete its resources and replay it
            stopSound(thunder_name);
            playSound(thunder_name, 0);
        });

        Switch rainSwitch = (Switch) findViewById(R.id.rainSwitch);
        rainSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    playSound("rain", -1);
                } else {
                    stopSound("rain");
                }
            }
        });

        Switch thunderSwitch = (Switch) findViewById(R.id.thunderSwitch);
        thunderSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    runner.start();
                } else {
                    runner.stop();
                }
            }
        });
    }

    public void playSound(String sound, int loop) {
        Log.d("RainyRainSounds", "Playing " + sound);

        // We have never played this sound, save the stream
        int soundId = soundIds.get(sound);
        int streamId = soundPool.play(soundId, 1, 1, 0, loop, 1);

        streamIds.put(sound, streamId);
    }

    public void stopSound(String sound) {
        if (streamIds.containsKey(sound)) {
            // The sound has already been played, pause
            int streamId = streamIds.get(sound);
            soundPool.stop(streamId);
            streamIds.remove(sound);
        }
    }
}