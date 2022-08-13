package com.example.exception;

public class FileChunkNotMatchException extends Exception{
    public FileChunkNotMatchException(String str) {
        super(str);
    }
}
