package com.yidong.model;

public class SmallType {
    private int id;

    private String name;

    private int bigTypeId;

    private String img;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id ;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }


    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getBigTypeId() {
        return bigTypeId;
    }

    public void setBigTypeId(int bigTypeId) {
        this.bigTypeId = bigTypeId;
    }
}