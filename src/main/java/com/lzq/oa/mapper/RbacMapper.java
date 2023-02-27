package com.lzq.oa.mapper;

import com.lzq.oa.entity.Node;
import com.lzq.oa.utils.MyBatisUtils;

import java.util.List;

public class RbacMapper {
    public List<Node> selectNodeByUserId(Long userId){
        List list = (List) MyBatisUtils.executeQuery(sqlSession -> sqlSession.selectList("rbacmapper.selectNodeByUserId",userId));
        return list;
    }
}
