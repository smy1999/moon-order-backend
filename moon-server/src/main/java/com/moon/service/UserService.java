package com.moon.service;

import com.moon.dto.UserLoginDTO;
import com.moon.entity.User;
import com.moon.vo.UserLoginVO;

public interface UserService {

    /**
     * 用户登录业务
     * @param userLoginDTO
     * @return
     */
    User login(UserLoginDTO userLoginDTO);

}
