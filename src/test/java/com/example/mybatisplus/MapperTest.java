package com.example.mybatisplus;

import com.baomidou.mybatisplus.annotation.TableField;
import com.example.mybatisplus.entity.User;
import com.example.mybatisplus.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.nio.channels.ServerSocketChannel;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Lishenglong
 * @Date: 2022/7/26 15:25
 */
@SpringBootTest
public class MapperTest {

    @Resource
    private UserMapper userMapper;

    @Test
    public void testInsert(){
        User user = new User();
        user.setName("Helen");
        user.setAge(18);
        //不设置email属性，则生成的动态sql中不包括email字段

        //user.setCreateTime(LocalDateTime.now());
        //user.setUpdateTime(LocalDateTime.now());

        int result = userMapper.insert(user);
        System.out.println("影响的行数：" + result); //影响的行数
        System.out.println("id：" + user.getId()); //id自动回填

    }

    @Test
    public void testSelect(){
        User user = userMapper.selectById(1);
        System.out.println(user);

        List<User> users = userMapper.selectBatchIds(Arrays.asList(1, 2, 3));
        users.forEach(System.out::println);

        Map<String, Object> map = new HashMap<>();
        map.put("name","建国");
        map.put("age",74);
        List<User> users1 = userMapper.selectByMap(map);
        users1.forEach(System.out::println);
    }

    @Test
    public void testUpdate(){
        User user = new User();
        user.setId(2L);
        user.setAge(100);
        //user.setUpdateTime(LocalDateTime.now());
        int i = userMapper.updateById(user);
        System.out.println(i);
    }

    @Test
    public void testDelete(){

        int i = userMapper.deleteById(2L);
        System.out.println("结果"+i);
    }

    @Test
    public void testSelectByName(){
        List<User> jack = userMapper.selectAllByName("Jack");
        jack.forEach(System.out::println);
    }


}
