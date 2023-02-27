package com.lzq.oa.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;
import java.util.function.Function;

public class MyBatisUtils {
    private static SqlSessionFactory sqlSessionFactory = null;
    static {
        Reader reader = null;
        try {
            //读取xml文件
            reader = Resources.getResourceAsReader("mybatis-config.xml");
            //构建SqlSessionFactory
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }

    }
    public static Object executeQuery(Function<SqlSession,Object> func){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Object obj = func.apply(sqlSession);
            return obj;
        }finally {
            sqlSession.close();
        }
    }
    public static Object executeUpdate(Function<SqlSession,Object> func){
        //参数false手动提交、回滚事务
        SqlSession sqlSession = sqlSessionFactory.openSession(false);
        try{
            Object obj = func.apply(sqlSession);
            sqlSession.commit();
            return obj;
        }catch (Exception e){
            sqlSession.rollback();
            throw e;
        }finally {
            sqlSession.close();
        }
    }
}
