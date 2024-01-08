package com.moon.controller.user;


import com.moon.constant.JwtClaimsConstant;
import com.moon.dto.UserLoginDTO;
import com.moon.entity.User;
import com.moon.properties.JwtProperties;
import com.moon.result.Result;
import com.moon.service.UserService;
import com.moon.utils.JwtUtil;
import com.moon.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/user/user")
public class UserController {

    @Autowired
    private UserService userService;


    @Autowired
    private JwtProperties jwtProperties;


    /**
     * 用户登录
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("用户登录: {}", userLoginDTO);
        User user = userService.login(userLoginDTO);


        // 为用户生成jwt token
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims);

        // 返回
        UserLoginVO userLoginVO = UserLoginVO
                .builder()
                .id(user.getId())
                .openid(user.getOpenid())
                .token(token)
                .build();
        return Result.success(userLoginVO);


    }
}
