package com.gz.lucky.bean;

public class RewardBean {

    private String name;
    private String info;
    private String id;
    private long time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String image) {
        this.info = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public RewardBean(String name, String info, String id, long time) {
        this.name = name;
        this.info = info;
        this.id = id;
        this.time = time;
    }
}
