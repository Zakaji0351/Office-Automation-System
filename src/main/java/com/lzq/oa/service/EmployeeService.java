package com.lzq.oa.service;

import com.lzq.oa.entity.Employee;
import com.lzq.oa.mapper.EmployeeMapper;
import com.lzq.oa.utils.MyBatisUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeService {
    public Employee selectById(Long employeeId){
        Employee employee = (Employee) MyBatisUtils.executeQuery(sqlSession -> {
            EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
            return employeeMapper.selectById((employeeId));
        });
        return employee;
    }
    public Employee selectLeader(Long employeeId){
        Employee l = (Employee)MyBatisUtils.executeQuery(sqlSession -> {
            EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
            Employee employee =  employeeMapper.selectById((employeeId));
            Map params = new HashMap();
            Employee leader = null;
            if(employee.getLevel()<7){
                //查询部门经理
                params.put("level",7);
                params.put("departmentId",employee.getDepartmentId());
                List<Employee> employees = employeeMapper.selectByParams(params);
                leader = employees.get(0);
            }else if(employee.getLevel() == 7){
                //查询经理
                params.put("level",8);
                List<Employee> employees = employeeMapper.selectByParams(params);
                leader = employees.get(0);
            }else if(employee.getLevel() == 8){
                //自己就是经理，返回自己
                leader = employee;
            }
            return leader;
        });
        return l;

    }
}
