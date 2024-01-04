package com.moon.mapper;

import com.moon.annotation.AutoFill;
import com.moon.dto.DishDTO;
import com.moon.dto.DishPageQueryDTO;
import com.moon.entity.Dish;
import com.moon.enumeration.OperationType;
import com.moon.vo.DishVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 添加菜品
     *
     * @param dish
     */
    @AutoFill(OperationType.INSERT)
    void addDish(Dish dish);

    /**
     * 分页查询菜品
     * @param dishPageQueryDTO
     * @return
     */
    List<DishVO> findPage(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 查询菜品售卖状态集合
     * @param ids
     * @return
     */
    List<Integer> getStatuses(List<Long> ids);

    /**
     * 批量删除菜品
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Select("SELECT * FROM dish WHERE id = #{id}")
    Dish findById(Long id);

    /**
     * 更新dish
     * @param dish
     */
    @AutoFill(OperationType.UPDATE)
    void updateDish(Dish dish);

    /**
     * 根据条件查询
     * @param dish
     * @return
     */
    List<Dish> findList(Dish dish);

    /**
     * 根据id查询是否
     * @param id
     * @return
     */
    @Select("""
            SELECT dish.*
            FROM dish
                     LEFT JOIN setmeal_dish ON dish.id = setmeal_dish.dish_id
            WHERE setmeal_id = 43
            """)
    List<Dish> findBySetmealId(Long id);
}
