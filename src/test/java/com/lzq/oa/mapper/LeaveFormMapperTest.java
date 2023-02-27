package com.lzq.oa.mapper;

import com.lzq.oa.entity.LeaveForm;
import com.lzq.oa.utils.MyBatisUtils;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class LeaveFormMapperTest {

    @Test
    public void insert() {
        MyBatisUtils.executeUpdate(sqlSession -> {
            LeaveFormMapper mapper = sqlSession.getMapper(LeaveFormMapper.class);
            LeaveForm form = new LeaveForm();
            form.setEmployeeId(7l);
            form.setFormType(3);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startTime = null;
            Date endTime = null;
            try{
                startTime = sdf.parse("2022-03-08 08:00:00");
                endTime = sdf.parse("2022-03-15 12:00:00");
            }catch(ParseException e){
                e.printStackTrace();
            }
            form.setStartTime(startTime);
            form.setEndTime(endTime);
            form.setReason("回家过年");
            form.setCreateTime(new Date());
            form.setState("Processing");
            mapper.insert(form);
            return null;
        });
    }

    @Test
    public void selectByParams() {
        MyBatisUtils.executeQuery(sqlSession -> {
            LeaveFormMapper leaveFormMapper = sqlSession.getMapper(LeaveFormMapper.class);
            List<Map> list = leaveFormMapper.selectByParams("process",2l);
            System.out.println(list);
            return list;
        });
    }
}