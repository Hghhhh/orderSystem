package com.yidong.model;

public class SmallType {
    private String id;

    private String name;

    private String bigTypeId;

    private String img;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getBigTypeId() {
        return bigTypeId;
    }

    public void setBigTypeId(String bigTypeId) {
        this.bigTypeId = bigTypeId == null ? null : bigTypeId.trim();
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}