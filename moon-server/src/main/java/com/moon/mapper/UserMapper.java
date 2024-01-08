package com.moon.mapper;


import com.moon.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {


    /**
     * 根据openid查找用户
     * @param openid
     * @return
     */
    User findByOpenid(String openid);

    /**
     * 添加用户
     * @param user
     */
    void add(User user);
}
