package com.example.service;

import com.example.exception.MergeFailureException;
import com.example.model.Video;
import com.example.model.VideoChunk;
import com.example.model.VideoStateType;
import com.example.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class VideoService {
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private StorageService storageService;
    @Autowired
    private VideoChunkService videoChunkService;

    public boolean isVideoMetaExists(String hash) {
        Optional<Video> videoOptional = videoRepository.findVideoByHash(hash);
        if (videoOptional.isPresent()) {
            return true;
        }
        return false;
    }

    public void insertVideoMetaData(Video video) throws IOException{
        storageService.createFolderIfNotExists(getStorageDirectory(video));
        video.setHash(video.getHash().toUpperCase());
        video.setFileName(video.getFileName().replaceAll("\\s+",""));
        video.setVideoUrl("http://localhost:8080/" + getStorageFilePath(video));
        video.setState(VideoStateType.INCOMPLETE);

        // save img to local disk and save uri to DB
        String storagePath = getStorageDirectory(video) + "/images/avatarImg";
        String imagePath = storageService.saveUploadedImage(storagePath, video.getAvatarImg());
        video.setAvatarImg("http://localhost:8080/" + imagePath);
        System.out.println(imagePath);

        videoRepository.save(video);
    }

    public List<Video> getAllPublicVideoMetas() {
        return videoRepository.findByState(VideoStateType.MERGED);
    }

    public String getStorageDirectory(Video video) {
        var builder = new StringBuilder("storage/");
        builder.append(video.getHash());
        return builder.toString();
    }

    public String getStorageFilePath(Video video) {
        var builder = new StringBuilder("storage/");
        builder.append(video.getHash());
        builder.append("/");
        builder.append(video.getFileName());
        return builder.toString();
    }

    @Transactional
    public void mergeChunksByHash(String hash) throws MergeFailureException, IOException {
        Optional<Video> videoOptional = videoRepository.findVideoByHashForUpdate(hash);
        if (!videoOptional.isPresent()) {
            throw new MergeFailureException("Cannot find the video by hash");
        }
        Video video = videoOptional.get();
        if (!isAllChunkReceived(video)) {
            throw new MergeFailureException("chunk is incomplete!");
        }
        String filePath = getStorageFilePath(video);
        storageService.mergeFilesInParentDirectory(filePath);
        // After all chunks has been merged
        // 1. check the merged file's md5 must match our record
        if (!storageService.computeFileMD5(filePath).equals(video.getHash())) {
            // delete all chunks and the merged file, redo upload
            deleteAllChunks(video);
            video.setState(VideoStateType.INCOMPLETE);
            throw new MergeFailureException("chunk's md5 does not match!");
        }
        // 2. set video's state and clean up chunks
        video.setState(VideoStateType.MERGED);
        deleteAllChunks(video);
        videoRepository.save(video);
    }
    public boolean isAllChunkReceived(Video video) {
        if (video.getState() != VideoStateType.INCOMPLETE) {
            return true;
        }
        for (VideoChunk chunk : video.getChunks()) {
            if (!storageService.isFileExists(videoChunkService.getChunkFilePath(chunk))) {
                return false;
            }
            // All chunks have been received
            video.setState(VideoStateType.COMPLETE);
        }
        return true;
    }

    private void deleteAllChunks(Video video) {
        for (VideoChunk chunk : video.getChunks()) {
            storageService.deleteFileIfExists(videoChunkService.getChunkFilePath(chunk));
        }
    }
}
