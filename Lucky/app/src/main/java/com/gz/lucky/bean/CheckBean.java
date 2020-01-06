package com.gz.lucky.bean;

public class CheckBean {

    private String name;
    private String image;
    private String id;
    private boolean isReward;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isReward() {
        return isReward;
    }

    public void setReward(boolean reward) {
        isReward = reward;
    }

    public CheckBean(String name, String image, String id, boolean isReward) {
        this.name = name;
        this.image = image;
        this.id = id;
        this.isReward = isReward;
    }
}
