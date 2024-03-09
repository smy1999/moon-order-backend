package com.moon.warm;


import com.moon.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//@Component
@Slf4j
public class CacheWarmingRunner implements CommandLineRunner {

//    @Autowired
    private DishService dishService;

    @Override
    public void run(String... args) throws Exception {
        log.info("缓存预热...");
        Long dishId = 16L;
        dishService.findDishVOByCategoryId(dishId);
    }
}
