package com.soaic.zero.rxjava;


import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class Scheduler {

    protected abstract void schedule(Runnable runnable);

    public static Scheduler io() {
        return new IoScheduler();
    }

    public static Scheduler main() {
        return new MainScheduler();
    }

    private static class IoScheduler extends Scheduler {
        private final ExecutorService executors;
        public IoScheduler() {
            executors = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                    60L, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>());
        }

        @Override
        protected void schedule(Runnable runnable) {
            executors.execute(runnable);
        }
    }

    private static class MainScheduler extends Scheduler {
        private final Handler mHandler;
        public MainScheduler() {
            mHandler = new Handler(Looper.getMainLooper());
        }

        @Override
        protected void schedule(Runnable runnable) {
            mHandler.post(runnable);
        }
    }
}
