package com.moon.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.moon.constant.MessageConstant;
import com.moon.constant.StatusConstant;
import com.moon.dto.DishDTO;
import com.moon.dto.DishPageQueryDTO;
import com.moon.entity.Dish;
import com.moon.entity.DishFlavor;
import com.moon.exception.DeletionNotAllowedException;
import com.moon.mapper.DishFlavorMapper;
import com.moon.mapper.DishMapper;
import com.moon.mapper.SetmealDishMapper;
import com.moon.result.PageResult;
import com.moon.service.DishService;
import com.moon.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

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

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {

        // 判断是否存在启售中
        List<Integer> statuses = dishMapper.getStatuses(ids);
        if (statuses.contains(StatusConstant.ENABLE)) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
        }

        // 判断是否关联了套餐
        List<Integer> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if (setmealIds != null && !setmealIds.isEmpty()) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        dishMapper.deleteBatch(ids);

        dishFlavorMapper.deleteBatch(ids);

    }

    @Override
    public DishVO findById(Long id) {
        DishVO dishVO = new DishVO();

        Dish dish = dishMapper.findById(id);
        BeanUtils.copyProperties(dish, dishVO);

        List<DishFlavor> flavors = dishFlavorMapper.findById(id);
        dishVO.setFlavors(flavors);

        return dishVO;
    }

    @Override
    @Transactional
    public void updateDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        dishMapper.updateDish(dish);

        List<DishFlavor> flavors = dishDTO.getFlavors();
        Long dishId = dish.getId();
        List<Long> dishIds = new ArrayList<>();
        dishIds.add(dishId);

        dishFlavorMapper.deleteBatch(dishIds);

        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(flavor -> flavor.setDishId(dishId));
            dishFlavorMapper.addBatchFlavor(flavors);
        }

    }

    @Override
    public void changeStatus(Long id, Integer status) {
        Dish dish = Dish.builder()
                .status(status)
                .id(id)
                .build();
        dishMapper.updateDish(dish);
    }

    @Override
    public List<Dish> findDishByCategoryId(Long categoryId) {
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();

        return dishMapper.findDishList(dish);
    }

    @Override
    public List<DishVO> findDishVOByCategoryId(Long categoryId) {
        List<DishVO> dishes = dishMapper.getDishVOByCategoryId(categoryId);
        dishes.forEach(
                dish -> dish.setFlavors(dishFlavorMapper.findById(dish.getId()))
        );
        return dishes;
    }
}
