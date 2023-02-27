package com.lzq.oa.mapper;

import com.lzq.oa.entity.User;
import com.lzq.oa.utils.MyBatisUtils;

public class UserMapper {
    public User selectByUsername(String username){
        User user = (User) MyBatisUtils.executeQuery(sqlSession -> sqlSession.selectOne("usermapper.selectByUsername",username));
        return user;
    }
}
