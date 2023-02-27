package com.lzq.oa.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class ResponseUtilsTest {

    @Test
    public void put() {
        ResponseUtils responseUtils = new ResponseUtils("LoginException","密码错误").put("class","XXXClass").put("name","imooc");
        String json = responseUtils.toJsonString();
        System.out.println(json);
    }
}