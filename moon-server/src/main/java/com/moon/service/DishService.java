package com.moon.service;

import com.moon.dto.DishDTO;
import com.moon.dto.DishPageQueryDTO;
import com.moon.entity.Dish;
import com.moon.result.PageResult;
import com.moon.vo.DishVO;

import java.util.List;

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

    /**
     * 批量删除菜品
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    DishVO findById(Long id);

    /**
     * 修改菜品
     * @param dishDTO
     */
    void updateDish(DishDTO dishDTO);

    /**
     * 修改状态
     * @param id
     * @param status
     */
    void changeStatus(Long id, Integer status);

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    List<Dish> findByCategoryId(Long categoryId);
}
