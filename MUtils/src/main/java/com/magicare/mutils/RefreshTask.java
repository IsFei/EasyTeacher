package com.magicare.mutils;

import android.os.Handler;

/**
 * @author justin on 2016/01/09 16:25
 *         justin@magicare.me
 * @version V1.0
 */
public abstract class RefreshTask extends Handler implements Runnable {
    final long REFRESH_GAP_TIME;
    final long MIN_REFRESH_TIME;
    private long last = 0;

    final Runnable callBack = new Runnable() {
        @Override
        public void run() {
            RefreshTask.this.run();
            if (MIN_REFRESH_TIME == -1) {
                repeat();
            }
        }
    };

    public RefreshTask(long refresh) {
        this(refresh, -1);
    }

    public RefreshTask(long refresh, long min) {
        this.REFRESH_GAP_TIME = refresh;
        this.MIN_REFRESH_TIME = min;
    }

    public void done() {
        last = System.currentTimeMillis();
        if (MIN_REFRESH_TIME != -1) {
            repeat();
        }
    }

    public void start(long delay) {
        stop();
        if (MIN_REFRESH_TIME == -1) {
            postDelayed(callBack, delay);
            return;
        }
        long gap = System.currentTimeMillis() + delay - last;
        if (gap > MIN_REFRESH_TIME) {
            postDelayed(callBack, delay);
        } else {
            postDelayed(callBack, MIN_REFRESH_TIME - gap + delay);
        }
    }

    public void start() {
        start(1);
    }

    public void repeat() {
        stop();
        postDelayed(callBack, REFRESH_GAP_TIME);
    }

    public void stop() {
        removeCallbacks(callBack);
    }

}