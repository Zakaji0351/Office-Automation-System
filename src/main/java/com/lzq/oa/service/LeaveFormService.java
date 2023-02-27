package com.lzq.oa.service;

import com.lzq.oa.entity.Employee;
import com.lzq.oa.entity.LeaveForm;
import com.lzq.oa.entity.Notice;
import com.lzq.oa.entity.ProcessFlow;
import com.lzq.oa.mapper.EmployeeMapper;
import com.lzq.oa.mapper.LeaveFormMapper;
import com.lzq.oa.mapper.NoticeMapper;
import com.lzq.oa.mapper.ProcessFlowMapper;
import com.lzq.oa.service.exception.LeaveFormException;
import com.lzq.oa.utils.MyBatisUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LeaveFormService {
    private EmployeeService employeeService = new EmployeeService();

    public LeaveForm createLeaveForm(LeaveForm form) {
        LeaveForm leaveForm = (LeaveForm) MyBatisUtils.executeUpdate(sqlSession -> {
            //1.持久化form表单数据，8级以下状态为processing， 8级直接approved
            EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
            Employee employee = employeeMapper.selectById(form.getEmployeeId());
            if (employee.getLevel() == 8) {
                form.setState("approved");
            } else {
                form.setState("processing");
            }
            LeaveFormMapper leaveFormMapper = sqlSession.getMapper(LeaveFormMapper.class);
            leaveFormMapper.insert(form);
            NoticeMapper noticeMapper = sqlSession.getMapper(NoticeMapper.class);
            //2.增加第一条流程数据，说明表单已提交，状态为complete
            ProcessFlowMapper processFlowMapper = sqlSession.getMapper(ProcessFlowMapper.class);
            ProcessFlow flow1 = new ProcessFlow();
            flow1.setFormId(form.getFormId());
            flow1.setOperatorId(employee.getEmployeeId());
            flow1.setAction("apply");
            flow1.setCreateTime(new Date());
            flow1.setOrderNo(1);//流程中的第一个节点
            flow1.setState("complete");
            flow1.setIsLast(0);
            processFlowMapper.insert(flow1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH");
            //3.分情况创建其余流程数据
            //3.1 7级以下员工，生成部门经理审批任务，请假时间大于等于三天，生成总经理审批任务
            if (employee.getLevel() < 7) {
                Employee dmanager = employeeService.selectLeader(employee.getEmployeeId());
                ProcessFlow flow2 = new ProcessFlow();
                flow2.setFormId(form.getFormId());
                flow2.setOperatorId(dmanager.getEmployeeId());
                flow2.setAction("audit");
                flow2.setCreateTime(new Date());
                flow2.setOrderNo(2);
                flow2.setState("process");
                long diff = form.getEndTime().getTime() - form.getStartTime().getTime();
                float hours = diff / (1000 * 60 * 60) * 1f;
                if (hours < 72) {
                    flow2.setIsLast(1);
                    processFlowMapper.insert(flow2);
                } else {
                    flow2.setIsLast(0);
                    processFlowMapper.insert(flow2);
                    Employee manager = employeeService.selectLeader(dmanager.getEmployeeId());
                    ProcessFlow flow3 = new ProcessFlow();
                    flow3.setFormId(form.getFormId());
                    flow3.setOperatorId(manager.getEmployeeId());
                    flow3.setAction("audit");
                    flow3.setCreateTime(new Date());
                    flow3.setOrderNo(3);
                    flow3.setState("ready");
                    flow3.setIsLast(1);
                    processFlowMapper.insert(flow3);
                }
                String notice1 = String.format("你的请假申请[%s-%s]已提交，请等待上级审批", sdf.format(form.getStartTime()), sdf.format(form.getEndTime()));
                noticeMapper.insert(new Notice(employee.getEmployeeId(), notice1));
                String notice2 = String.format("%s-%s提起请假申请[%s-%s],请尽快审批", employee.getTitle(), employee.getName(), sdf.format(form.getStartTime()), sdf.format(form.getEndTime()));
                noticeMapper.insert(new Notice(dmanager.getEmployeeId(),notice2));
            }
            //7级员工，仅生成总经理审批任务
            else if (employee.getLevel() == 7) {
                Employee manager = employeeService.selectLeader(employee.getEmployeeId());
                ProcessFlow flow2 = new ProcessFlow();
                flow2.setFormId(form.getFormId());
                flow2.setOperatorId(manager.getEmployeeId());
                flow2.setAction("audit");
                flow2.setCreateTime(new Date());
                flow2.setOrderNo(2);
                flow2.setState("process");
                flow2.setIsLast(1);
                processFlowMapper.insert(flow2);
                String notice1 = String.format("你的请假申请[%s-%s]已提交，请等待上级审批", sdf.format(form.getStartTime()), sdf.format(form.getEndTime()));
                noticeMapper.insert(new Notice(employee.getEmployeeId(), notice1));
                String notice2 = String.format("%s-%s提起请假申请[%s-%s],请尽快审批", employee.getTitle(), employee.getName(), sdf.format(form.getStartTime()), sdf.format(form.getEndTime()));
                noticeMapper.insert(new Notice(manager.getEmployeeId(),notice2));
            }
            //八级员工，自己审批
            else if (employee.getLevel() == 8) {
                ProcessFlow flow2 = new ProcessFlow();
                flow2.setFormId(form.getFormId());
                flow2.setOperatorId(employee.getEmployeeId());
                flow2.setAction("audit");
                flow2.setResult("approved");
                flow2.setReason("automatically approved");
                flow2.setCreateTime(new Date());
                flow2.setAuditTime(new Date());
                flow2.setState("complete");
                flow2.setOrderNo(2);
                flow2.setIsLast(1);
                processFlowMapper.insert(flow2);
                String notice = String.format("您的请假申请[%s-%s]系统已自动批准",sdf.format(form.getStartTime()),sdf.format(form.getEndTime()));
                noticeMapper.insert(new Notice(employee.getEmployeeId(),notice));
            }
            return form;
        });
        return leaveForm;
    }

    public List<Map> getLeaveFormList(String pfState, Long operatorId) {
        return (List<Map>) MyBatisUtils.executeQuery(sqlSession -> {
            LeaveFormMapper mapper = sqlSession.getMapper(LeaveFormMapper.class);
            List<Map> maps = mapper.selectByParams(pfState, operatorId);
            return maps;
        });
    }

    public void audit(Long formId, Long operatorId, String result, String reason) {
        MyBatisUtils.executeUpdate(sqlSession -> {
            ProcessFlowMapper processFlowMapper = sqlSession.getMapper(ProcessFlowMapper.class);
            List<ProcessFlow> flowList = processFlowMapper.selectByFormId(formId);
            if (flowList.size() == 0) {
                throw new LeaveFormException("无效审批流程");
            }
            List<ProcessFlow> processList = flowList.stream()
                    .filter(p -> p.getOperatorId() == operatorId && p.getState().equals("process")).collect(Collectors.toList());
            ProcessFlow process = null;
            if (processList.size() == 0) {
                throw new LeaveFormException("未找到待处理任务节点");
            } else {
                process = processList.get(0);
                process.setState("complete");
                process.setResult(result);
                process.setReason(reason);
                process.setAuditTime(new Date());
                processFlowMapper.update(process);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH");
            NoticeMapper noticeMapper = sqlSession.getMapper(NoticeMapper.class);
            LeaveFormMapper leaveFormMapper = sqlSession.getMapper(LeaveFormMapper.class);
            LeaveForm leaveForm = leaveFormMapper.selectById(formId);
            Employee operator = employeeService.selectById(operatorId);
            Employee employee = employeeService.selectById(leaveForm.getEmployeeId());
            if (process.getIsLast() == 1) {//如果是最后一个节点,代表流程结束，更新请假单状态为approved或者refused
                leaveForm.setState(result);
                leaveFormMapper.update(leaveForm);
                String notice1 = String.format("您的请假申请[%s-%s]%s%s已%s,审批意见：%s,审批流程结束",sdf.format(leaveForm.getStartTime()),sdf.format(leaveForm.getEndTime())
                                    ,operator.getTitle(),operator.getName(),result,reason);
                noticeMapper.insert(new Notice(leaveForm.getEmployeeId(),notice1));
                String notice2 = String.format("%s-%s提起请假申请[%s-%s]您已%s,审批意见：%s,审批流程结束",employee.getTitle(),employee.getName()
                                        , sdf.format(leaveForm.getStartTime()),sdf.format(leaveForm.getEndTime()),result,reason);
                noticeMapper.insert(new Notice(operatorId,notice2));
            } else {
                //readylist包含所有后续节点
                List<ProcessFlow> readyList = flowList.stream().filter(p -> p.getState().equals("ready")).collect(Collectors.toList());
                //如果不是最后一个节点且审批通过，下一个节点状态从ready变为process
                if (result.equals("approved")) {
                    ProcessFlow readyProcess = readyList.get(0);
                    readyProcess.setState("process");
                    processFlowMapper.update(readyProcess);
                    //消息1，通知表单提交人，部门经理已审批
                    String notice1 = String.format("您的请假申请[%s-%s]%s%s已批准,审批意见：%s,请等待总经理审批",sdf.format(leaveForm.getStartTime()),sdf.format(leaveForm.getEndTime())
                            ,operator.getTitle(),operator.getName(),reason);
                    noticeMapper.insert(new Notice(leaveForm.getEmployeeId(),notice1));
                    //消息2，通知总经理有新的审批任务
                    String notice2 = String.format("%s-%s提起请假申请[%s-%s],请尽快审批",employee.getTitle(),employee.getName()
                            , sdf.format(leaveForm.getStartTime()),sdf.format(leaveForm.getEndTime()));
                    noticeMapper.insert(new Notice(readyProcess.getOperatorId(),notice2));
                    //消息3，通知部门经理，交由上级
                    String notice3 = String.format("%s-%s提起请假申请[%s-%s]您已批准,审批意见：%s,交由总经理审批",employee.getTitle(),employee.getName()
                            , sdf.format(leaveForm.getStartTime()),sdf.format(leaveForm.getEndTime()),reason);
                    noticeMapper.insert(new Notice(operatorId,notice3));
                } else if (result.equals("refused")) {
                    //如果不是最后一个节点且被驳回，后续所有流程状态变为cancel， 请假单状态变为refused
                    for (ProcessFlow p : readyList) {
                        p.setState("cancel");
                        processFlowMapper.update(p);
                    }
                    leaveForm.setState("refused");
                    leaveFormMapper.update(leaveForm);
                    //消息1，通知表单提交人申请被驳回
                    String notice1 = String.format("您的请假申请[%s-%s]%s%s已驳回,审批意见：%s,审批流程结束",sdf.format(leaveForm.getStartTime()),sdf.format(leaveForm.getEndTime())
                            ,operator.getTitle(),operator.getName(),reason);
                    noticeMapper.insert(new Notice(leaveForm.getEmployeeId(),notice1));
                    //消息2，通知经办人，已驳回
                    String notice2 = String.format("%s-%s提起请假申请[%s-%s],您已驳回，审批意见：%s,审批结束",employee.getTitle(),employee.getName()
                            , sdf.format(leaveForm.getStartTime()),sdf.format(leaveForm.getEndTime()),reason);
                    noticeMapper.insert(new Notice(operatorId,notice2));
                }
            }
            return null;
        });
    }
}
