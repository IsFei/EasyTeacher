package com.magicare.mutils.task;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by justin on 15/6/5.
 * 支持优先级的线程池管理类
 */
public class PriorityExecutor implements Executor {

    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 9;
    private static final int MAXIMUM_QUEUE_SIZE = 128;
    private static final int KEEP_ALIVE = 1;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "PriorityExecutor #" + mCount.getAndIncrement());
        }
    };

    private static final Comparator<Runnable> sRunnableComparator = new Comparator<Runnable>() {

        @Override
        public int compare(Runnable lhs, Runnable rhs) {
            if (lhs instanceof PriorityRunnable && rhs instanceof PriorityRunnable) {
                return ((PriorityRunnable) lhs).priority.ordinal() - ((PriorityRunnable) rhs).priority.ordinal();
            } else {
                return 0;
            }
        }
    };

    private final ThreadPoolExecutor mThreadPoolExecutor;

    public PriorityExecutor() {
        this(CORE_POOL_SIZE);
    }

    public PriorityExecutor(int poolSize) {
        BlockingQueue<Runnable> mPoolWorkQueue =
                new PriorityBlockingQueue<Runnable>(MAXIMUM_QUEUE_SIZE, sRunnableComparator);
        /**
         * RejectedExecutionHandler 使用 DiscardOldestPolicy 策略，当申请的线程数超过线程池队列大小时
         * 去掉最先进入队列的任务，默认的策略是直接抛出 rejectedExecution 的异常
         */
        mThreadPoolExecutor = new ThreadPoolExecutor(
                poolSize,
                MAXIMUM_POOL_SIZE,
                KEEP_ALIVE,
                TimeUnit.SECONDS,
                mPoolWorkQueue,
                sThreadFactory,
                new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    public int getPoolSize() {
        return mThreadPoolExecutor.getCorePoolSize();
    }

    public void setPoolSize(int poolSize) {
        if (poolSize > 0) {
            mThreadPoolExecutor.setCorePoolSize(poolSize);
        }
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return mThreadPoolExecutor;
    }

    public boolean isBusy() {
        return mThreadPoolExecutor.getActiveCount() >= mThreadPoolExecutor.getCorePoolSize();
    }

    @Override
    public void execute(final Runnable runnable) {
        mThreadPoolExecutor.execute(runnable);
    }
}
