package com.pablojimenezmateo.rainyrainsounds;

import androidx.appcompat.app.AppCompatActivity;

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

    static final int NUMBER_OF_THUNDERS = 11;
    SoundPool soundPool;

    // Store the sound IDs
    HashMap<String, Integer> soundIds;

    // Store what is currently playing
    HashMap<String, Integer> streamIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hide app namebar
        Objects.requireNonNull(getSupportActionBar()).hide();

        // Get the sound pool
        SoundPoolSingleton soundPoolSingleton = SoundPoolSingleton.getInstance();
        soundPool = soundPoolSingleton.soundPool;

        RandomThunderRunnerSingleton thunderRunner = initializeThunderRunner();
        initializeListeners(thunderRunner);

        // Check if the activity is being created for the first time
        if (savedInstanceState == null) {

            // Initialize the maps so they are filled by the function
            soundIds = new HashMap<>();
            streamIds = new HashMap<>();

            initializeSounds(soundPool, soundIds, streamIds);

        } else {
            // Restore the sound IDs
            soundIds = (HashMap<String, Integer>) savedInstanceState.getSerializable("soundIds");

            // Restore what is currently playing
            streamIds = (HashMap<String, Integer>) savedInstanceState.getSerializable("streamIds");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outstate) {
        super.onSaveInstanceState(outstate);

        // Save the sound IDs
        outstate.putSerializable("soundIds", soundIds);

        // Save what is currently playing
        outstate.putSerializable("streamIds", streamIds);
    }

    public RandomThunderRunnerSingleton initializeThunderRunner() {
        Log.d("RainyRain", "Initializing thunder runner");
        Random rand = new Random();

        // Prepare the random thunder runner
        RandomThunderRunnerSingleton runner = RandomThunderRunnerSingleton.getInstance(() -> {
            String thunder_name = "thunder" + rand.nextInt(NUMBER_OF_THUNDERS);

            // If it is still playing, delete its resources and replay it
            stopSound(thunder_name);
            playSound(thunder_name, 0);
        });

        return runner;
    }

    public void initializeListeners(RandomThunderRunnerSingleton thunderRunner) {
        Log.d("RainyRain", "Initializing listeners");

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
                    thunderRunner.start();
                } else {
                    thunderRunner.stop();
                }
            }
        });
    }

    public void initializeSounds(SoundPool soundPool, Map<String, Integer> soundIds, Map<String, Integer> streamIds) {
        Log.d("RainyRain", "Initializing sounds");

        // Load the sounds
        int soundIdAux = soundPool.load(this, R.raw.calpo_rain, 1);
        soundIds.put("rain", soundIdAux);

        soundIdAux = soundPool.load(this, R.raw.church_bell, 1);
        soundIds.put("church_bell", soundIdAux);

        // Load the thunders
        for (int i = 0; i < NUMBER_OF_THUNDERS; i++) {
            int resId = getResources().getIdentifier("thunder" + i, "raw", "com.pablojimenezmateo.rainyrainsounds");
            soundIdAux = soundPool.load(this, resId, 1);
            soundIds.put("thunder" + i, soundIdAux);
        }
    }

    public void playSound(String sound, int loop) {

        if (streamIds.containsKey(sound)) {
            Log.d("RainyRainSounds", "Already playing " + sound);
            return;
        }

        Log.d("RainyRainSounds", "Playing " + sound);

        int soundId = soundIds.get(sound);
        int streamId = soundPool.play(soundId, 1, 1, 0, loop, 1);

        streamIds.put(sound, streamId);
    }

    public void stopSound(String sound) {
        if (streamIds.containsKey(sound)) {
            Log.d("RainyRainSounds", "Stopping " + sound);

            // The sound has already been played, pause
            int streamId = streamIds.get(sound);
            soundPool.stop(streamId);
            streamIds.remove(sound);
        }
    }
}