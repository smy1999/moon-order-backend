package com.moon.service.impl;

import com.moon.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class ShopServiceImpl implements ShopService {

    private static final String KEY = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void setStatus(Integer status) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(KEY, status);
    }

    @Override
    public Integer getStatus() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Integer status = (Integer) valueOperations.get(KEY);
        return status;
    }
}
