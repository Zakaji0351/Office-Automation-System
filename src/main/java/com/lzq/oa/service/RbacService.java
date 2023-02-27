package com.lzq.oa.service;

import com.lzq.oa.entity.Node;
import com.lzq.oa.mapper.RbacMapper;

import java.util.List;

public class RbacService {
    private RbacMapper rbacMapper = new RbacMapper();
    public List<Node> selectNodeByUserId(Long userId){
        return rbacMapper.selectNodeByUserId(userId);
    }
}
