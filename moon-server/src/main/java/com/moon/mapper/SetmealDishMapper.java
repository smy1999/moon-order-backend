package com.moon.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 给定dish列表在套餐中出现次数
     * @param ids
     * @return
     */
    Integer countByDish(List<Long> ids);

    /**
     * 根据dishIds获取SetmealIds
     * @param dishIds
     * @return
     */
    List<Integer> getSetmealIdsByDishIds(List<Long> dishIds);
}
