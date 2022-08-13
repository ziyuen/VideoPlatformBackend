package com.example.service;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.Arrays;

@Service
public class StorageService {
    public boolean isFolderExists(String path) {
        if (Files.isDirectory(Paths.get(path))) {
            return true;
        }
        return false;
    }

    public boolean isFileExists(String path) {
        Path p = Paths.get(path);
        if (Files.exists(p)) {
            return true;
        }
        return false;
    }

    public void createFolderIfNotExists(String path) {
        File theDir = new File(path);
        if (!theDir.exists()){
            theDir.mkdirs();  // Will create parent directories if not exists
        }
    }

    public void saveFile(String path, byte[] content) throws Exception {
        if (isFileExists(path)) {
            throw new Exception("File already existed!!!");
        }
        Path p = Paths.get(path);
        // Create whole path automatically when writing to a new file
        Files.createDirectories(p.getParent());
        Files.createFile(p);
        Files.write(p, content, StandardOpenOption.APPEND); 
    }

    public void deleteFileIfExists(String path) {
        if (isFileExists(path)) {
            File f = new File(path);
            f.delete();
        }
    }

    public String computeFileMD5(String path) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(Files.readAllBytes(Paths.get(path)));
            return DatatypeConverter
                    .printHexBinary(md.digest()).toUpperCase();
        } catch (Exception e) {
            System.out.println("No such security algorithm!!!");
            return null;
        }
    }

    public void saveUploadedFile(String path, MultipartFile file) {
        try {
            Path target = Paths.get(path);
            Files.createDirectories(target.getParent());
            Files.copy(file.getInputStream(), target);
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    // convert base4String to normal image file and save to storage
    // return the path of the image
    public String saveUploadedImage(String path, String base64String) throws IOException {
        String[] strings = base64String.split(",");
        String extension;
        switch (strings[0]) { //check image's extension
            case "data:image/jpeg;base64":
                extension = ".jpeg";
                break;
            case "data:image/png;base64":
                extension = ".png";
                break;
            default: //should write cases for more images types
                extension = ".jpg";
                break;
        }
        //convert base64 string to binary data
        byte[] imageByte = DatatypeConverter.parseBase64Binary(strings[1]);
        String fullPath = path + extension;
        File imageFile = new File(fullPath);
        imageFile.getParentFile().mkdirs(); // Will create parent directories if not exists
        imageFile.createNewFile();
        new FileOutputStream(imageFile).write(imageByte);
        return fullPath;
    }

    // given a file path, merge all files in parent directory to it
    public void mergeFilesInParentDirectory(String path) throws IOException {
        // if the file is already existed, it must be deleted to avoid recursion
        deleteFileIfExists(path);
        File destFile = new File(path);
        File[] parentDirFiles = destFile.getParentFile().listFiles();
        destFile.createNewFile();
        // sort by file name lexicographically
        Arrays.sort(parentDirFiles);
        for (int i = 0; i < parentDirFiles.length; i++) {
            if (!parentDirFiles[i].isDirectory()) {  // skip directory
                FileOutputStream destOs = new FileOutputStream(destFile, true);
                FileUtils.copyFile(parentDirFiles[i], destOs);
                destOs.close();
            }
        }
    }
}
