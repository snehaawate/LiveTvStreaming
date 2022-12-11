package com.sneha.livestreaming.database.realmmodel;

import java.io.Serializable;

public class InnerChannelModel implements Serializable {

    private String cat_id;
    private String channels_name;
    private String channels_image;
    private String channels_url;
    private String channel_type;
    private String channel_description;
    private String created_at;
    private String channels_id;
    private String category_name;

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getChannels_id() {
        return channels_id;
    }

    public void setChannels_id(String channels_id) {
        this.channels_id = channels_id;
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


    @Override
    public String toString() {
        return "InnerChannelModel{" +
                "cat_id='" + cat_id + '\'' +
                ", channels_name='" + channels_name + '\'' +
                ", channels_image='" + channels_image + '\'' +
                ", channels_url='" + channels_url + '\'' +
                ", channel_type='" + channel_type + '\'' +
                ", channel_description='" + channel_description + '\'' +
                ", created_at='" + created_at + '\'' +
                '}';
    }
}
