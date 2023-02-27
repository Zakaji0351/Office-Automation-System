package com.lzq.oa.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lzq.oa.entity.User;
import com.lzq.oa.service.UserService;
import com.lzq.oa.utils.ResponseUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/api/login")
public class LoginServlet extends HttpServlet {
    private UserService userService = new UserService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        //接受用户输入
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        //调用业务逻辑
//        Map result = new LinkedHashMap<>();
        ResponseUtils responseUtils = null;
        try {
            User user = userService.checkLogin(username, password);
            user.setPassword(null);
            user.setSalt(null);
            responseUtils = new ResponseUtils().put("user",user);
//            result.put("code","0");
//            result.put("message","success");
//            Map data = new LinkedHashMap<>();
//            data.put("user",user);
//            result.put("data",data);
        }catch(Exception e){
            e.printStackTrace();
//            result.put("code",e.getClass().getSimpleName());
//            result.put("message",e.getMessage());
            responseUtils = new ResponseUtils(e.getClass().getSimpleName(),e.getMessage());
        }
        //返回json结果
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        String json = objectMapper.writeValueAsString(result);
        response.getWriter().println(responseUtils.toJsonString());
    }
}
