package com.zeustel.top9.widgets;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 定时器
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/11/30 17:20
 */
public class ScheduledProvider {
    //周期任务线程池
    private ScheduledExecutorService scheduledThreadPool = null;
    private ScheduledFuture future = null;

    public ScheduledProvider() {
        scheduledThreadPool = Executors.newScheduledThreadPool(1);
    }

    /**
     * 注册任务
     * @param runnable 任务
     * @param period 间隔时间
     */
    public void registerProvider(Runnable runnable, long period) {
        final ScheduledFuture future = scheduledThreadPool.scheduleAtFixedRate(runnable, 0, period, TimeUnit.SECONDS);
        if (this.future != null && !this.future.equals(future)) {
            unRegisterProvider();
        }
        this.future = future;
    }

    /**
     * 取消任务
     */
    public void unRegisterProvider() {
        if (!future.isCancelled()) {
            future.cancel(true);
        }
    }

    /**
     * 销毁对象
     */
    public void onDestroy() {
        if (!future.isCancelled()) {
            future.cancel(true);
        }
        scheduledThreadPool.shutdown();
        scheduledThreadPool = null;
    }
}
