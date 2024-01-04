package com.moon.service;

import com.moon.dto.DishDTO;

public interface DishService {

    /**
     * 新增菜品
     * @param dishDTO
     */
    void addDish(DishDTO dishDTO);
}
