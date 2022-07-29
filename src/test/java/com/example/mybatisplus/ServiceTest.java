package com.example.mybatisplus;

import com.example.mybatisplus.entity.User;
import com.example.mybatisplus.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Lishenglong
 * @Date: 2022/7/26 17:04
 */

@SpringBootTest
public class ServiceTest {

    @Resource
    UserService userService;

    @Test
    public void testCount(){
        int count = userService.count();
        System.out.println("总记录数： "+ count);
    }

    @Test
    public void saveSaveBatch(){
        ArrayList<User> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setName("花花" + i);
            user.setAge(20+i);
            list.add(user);
        }
        boolean b = userService.saveBatch(list);
        System.out.println("插入是否成功： " + b );

    }

    @Test
    public void testListByName(){
        List<User> tom = userService.listAllByName("Tom");
        tom.forEach(System.out::println);
    }
}
