package utils;

/**
 * Created by Administrator on 2018/2/28 0028.
 */

public class DealRecordBean {

    private String id;
    private String name;
    private String user_id;
    private String deal_time;
    private String deal_valence;
    private String volume;
    private String sell;
    private String turnover;
    private String status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDeal_time() {
        return deal_time;
    }

    public void setDeal_time(String deal_time) {
        this.deal_time = deal_time;
    }

    public String getDeal_valence() {
        return deal_valence;
    }

    public void setDeal_valence(String deal_valence) {
        this.deal_valence = deal_valence;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    public String getTurnover() {
        return turnover;
    }

    public void setTurnover(String turnover) {
        this.turnover = turnover;
    }

    public DealRecordBean( String name, String deal_time, String deal_valence, String volume, String sell, String status) {
        this.name = name;
        this.deal_time = deal_time;
        this.deal_valence = deal_valence;
        this.volume = volume;
        this.sell = sell;
        this.status = status;
    }
}
