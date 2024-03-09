package com.moon.warm;


import com.moon.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class CacheWarmingAnnotation{

    @Autowired
    private DishService dishService;

    @PostConstruct
    public void dishWarmUp() {
        log.info("缓存预热...");
        Long dishId = 16L;
        dishService.findDishVOByCategoryId(dishId);
    }
}
