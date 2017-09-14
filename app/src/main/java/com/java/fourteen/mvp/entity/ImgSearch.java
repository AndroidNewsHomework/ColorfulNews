package com.java.fourteen.mvp.entity;

import java.util.List;

/**
 * Created by Lenovo on 2017/9/11.
 */

public class ImgSearch {
    public List<Image> getValue() {
        return value;
    }

    public void setValue(List<Image> value) {
        this.value = value;
    }

    public static class Image {
        public String getContentUrl() {
            return contentUrl;
        }

        public void setContentUrl(String contentUrl) {
            this.contentUrl = contentUrl;
        }

        public String contentUrl;
    }

    private List<Image> value;
}
