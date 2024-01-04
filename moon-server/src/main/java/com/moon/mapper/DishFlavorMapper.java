package com.moon.mapper;


import com.moon.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * 添加dish对应的flavor
     */
    void addBatchFlavor(@Param("flavors") List<DishFlavor> flavors);

}
