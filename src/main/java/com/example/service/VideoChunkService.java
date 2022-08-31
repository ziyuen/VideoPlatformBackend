package com.example.service;

import com.example.exception.FileChunkNotMatchException;
import com.example.model.VideoChunk;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
public class VideoChunkService {
    private final StorageService storageService;

    // verifyDiskContent computes MD5 hash of the content in storage, compare it with the record
    // It deletes the file if the hashes are not correct and return FileChunkNotMatch error
    public void verifyDiskContent(VideoChunk chunk) throws IOException, FileChunkNotMatchException {
        String md5 = storageService.computeFileMD5(getChunkFilePath(chunk));
        // If hash does not match, delete file and throws exception
        if (!md5.equals(chunk.getHash())) {
            storageService.deleteFileIfExists(getChunkFilePath(chunk));
            throw new FileChunkNotMatchException("chunk hash does not match!");
        }
    }

    public String getChunkFilePath(VideoChunk chunk) {
        var builder = new StringBuilder("storage/");
        builder.append(chunk.getFileHash());
        builder.append("/");
        builder.append(chunk.getFileIndex());
        return builder.toString();
    }
}
