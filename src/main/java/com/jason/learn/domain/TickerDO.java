package com.jason.learn.domain;

/**
 * Created by jason on 2017/1/10.
 */
public class TickerDO {
    private long id;
    private int currency_id;
    private int price;
    private int lowest_ask;
    private int highest_bid;
    private int percent_change;
    private int base_vol;
    private int quote_vol;
    private int high_24h;
    private int low_24h;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCurrency_id() {
        return currency_id;
    }

    public void setCurrency_id(int currency_id) {
        this.currency_id = currency_id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getLowest_ask() {
        return lowest_ask;
    }

    public void setLowest_ask(int lowest_ask) {
        this.lowest_ask = lowest_ask;
    }

    public int getHighest_bid() {
        return highest_bid;
    }

    public void setHighest_bid(int highest_bid) {
        this.highest_bid = highest_bid;
    }

    public int getPercent_change() {
        return percent_change;
    }

    public void setPercent_change(int percent_change) {
        this.percent_change = percent_change;
    }

    public int getBase_vol() {
        return base_vol;
    }

    public void setBase_vol(int base_vol) {
        this.base_vol = base_vol;
    }

    public int getQuote_vol() {
        return quote_vol;
    }

    public void setQuote_vol(int quote_vol) {
        this.quote_vol = quote_vol;
    }

    public int getHigh_24h() {
        return high_24h;
    }

    public void setHigh_24h(int high_24h) {
        this.high_24h = high_24h;
    }

    public int getLow_24h() {
        return low_24h;
    }

    public void setLow_24h(int low_24h) {
        this.low_24h = low_24h;
    }
}
