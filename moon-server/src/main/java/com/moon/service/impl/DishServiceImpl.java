package com.moon.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.moon.dto.DishDTO;
import com.moon.dto.DishPageQueryDTO;
import com.moon.entity.Dish;
import com.moon.entity.DishFlavor;
import com.moon.mapper.DishFlavorMapper;
import com.moon.mapper.DishMapper;
import com.moon.result.PageResult;
import com.moon.service.DishService;
import com.moon.vo.DishVO;
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

        dishMapper.addDish(dish);
        Long dishId = dish.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();

        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(
                    flavor -> flavor.setDishId(dishId)
            );
            dishFlavorMapper.addBatchFlavor(flavors);
        }

    }

    @Override
    public PageResult findPage(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());

        List<DishVO> dishes = dishMapper.findPage(dishPageQueryDTO);
        PageInfo<DishVO> pageInfo = new PageInfo<>(dishes);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }
}
