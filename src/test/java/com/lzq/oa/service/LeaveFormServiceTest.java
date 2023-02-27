package com.lzq.oa.service;

import com.lzq.oa.entity.LeaveForm;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class LeaveFormServiceTest {
    LeaveFormService leaveFormService = new LeaveFormService();
    @Test
    public void createLeaveForm() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        LeaveForm leaveForm = new LeaveForm();
        leaveForm.setEmployeeId(3l);
        leaveForm.setStartTime(sdf.parse("20200308"));
        leaveForm.setEndTime(sdf.parse("20200315"));
        leaveForm.setFormType(1);
        leaveForm.setReason("市场部员工请假超过三天");
        leaveForm.setCreateTime(new Date());
        LeaveForm savedForm = leaveFormService.createLeaveForm(leaveForm);
        leaveFormService.audit(savedForm.getFormId(),2l,"approved","部门经理同意");
        leaveFormService.audit(savedForm.getFormId(),1l,"approved","总经理同意");
        System.out.println(savedForm.getFormId());
    }
    @Test
    public void createLeaveForm1() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        LeaveForm leaveForm = new LeaveForm();
        leaveForm.setEmployeeId(8l);
        leaveForm.setStartTime(sdf.parse("20200308"));
        leaveForm.setEndTime(sdf.parse("20200303"));
        leaveForm.setFormType(1);
        leaveForm.setReason("市场部员工请假少于三天");
        leaveForm.setCreateTime(new Date());
        LeaveForm savedForm = leaveFormService.createLeaveForm(leaveForm);
        System.out.println(savedForm.getFormId());
    }
    @Test
    public void createLeaveForm2() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        LeaveForm leaveForm = new LeaveForm();
        leaveForm.setEmployeeId(2l);
        leaveForm.setStartTime(sdf.parse("20200308"));
        leaveForm.setEndTime(sdf.parse("20200315"));
        leaveForm.setFormType(1);
        leaveForm.setReason("部门经理请假超过3天");
        leaveForm.setCreateTime(new Date());
        LeaveForm savedForm = leaveFormService.createLeaveForm(leaveForm);
        System.out.println(savedForm.getFormId());
    }
    @Test
    public void createLeaveForm3() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        LeaveForm leaveForm = new LeaveForm();
        leaveForm.setEmployeeId(1l);
        leaveForm.setStartTime(sdf.parse("20200308"));
        leaveForm.setEndTime(sdf.parse("20200315"));
        leaveForm.setFormType(1);
        leaveForm.setReason("经理老赖");
        leaveForm.setCreateTime(new Date());
        LeaveForm savedForm = leaveFormService.createLeaveForm(leaveForm);
        System.out.println(savedForm.getFormId());
    }
}