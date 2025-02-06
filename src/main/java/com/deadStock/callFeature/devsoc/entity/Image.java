package com.deadStock.callFeature.devsoc.entity;

public class Image {
    public Image() {}

    public Image(String url) {
        this.url = url;
    }
    private String publicId;
    private String url;

    public String getPublicId() { return publicId; }
    public void setPublicId(String publicId) { this.publicId = publicId; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}