package com.example.mybatisplus;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mybatisplus.entity.Product;
import com.example.mybatisplus.entity.User;
import com.example.mybatisplus.mapper.ProductMapper;
import com.example.mybatisplus.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.annotation.Resources;
import java.util.List;

/**
 * @Author: Lishenglong
 * @Date: 2022/7/27 16:57
 */
@SpringBootTest
public class InterceptorTest {

    @Resource
    UserMapper userMapper;

    @Resource
    ProductMapper productMapper;
    @Test
    public void testSelectPage(){
        Page<User> pagePara = new Page<>(1,5);
        userMapper.selectPage(pagePara,null);
        List<User> users = pagePara.getRecords();
        users.forEach(System.out::println);

        long total = pagePara.getTotal();
        System.out.println(total);
        boolean b = pagePara.hasNext();
        System.out.println(b);
        boolean b1 = pagePara.hasPrevious();
        System.out.println(b1);
    }

    @Test
    public void testSelectPageVo(){
        Page<User> pageParam = new Page<>(1,5);
        userMapper.selectPageByPage(pageParam, 18);
        List<User> users = pageParam.getRecords();
        users.forEach(System.out::println);
    }

    @Test
    public void testConcurrentUpdate(){
        //1、小李
        Product p1 = productMapper.selectById(1L);

        //2、小王
        Product p2 = productMapper.selectById(1L);

        //3、小李将价格加了50元，存入了数据库
        p1.setPrice(p1.getPrice() + 50);
        int result1 = productMapper.updateById(p1);
        System.out.println("小李修改结果：" + result1);

        //4、小王将商品减了30元，存入了数据库
        p2.setPrice(p2.getPrice() - 30);
        int result2 = productMapper.updateById(p2);
        if(result2 == 0){//更新失败，重试
            System.out.println("小王重试");
            //重新获取数据
            p2 = productMapper.selectById(1L);
            //更新
            p2.setPrice(p2.getPrice() - 30);
            productMapper.updateById(p2);
        }
        System.out.println("小王修改结果：" + result2);

        //最后的结果
        Product p3 = productMapper.selectById(1L);
        System.out.println("最后的结果：" + p3.getPrice());
    }
}
