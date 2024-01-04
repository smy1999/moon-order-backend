package com.moon.service;

import com.moon.dto.SetmealDTO;
import com.moon.dto.SetmealPageQueryDTO;
import com.moon.result.PageResult;
import com.moon.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    /**
     * 添加套餐
     * @param setmealDTO
     */
    void add(SetmealDTO setmealDTO);

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult page(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 批量删除
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    SetmealVO getById(Long id);

    /**
     * 修改菜品
     * @param setmealDTO
     */
    void update(SetmealDTO setmealDTO);

    /**
     * 修改状态
     * @param id
     * @param status
     */
    void changeStatus(Long id, Integer status);
}

