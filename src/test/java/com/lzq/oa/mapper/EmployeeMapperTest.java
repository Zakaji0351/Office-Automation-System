package com.lzq.oa.mapper;

import com.lzq.oa.entity.Employee;
import com.lzq.oa.utils.MyBatisUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class EmployeeMapperTest {

    @Test
    public void selectById() {
        Employee emp = (Employee) MyBatisUtils.executeQuery(sqlSession -> {
            EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
            Employee employee = employeeMapper.selectById(7l);
            System.out.println(employee);
            return employee;
        });
    }

    @Test
    public void selectByParams() {
        Map params = new HashMap<>();
        params.put("level",7);
        params.put("departmentId",2);
        MyBatisUtils.executeQuery(sqlSession -> {
            EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
            List<Employee> employees = employeeMapper.selectByParams(params);
            System.out.println(employees);
            return employees;
        });
    }
    @Test
    public void selectByParams1() {
        Map params = new HashMap<>();
        params.put("level",8);
        MyBatisUtils.executeQuery(sqlSession -> {
            EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
            List<Employee> employees = employeeMapper.selectByParams(params);
            System.out.println(employees);
            return employees;
        });

    }
}