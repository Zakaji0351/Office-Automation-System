package com.lzq.oa.service;

import com.lzq.oa.entity.Department;
import com.lzq.oa.mapper.DepartmentMapper;
import com.lzq.oa.utils.MyBatisUtils;

public class DepartmentService {
    public Department selectById(Long departmentId) {
        return (Department) MyBatisUtils.executeQuery(sqlSession -> sqlSession.getMapper(DepartmentMapper.class)
                .selectById(departmentId));

    }
}
