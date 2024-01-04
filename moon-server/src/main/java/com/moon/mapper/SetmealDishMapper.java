package com.moon.mapper;

import com.moon.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
     * 批量添加与套餐关联的菜品
     * @param dishes
     */
    void addBatch(List<SetmealDish> dishes);

    /**
     * 根据setmeal id 删除相关
     * @param setmealIds
     */
    void deleteBatchBySetmealId(List<Long> setmealIds);

    /**
     * 根据 setmeal id 获取 dish
     *
     * @param id
     */
    @Select("SELECT * FROM setmeal_dish WHERE setmeal_id = #{id}")
    List<SetmealDish> getBySetmealId(Long id);

    /**
     * 查看 dish 是否有对应的 setmeal
     * @param dishIds
     * @return
     */
    List<Integer> getSetmealIdsByDishIds(List<Long> dishIds);
}
