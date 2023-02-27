package com.lzq.oa.service;

import com.lzq.oa.entity.User;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserServiceTest {
    private UserService userService = new UserService();
    @Test
    public void checkLogin() {
        User user = userService.checkLogin("m8","test1");
        System.out.println(user);
    }

}