package com.jason.learn.task;

import com.jason.learn.util.ApacheHttpClient;
import org.apache.http.pool.PoolStats;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by jason on 2016/12/7.
 */

@Component
public class MonitorTask {
    @Scheduled(fixedRate = 3000)
    public void task() {
        PoolStats poolStats = ApacheHttpClient.getPoolStatus();
        System.out.println(poolStats.toString());
    }

}
