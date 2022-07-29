package com.example.mybatisplus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mybatisplus.entity.User;
import com.example.mybatisplus.mapper.UserMapper;
import com.example.mybatisplus.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: Lishenglong
 * @Date: 2022/7/26 16:11
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    //@Resource
    //UserMapper userMapper;

    @Override
    public List<User> listAllByName(String name) {
        List<User> users = baseMapper.selectAllByName(name); //使用baseMapper父类已经存在 userMapper继承baseMapper
        return users;
    }
}
