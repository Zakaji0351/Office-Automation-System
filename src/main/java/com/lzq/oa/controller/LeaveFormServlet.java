package com.lzq.oa.controller;

import com.lzq.oa.entity.LeaveForm;
import com.lzq.oa.service.LeaveFormService;
import com.lzq.oa.utils.ResponseUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@WebServlet("/api/leave/*")
public class LeaveFormServlet extends HttpServlet {
    private LeaveFormService leaveFormService = new LeaveFormService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        String uri = request.getRequestURI();
        String methodName = uri.substring(uri.lastIndexOf("/")+1);
        if(methodName.equals("create")){
            this.create(request,response);
        }else if(methodName.equals("list")){
            this.list(request,response);
        }else if(methodName.equals("audit")){
            this.audit(request,response);

        }
    }
    private void create(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String employeeId = request.getParameter("eid");
        String formType = request.getParameter("formType");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String reason = request.getParameter("reason");
        LeaveForm form = new LeaveForm();
        form.setEmployeeId(Long.parseLong(employeeId));
        form.setFormType(Integer.parseInt(formType));
        form.setStartTime(new Date(Long.parseLong(startTime)));
        form.setEndTime(new Date(Long.parseLong(endTime)));
        form.setReason(reason);
        form.setCreateTime(new Date());
        ResponseUtils responseUtils = null;
        try{
            leaveFormService.createLeaveForm(form);
            responseUtils = new ResponseUtils();
        }catch (Exception e){
            e.printStackTrace();
            responseUtils = new ResponseUtils(e.getClass().getSimpleName(),e.getMessage());
        }
        response.getWriter().println(responseUtils.toJsonString());
    }
    private void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String employeeId = request.getParameter("eid");
        ResponseUtils responseUtils = null;
        try {
            List<Map> formList = leaveFormService.getLeaveFormList("process", Long.parseLong(employeeId));
            responseUtils = new ResponseUtils().put("list",formList);
        }catch(Exception e){
            e.printStackTrace();
            responseUtils = new ResponseUtils(e.getClass().getSimpleName(),e.getMessage());
        }
        response.getWriter().println(responseUtils.toJsonString());
    }
    private void audit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String formId = request.getParameter("formId");
        String result = request.getParameter("result");
        String reason = request.getParameter("reason");
        String eid = request.getParameter("eid");
        ResponseUtils responseUtils = null;
        try {
            leaveFormService.audit(Long.parseLong(formId), Long.parseLong(eid), result, reason);
            responseUtils = new ResponseUtils();
        }catch(Exception e){
            e.printStackTrace();
            responseUtils = new ResponseUtils(e.getClass().getSimpleName(),e.getMessage());
        }
        response.getWriter().println(responseUtils.toJsonString());
    }
}
