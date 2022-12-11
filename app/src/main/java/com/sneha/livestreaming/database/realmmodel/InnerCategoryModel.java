package com.sneha.livestreaming.database.realmmodel;

import java.io.Serializable;

public class InnerCategoryModel implements Serializable {

    private String name;
    private String image;
    private String cat_id;


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

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }


    @Override
    public String toString() {
        return "InnerCategoryModel{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", cat_id='" + cat_id + '\'' +
                '}';
    }
}
