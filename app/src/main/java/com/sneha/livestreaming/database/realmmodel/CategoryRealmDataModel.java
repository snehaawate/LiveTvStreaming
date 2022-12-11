package com.sneha.livestreaming.database.realmmodel;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CategoryRealmDataModel extends RealmObject {

    private String name;
    private String image;
    @PrimaryKey
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
}
