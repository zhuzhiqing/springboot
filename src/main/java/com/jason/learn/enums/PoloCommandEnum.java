package com.jason.learn.enums;

/**
 * Created by jason on 2017/1/9.
 */
public enum PoloCommandEnum {
    TICKER("returnTicker"),
    VOL("return24Volume"),
    ORDERBOOK("returnOrderBook"),
    TRADEHISTORY("returnTradeHistory"),
    CHARTDATA("returnChartData"),
    CURRENCY("returnCurrencies"),
    LOANORDER("returnLoanOrders");

    private String value;

    PoloCommandEnum(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
