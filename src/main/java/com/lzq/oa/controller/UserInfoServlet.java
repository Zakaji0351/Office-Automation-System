package com.lzq.oa.controller;

import com.lzq.oa.entity.Department;
import com.lzq.oa.entity.Employee;
import com.lzq.oa.entity.Node;
import com.lzq.oa.service.DepartmentService;
import com.lzq.oa.service.EmployeeService;
import com.lzq.oa.service.RbacService;
import com.lzq.oa.utils.ResponseUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/user_info")
public class UserInfoServlet extends HttpServlet {
    private RbacService rbacService = new RbacService();
    private EmployeeService employeeService = new EmployeeService();
    private DepartmentService departmentService = new DepartmentService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("userId");
        String emplpyeeId = request.getParameter("employeeId");
        List<Node> list = rbacService.selectNodeByUserId(Long.parseLong(userId));
        List<Map> treeList = new ArrayList<>();
        Map module = null;
        for(Node node: list){
            if(node.getNodeType()==1){
                module = new LinkedHashMap();
                module.put("node",node);
                module.put("children",new ArrayList());
                treeList.add(module);
            }
            else if(node.getNodeType()==2){
                List children = (List)module.get("children");
                children.add(node);
            }
        }
        Employee employee = employeeService.selectById(Long.parseLong(emplpyeeId));
        Department department = departmentService.selectById(employee.getDepartmentId());
        String json = new ResponseUtils().put("nodeList",treeList)
                .put("employee",employee).put("department",department).toJsonString();
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(json);

    }
}
