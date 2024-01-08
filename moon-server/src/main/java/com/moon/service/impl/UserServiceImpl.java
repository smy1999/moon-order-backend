package com.moon.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.moon.constant.JwtClaimsConstant;
import com.moon.constant.MessageConstant;
import com.moon.dto.UserLoginDTO;
import com.moon.entity.User;
import com.moon.exception.LoginFailedException;
import com.moon.mapper.UserMapper;
import com.moon.properties.WeChatProperties;
import com.moon.service.UserService;
import com.moon.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserMapper userMapper;

    private static final String WX_LOGIN_API = "https://api.weixin.qq.com/sns/jscode2session";

    @Override
    public User login(UserLoginDTO userLoginDTO) {

        String openid = getOpenid(userLoginDTO.getCode());

        if (openid == null || openid.isEmpty()) {
            throw new LoginFailedException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // 判断是登录还是注册
        User user = userMapper.findByOpenid(openid);
        if (user == null) {
            user = User.builder()
                    .createTime(LocalDateTime.now())
                    .openid(openid)
                    .build();
            userMapper.add(user);
        }
        return user;
    }

    private String getOpenid(String code) {
        // 发送请求鉴权,获取返回的openid
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("appid", weChatProperties.getAppid());
        paramMap.put("secret", weChatProperties.getSecret());
        paramMap.put("js_code", code);
        paramMap.put("grant_type", "authorization_code");

        // 解析结果
        String json = HttpClientUtil.doGet(WX_LOGIN_API, paramMap);
        JSONObject jsonObject = JSON.parseObject(json);
        return jsonObject.getString("openid");
    }


}
