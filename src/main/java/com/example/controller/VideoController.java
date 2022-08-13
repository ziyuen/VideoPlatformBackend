package com.example.controller;

import com.example.message.ResponseHandler;
import com.example.message.VideoMetaResponse;
import com.example.model.Video;
import com.example.model.VideoChunk;
import com.example.service.StorageService;
import com.example.service.VideoChunkService;
import com.example.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Controller // This means that this class is a Controller
public class VideoController {
    @Autowired
    private VideoService videoService;
    @Autowired
    private VideoChunkService videoChunkService;
    @Autowired
    private StorageService storageService;

    @GetMapping(path="/videoMetas") // get all public video metas
    public ResponseEntity<List<VideoMetaResponse>> getVideoMetas() {
        return ResponseHandler.generateVideoMetaResponse(videoService.getAllPublicVideoMetas());
    }

    @PostMapping(path = "/videoMetas")
    public ResponseEntity<Object> postVideoMetas(@RequestBody Video video) {
        try {
            if (videoService.isVideoMetaExists(video.getHash())) {
                Map<String, Object> map = new HashMap<>();
                return ResponseHandler.generateResponse("videoMeta already exists in the database",
                        HttpStatus.OK, 0);
            }
            videoService.insertVideoMetaData(video);
            return ResponseHandler.generateResponse("videoMeta has been uploaded",
                    HttpStatus.OK, 1);
        } catch (Exception e) {
            return ResponseHandler.generateResponse("fail to post VideoMeta, Error: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PostMapping(path="/videoChunks")
    public ResponseEntity<Object> PostVideoChunks(@RequestParam("content") MultipartFile content,
                                                  @RequestParam("hash") String hash,
                                                  @RequestParam("fileIndex") Integer fileIndex,
                                                  @RequestParam("fileHash") String fileHash) {
        try {
            var chunk = new VideoChunk(hash, fileIndex, fileHash);
            if (storageService.isFileExists(videoChunkService.getChunkFilePath(chunk))) {
                return ResponseHandler.generateResponse("Your file already exists", HttpStatus.OK, null);
            }
            storageService.saveUploadedFile(videoChunkService.getChunkFilePath(chunk), content);
            videoChunkService.verifyDiskContent(chunk);
            return ResponseHandler.generateResponse("The chunk has been successfully uploaded",
                    HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse("Unable to save the file, Error: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PostMapping(path="/merge")
    public ResponseEntity<Object> Merge(@RequestParam("hash") String hash) {
        try {
            videoService.mergeChunksByHash(hash);
            return ResponseHandler.generateMergeResponse("Chunks are successfully merged", HttpStatus.OK);
        } catch (Exception e) {
            return ResponseHandler.generateMergeResponse("Fail to merge, Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}