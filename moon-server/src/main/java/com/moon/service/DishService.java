package com.moon.service;

import com.moon.dto.DishDTO;
import com.moon.dto.DishPageQueryDTO;
import com.moon.result.PageResult;

public interface DishService {

    /**
     * 新增菜品
     * @param dishDTO
     */
    void addDish(DishDTO dishDTO);

    /**
     * 分页查询菜品
     * @param dishPageQueryDTO
     * @return
     */
    PageResult findPage(DishPageQueryDTO dishPageQueryDTO);
}
