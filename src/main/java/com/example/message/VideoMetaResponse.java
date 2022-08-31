package com.example.message;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VideoMetaResponse {
    private final String title;
    private final String description;
    private final String url;
    private final String avatarImg;
}
