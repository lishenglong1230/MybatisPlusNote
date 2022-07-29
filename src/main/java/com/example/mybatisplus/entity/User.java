package com.example.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: Lishenglong
 * @Date: 2022/7/26 14:32
 */
@Data
//@TableName(value = "t_user")
public class User {

    //@TableId(type = IdType.ASSIGN_ID) //默认雪花算法
    private Long id;
    //名称映射解决方案3
    @TableField(value = "uname")
    private String name;
    private Integer age;
    private String email;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    @TableField(value = "is_deleted")
    private boolean deleted; //0 false 未删除 ; 1 true 已删除
}
