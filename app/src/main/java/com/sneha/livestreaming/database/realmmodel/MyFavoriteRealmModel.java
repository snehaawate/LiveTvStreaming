package com.sneha.livestreaming.database.realmmodel;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MyFavoriteRealmModel  extends RealmObject {


    private String cat_id;
    private String channels_name;
    private String channels_image;
    private String channels_url;
    private String channel_type;
    private String channel_description;
    private String created_at;
    private boolean isFavorite;
    @PrimaryKey
    private String channels_id;

    public String getChannels_id() {
        return channels_id;
    }

    public void setChannels_id(String channels_id) {
        this.channels_id = channels_id;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getChannels_name() {
        return channels_name;
    }

    public void setChannels_name(String channels_name) {
        this.channels_name = channels_name;
    }

    public String getChannels_image() {
        return channels_image;
    }

    public void setChannels_image(String channels_image) {
        this.channels_image = channels_image;
    }

    public String getChannels_url() {
        return channels_url;
    }

    public void setChannels_url(String channels_url) {
        this.channels_url = channels_url;
    }

    public String getChannel_type() {
        return channel_type;
    }

    public void setChannel_type(String channel_type) {
        this.channel_type = channel_type;
    }

    public String getChannel_description() {
        return channel_description;
    }

    public void setChannel_description(String channel_description) {
        this.channel_description = channel_description;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
