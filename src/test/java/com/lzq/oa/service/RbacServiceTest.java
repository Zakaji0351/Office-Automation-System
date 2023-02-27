package com.lzq.oa.service;

import com.lzq.oa.entity.Node;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class RbacServiceTest {
    private RbacService rbacService = new RbacService();
    @Test
    public void selectNodeByUserId() {
        List<Node> list = rbacService.selectNodeByUserId(5l);
        for(Node n: list){
            System.out.println(n.getNodeName());
        }

    }
}