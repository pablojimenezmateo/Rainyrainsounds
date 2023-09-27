package com.pablojimenezmateo.rainyrainsounds;

import android.os.Handler;
import android.util.Log;

import java.util.Calendar;
import java.util.Random;

public class ChurchBellRunnerSingleton {

    public interface RandomTask {
        void execute();
    }

    private static ChurchBellRunnerSingleton instance;

    final private Handler handler = new Handler();
    final private RandomTask task;

    private ChurchBellRunnerSingleton(RandomTask task) {
        this.task = task;
    }

    public static ChurchBellRunnerSingleton getInstance(RandomTask task) {
        if (instance == null) {
            instance = new ChurchBellRunnerSingleton(task);
        }
        return instance;
    }

    final private Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            if (task != null) {
                task.execute();
            }

            Log.d("RainyRain", "Posponing bell for " + getTimeUntilNextMinute());

            // Delay the task until the next full hour
            handler.postDelayed(this, getTimeUntilNextMinute());
        }
    };

    private long getTimeUntilNextHour() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, 1);  // Go to the next hour
        calendar.set(Calendar.MINUTE, 0);  // Set minute to 0
        calendar.set(Calendar.SECOND, 0);  // Set second to 0
        calendar.set(Calendar.MILLISECOND, 0);  // Set millisecond to 0
        long timeNow = System.currentTimeMillis();
        return calendar.getTimeInMillis() - timeNow;  // Get the difference in milliseconds
    }

    private long getTimeUntilNextMinute() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 1);  // Go to the next minute
        calendar.set(Calendar.SECOND, 0);  // Set second to 0
        calendar.set(Calendar.MILLISECOND, 0);  // Set millisecond to 0
        long timeNow = System.currentTimeMillis();
        return calendar.getTimeInMillis() - timeNow;  // Get the difference in milliseconds
    }


    public void start() {
        handler.post(myRunnable);
    }

    public void stop() {
        handler.removeCallbacks(myRunnable);
    }
}
