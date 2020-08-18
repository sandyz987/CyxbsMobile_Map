package com.mredrock.cyxbs.discover.map.util

import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.ThreadPoolExecutor

/**
 * 线程池工具类
 */
class ThreadPool private constructor() {
    private val threadPoolExecutor: ThreadPoolExecutor = Executors.newFixedThreadPool(50) as ThreadPoolExecutor

    /**
     * 方式1，直接运行runnable
     *
     * @param task
     */
    fun execute(task: Runnable?) {
        threadPoolExecutor.execute(task)
    }

    /**
     * 方式2，运行
     *
     * @param callable
     * @param <T>
     * @return
    </T> */
    fun <T> submit(callable: Callable<T>): Future<T> {
        return threadPoolExecutor.submit(callable)
    }

    companion object {
        private var threadPool: ThreadPool? = null
        val instance: ThreadPool?
            get() {
                if (threadPool == null) {
                    synchronized(ThreadPool::class.java) {
                        if (threadPool == null) {
                            threadPool = ThreadPool()
                        }
                    }
                }
                return threadPool
            }
    }

}