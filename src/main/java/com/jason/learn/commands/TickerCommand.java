package com.jason.learn.commands;

import com.google.common.collect.Maps;
import com.jason.learn.constants.PoloniexConstant;
import com.jason.learn.enums.PoloCommandEnum;
import com.jason.learn.util.ApacheHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.Map;

/**
 * Created by jason on 2017/1/9.
 */
@Service
public class TickerCommand implements GetCommand {
    private Logger logger = LoggerFactory.getLogger(TickerCommand.class);

    public String getHttpResult() {
        StringBuffer sb = new StringBuffer();
        try {
            sb.append(ApacheHttpClient.httpGetRequest(PoloniexConstant.POLO_HOST, true, getParam()));
        } catch (URISyntaxException e) {
            logger.error("getHttpResult(), result exception, param e={}", e);
        }
        return sb.toString();
    }

    private Map<String, Object> getParam() {
        Map<String, Object> params = Maps.newHashMap();
        params.put("command", PoloCommandEnum.TICKER.getValue());

        return params;
    }

}
