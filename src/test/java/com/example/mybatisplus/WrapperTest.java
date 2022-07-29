package com.example.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.mybatisplus.entity.User;
import com.example.mybatisplus.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author: Lishenglong
 * @Date: 2022/7/28 13:58
 */
@SpringBootTest
public class WrapperTest {
    @Resource
    UserMapper userMapper;


    /**
     * 查询名字中包含n，年龄大于等于10且小于等于20，email不为空的用户
     */
    @Test
    public void test1() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .like("name", "n") // name是数据库列名
                .between("age", 10, 20)
                .isNotNull("email");
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    /**
     * 按年龄降序查询用户，如果年龄相同则按id升序排列
     */
    @Test
    public void test2(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .orderByDesc("age")
                .orderByAsc("id");

        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    /**
     *删除email为空的用户
     */
    @Test
    public void test3(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("email");
        int result = userMapper.delete(queryWrapper); //条件构造器也可以构建删除语句的条件
        System.out.println("delete return count = " + result);
    }

    /**
     * 查询名字中包含n，且（年龄小于18或email为空的用户），并将这些用户的年龄设置为18，邮箱设置为 user@atguigu.com
     */
    @Test
    public void test4(){
        //修改条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .like("name", "n")
                .and(i -> i.lt("age", 18).or().isNull("email")); //lam优先运算 i代替queryWrapper

        User user = new User();
        user.setAge(18);
        user.setEmail("user@atguigu.com");
        int result = userMapper.update(user, queryWrapper);
        System.out.println(result);
    }

    /**
     * 组装select子句 - 查询所有用户的用户名和年龄
     */
    @Test
    public void test5() {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("uname", "age");

        //selectMaps()返回Map集合列表，通常配合select()使用，避免User对象中没有被查询到的列值为null
        List<Map<String, Object>> maps = userMapper.selectMaps(queryWrapper);//返回值是Map列表
        maps.forEach(System.out::println);
    }

    /**
     * 实现子查询
     */
    @Test
    public void test6() {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //String sql = "select id from user where id <= 3 or true";//容易造成sql注入
        //queryWrapper.inSql("id", sql);//容易造成sql注入
        queryWrapper.in("id", 1, 2, 3 );
        // 或
        queryWrapper.le("id", 3 );

        //selectObjs的使用场景：只返回一列
        List<Object> objects = userMapper.selectObjs(queryWrapper);//返回值是Object列表
        objects.forEach(System.out::println);
    }

    /**
     * UpdateWrapper
     */
    @Test
    public void test7() {

        //组装set子句
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .set("age", 18)
                .set("email", "user@atguigu.com")
                .like("name", "n")
                .and(i -> i.lt("age", 18).or().isNull("email")); //lambda表达式内的逻辑优先运算

        //这里必须要创建User对象，否则无法应用"自动填充"。如果没有"自动填充"，可以设置为null
        User user = new User();
        int result = userMapper.update(user, updateWrapper);
        System.out.println(result);
    }

    /**
     * condition 查询条件来源于用户输入，是可选的
     */
    @Test
    public void test8() {

        //定义查询条件，有可能为null（用户未输入）
        String name = null;
        Integer ageBegin = 10;
        Integer ageEnd = 20;

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        /*if(StringUtils.isNotBlank(name)){
            queryWrapper.like("name","n");
        }
        if(ageBegin != null){
            queryWrapper.ge("age", ageBegin);
        }
        if(ageEnd != null){
            queryWrapper.le("age", ageEnd);
        }*/
        queryWrapper
                .like(StringUtils.isNotBlank(name), "name", "n")
                .ge(ageBegin != null, "age", ageBegin)
                .le(ageEnd != null, "age", ageEnd);

        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    /**
     * LambdaQueryWrapper 避免编译时无法发现的列名错误
     */
    @Test
    public void test9() {

        //定义查询条件，有可能为null（用户未输入）
        String name = null;
        Integer ageBegin = 10;
        Integer ageEnd = 20;

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                //避免使用字符串表示字段，防止运行时错误
                .like(StringUtils.isNotBlank(name), User::getName, "n")
                .ge(ageBegin != null, User::getAge, ageBegin)
                .le(ageEnd != null, User::getAge, ageEnd);

        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    /**
     * LambdaUpdateWrapper
     */
    @Test
    public void test10() {

        //组装set子句
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .set(User::getAge, 18)
                .set(User::getEmail, "user@atguigu.com")
                .like(User::getName, "n")
                .and(i -> i.lt(User::getAge, 18).or().isNull(User::getEmail)); //lambda表达式内的逻辑优先运算

        User user = new User();
        int result = userMapper.update(user, updateWrapper);
        System.out.println(result);
    }
}
