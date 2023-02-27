package com.lzq.oa.service;

import com.lzq.oa.entity.Notice;
import com.lzq.oa.mapper.NoticeMapper;
import com.lzq.oa.utils.MyBatisUtils;

import java.util.List;

public class NoticeService {
    public List<Notice> getNoticeList(Long receiverId){
        return (List<Notice>) MyBatisUtils.executeQuery(sqlSession -> {
            NoticeMapper noticeMapper = sqlSession.getMapper(NoticeMapper.class);
            return noticeMapper.selectByReceiverId(receiverId);
        });
    }
}

