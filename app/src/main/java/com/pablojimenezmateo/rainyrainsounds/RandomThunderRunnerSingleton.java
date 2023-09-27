package com.pablojimenezmateo.rainyrainsounds;

import android.os.Handler;
import java.util.Random;

public class RandomThunderRunnerSingleton {

    public interface RandomTask {
        void execute();
    }

    private static RandomThunderRunnerSingleton instance;

    final private Handler handler = new Handler();
    final private Random random = new Random();
    final private RandomTask task;

    private RandomThunderRunnerSingleton(RandomTask task) {
        this.task = task;
    }

    public static RandomThunderRunnerSingleton getInstance(RandomTask task) {
        if (instance == null) {
            instance = new RandomThunderRunnerSingleton(task);
        }
        return instance;
    }

    final private Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            if (task != null) {
                task.execute();
            }

            // Between 5 and 20 seconds
            int randomInterval = random.nextInt(20 * 1000) + 5 * 1000;
            handler.postDelayed(this, randomInterval);
        }
    };

    public void start() {
        handler.post(myRunnable);
    }

    public void stop() {
        handler.removeCallbacks(myRunnable);
    }
}
