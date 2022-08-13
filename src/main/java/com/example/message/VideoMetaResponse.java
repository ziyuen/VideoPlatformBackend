package com.example.message;

import lombok.Data;

@Data
public class VideoMetaResponse {
    private String title;
    private String description;
    private String url;
    private String avatarImg;
    VideoMetaResponse(String title, String desc, String url, String img) {
        this.title = title;
        this.description = desc;
        this.url = url;
        this.avatarImg = img;
    }
}
