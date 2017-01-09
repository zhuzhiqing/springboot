package com.jason.learn.job;

import com.google.common.base.Stopwatch;
import com.jason.learn.commands.TickerCommand;
import com.jason.learn.util.ApacheHttpClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by jason on 2016/12/6.
 */

@Component
public class HttpTask {
    @Resource
    TickerCommand tickerCommand;

    @Scheduled(fixedRate = 10 * 1000)
    public void task() throws InterruptedException {
        tickerCommand.getHttpResult();
    }

    @Async
    private void getOrderBook() {
        System.out.println("##########getOrderBook#########");
        Stopwatch stopwatch = Stopwatch.createStarted();
        String url = "https://poloniex.com/public?command=returnOrderBook&currencyPair=all&depth=10";
        System.out.println(ApacheHttpClient.httpGetRequest(url, true));
    }

    private void getCurrencies() {
        System.out.println("##########getCurrencies#########");
        Stopwatch stopwatch = Stopwatch.createStarted();
        String url = "https://poloniex.com/public?command=returnCurrencies";
        System.out.println(ApacheHttpClient.httpGetRequest(url, true));

        stopwatch.stop();
    }
}
