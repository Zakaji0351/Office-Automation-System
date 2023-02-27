package com.lzq.oa.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.Map;

public class ResponseUtils {
    private String code;
    private String message;
    private Map data = new LinkedHashMap<>();
    public ResponseUtils(){
        this.setCode("0");
        this.setMessage("success");
    }
    public ResponseUtils(String code,String message){
        this.setCode(code);
        this.setMessage(message);
    }
    public ResponseUtils put(String key,Object value){
        this.data.put(key,value);
        return this;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map getdata() {
        return data;
    }

    public void setData(Map data) {
        data = data;
    }
    public String toJsonString(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String json = null;
        try{
            json = objectMapper.writeValueAsString(this);
            return json;
        }catch(JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }
}
