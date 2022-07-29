package com.example.mybatisplus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.mybatisplus.entity.User;

import java.util.List;

/**
 * @Author: Lishenglong
 * @Date: 2022/7/26 16:10
 */
public interface UserService extends IService<User> {
    List<User> listAllByName(String name);
}
