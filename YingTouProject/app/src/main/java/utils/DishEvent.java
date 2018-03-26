package utils;

/**
 * Created by Administrator on 2018/2/1 0001.
 */

public class DishEvent {
    private String buy;//买价
    private String riseOrFall;//涨跌额
    private String open;//开盘
    private String highest;//最高
    private String volume;//成交量
    private String holdings;//持仓量
    private String sell;//卖价
    private String applies;//涨跌幅
    private String yesterday;//昨结
    private String lowest;//最低
    private String amplitude;//振幅




    public String getBuy() {
        return buy;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public String getRiseOrFall() {
        return riseOrFall;
    }

    public void setRiseOrFall(String riseOrFall) {
        this.riseOrFall = riseOrFall;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getHighest() {
        return highest;
    }

    public void setHighest(String highest) {
        this.highest = highest;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getHoldings() {
        return holdings;
    }

    public void setHoldings(String holdings) {
        this.holdings = holdings;
    }

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    public String getApplies() {
        return applies;
    }

    public void setApplies(String applies) {
        this.applies = applies;
    }

    public String getYesterday() {
        return yesterday;
    }

    public void setYesterday(String yesterday) {
        this.yesterday = yesterday;
    }

    public String getLowest() {
        return lowest;
    }

    public void setLowest(String lowest) {
        this.lowest = lowest;
    }

    public String getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(String amplitude) {
        this.amplitude = amplitude;
    }

    public DishEvent(String buy, String riseOrFall, String open, String highest, String volume, String holdings, String sell, String applies,
                     String yesterday, String lowest, String amplitude) {
        this.buy = buy;
        this.riseOrFall = riseOrFall;
        this.open = open;
        this.highest = highest;
        this.volume = volume;
        this.holdings = holdings;
        this.sell = sell;
        this.applies = applies;
        this.yesterday = yesterday;
        this.lowest = lowest;
        this.amplitude = amplitude;
    }

    public DishEvent() {
    }
}
