package com.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.service.StorageService;
import org.junit.jupiter.api.Test;

import javax.xml.bind.DatatypeConverter;


class StorageServiceTests {
    @Test
    void isFolderExistsTest() {
        StorageService storage = new StorageService();
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        assertEquals(true, storage.isFolderExists("src/test"));
    }

    @Test
    void isFileExistsTest() {
        StorageService storage = new StorageService();
        assertEquals(true,
                storage.isFileExists("src/test/resources/test_md5.txt"));
    }

    @Test
    void createFolderIfNotExistsTest() {
        StorageService storage = new StorageService();
        assertEquals(false, storage.isFolderExists("src/test/resources/temp"));
        storage.createFolderIfNotExists("src/test/resources/temp");
        assertEquals(true, storage.isFolderExists("src/test/resources/temp"));
        storage.deleteFileIfExists("src/test/resources/temp");
        assertEquals(false, storage.isFolderExists("src/test/resources/temp"));
    }

    @Test
    void saveFileTest() {
        try {
            StorageService storage = new StorageService();
            byte[] bytes = "Testing Storage Manager...".getBytes();
            storage.saveFile("src/test/resources/test_save_file.txt", bytes);
            assertEquals(true, storage.isFileExists("./src/test/resources/test_save_file.txt"));
            storage.deleteFileIfExists("src/test/resources/test_save_file.txt");
            assertEquals(false, storage.isFileExists("src/test/resources/test_save_file.txt"));
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    @Test
    void computeFileMD5Test() {
        StorageService storage = new StorageService();
        String filenName = "src/test/resources/test_md5.txt";
        String checkSum = "5EB63BBBE01EEED093CB22BB8F5ACDC3";

        String myChecksum = storage.computeFileMD5(filenName);
        assertEquals(checkSum, myChecksum);
    }

    @Test
    void mergeFilesInParentDirectoryTest() {
        try {
            // create the merged file
            StorageService storage = new StorageService();
            String fileName = "src/test/resources/test_merge/merge_final.txt";
            storage.mergeFilesInParentDirectory(fileName);

            // compare checkSum to see if merge success
            String checkSum = "226DFCC156F01ACDBFDAF708D561CE3E";
            String myChecksum = storage.computeFileMD5(fileName);
            assertEquals(checkSum, myChecksum);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
