package com.pablojimenezmateo.rainyrainsounds;

import android.os.Handler;
import java.util.Random;

public class RandomThunderRunner {

    public interface RandomTask {
        void execute();
    }

    final private Handler handler = new Handler();
    final private Random random = new Random();
    final private RandomTask task;

    public RandomThunderRunner(RandomTask task) {
        this.task = task;
    }

    final private Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            if (task != null) {
                task.execute();
            }

            // Between 10 and 100 seconds
            //int randomInterval = random.nextInt(100 * 1000) + 10 * 1000;
            int randomInterval = random.nextInt(10 * 1000) + 1 * 1000;
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