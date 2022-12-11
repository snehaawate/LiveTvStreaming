package com.sneha.livestreaming.model;

import java.io.Serializable;
import java.util.List;

import io.realm.annotations.PrimaryKey;

public class AllChannedlModel implements Serializable {

    private boolean success;
    private int statusCode;
    private List<ChannelsBean> channels;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public List<ChannelsBean> getChannels() {
        return channels;
    }

    public void setChannels(List<ChannelsBean> channels) {
        this.channels = channels;
    }

    public static class ChannelsBean {

        private int id;
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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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
}
