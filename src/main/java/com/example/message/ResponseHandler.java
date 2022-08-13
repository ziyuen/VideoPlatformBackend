package com.example.message;

import com.example.model.Video;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseHandler {
    public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object responseObj) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message", message);
        map.put("status", status.value());
        if (responseObj instanceof Integer) {
            map.put("continue", (Integer) responseObj);
        } else {
            map.put("data", responseObj);
        }
        return new ResponseEntity<Object>(map,status);
    }

    public static ResponseEntity<List<VideoMetaResponse>> generateVideoMetaResponse(List<Video> videoList) {
        var videoMetaResponses = new ArrayList<VideoMetaResponse>();
        for (var v : videoList) {
            var videoMetaResponse = new VideoMetaResponse(v.getTitle(), v.getDescription(), v.getVideoUrl(),
                    v.getAvatarImg());
            videoMetaResponses.add(videoMetaResponse);
        }
        return new ResponseEntity<List<VideoMetaResponse>>(videoMetaResponses, HttpStatus.OK);
    }

    public static ResponseEntity<Object> generateMergeResponse(String message, HttpStatus status) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message", message);
        if (status.equals(HttpStatus.OK)) {
            map.put("success", true);
        } else {
            map.put("success", false);
        }
        return new ResponseEntity<>(map, status);
    }
}