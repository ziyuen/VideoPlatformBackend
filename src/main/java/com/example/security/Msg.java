package com.example.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Msg {
    int code;   // error code
    String Message;
    Map<String,Object> data=new HashMap<>();

    public static Msg denyAccess(String message){
        Msg result=new Msg();
        result.setCode(300);
        result.setMessage(message);
        return result;
    }

    public static Msg success(String message){
        Msg result=new Msg();
        result.setCode(200);
        result.setMessage(message);
        return result;
    }

    public static Msg fail(String message){
        Msg result=new Msg();
        result.setCode(400);
        result.setMessage(message);
        return result;
    }

    public Msg add(String key,Object value){
        this.data.put(key,value);
        return this;
    }
}