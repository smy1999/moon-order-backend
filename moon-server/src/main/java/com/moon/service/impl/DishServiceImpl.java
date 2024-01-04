package com.moon.service.impl;

import com.moon.constant.StatusConstant;
import com.moon.dto.DishDTO;
import com.moon.entity.Dish;
import com.moon.entity.DishFlavor;
import com.moon.mapper.DishFlavorMapper;
import com.moon.mapper.DishMapper;
import com.moon.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private DishMapper dishMapper;


    @Override
    @Transactional
    public void addDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dish.setStatus(StatusConstant.DISABLE);

        dishMapper.addDish(dish);
        Long dishId = dish.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();

        dishFlavorMapper.addBatchFlavor(flavors, dishId);


    }
}
