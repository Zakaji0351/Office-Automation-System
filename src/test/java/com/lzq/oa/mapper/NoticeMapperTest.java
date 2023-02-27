package com.lzq.oa.mapper;

import com.lzq.oa.entity.Notice;
import com.lzq.oa.utils.MyBatisUtils;
import org.junit.Test;

import static org.junit.Assert.*;

public class NoticeMapperTest {

    @Test
    public void insert() {
        MyBatisUtils.executeUpdate(sqlSession -> {
            NoticeMapper mapper = sqlSession.getMapper(NoticeMapper.class);
            Notice notice = new Notice(2L,"test message");
            mapper.insert(notice);
            return null;
        });
    }
}