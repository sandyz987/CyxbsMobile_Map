package com.mredrock.cyxbs.discover.map.util;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池工具类
 */
public class ThreadPool {
    private static ThreadPool threadPool;
    private ThreadPoolExecutor threadPoolExecutor;


    /**
     * 私有化构造方法
     */
    private ThreadPool() {
        threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(50);
    }

    public static ThreadPool getInstance() {
        if (threadPool == null) {
            synchronized (ThreadPool.class) {
                if (threadPool == null) {
                    threadPool = new ThreadPool();
                }
            }
        }
        return threadPool;
    }

    /**
     * 方式1，直接运行runnable
     *
     * @param task
     */
    public void execute(Runnable task) {
        threadPoolExecutor.execute(task);
    }

    /**
     * 方式2，运行
     *
     * @param callable
     * @param <T>
     * @return
     */
    public <T> Future<T> submit(Callable<T> callable) {
        return threadPoolExecutor.submit(callable);
    }

}
