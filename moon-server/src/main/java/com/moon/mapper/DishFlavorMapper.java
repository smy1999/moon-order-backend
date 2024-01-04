package com.moon.mapper;


import com.moon.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * 添加dish对应的flavor
     */
    void addBatchFlavor(@Param("flavors") List<DishFlavor> flavors);

    /**
     * 删除 dish 对应的 flavor
     * @param dishIds
     */
    void deleteBatch(List<Long> dishIds);

    /**
     * 根据dish ID 查询flavor
     * @param dishId
     * @return
     */
    @Select("SELECT * FROM dish_flavor WHERE dish_id = #{dishId}")
    List<DishFlavor> findById(Long dishId);


}
