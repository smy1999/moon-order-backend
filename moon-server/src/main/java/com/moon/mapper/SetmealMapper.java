package com.moon.mapper;

import com.moon.annotation.AutoFill;
import com.moon.dto.SetmealPageQueryDTO;
import com.moon.entity.Setmeal;
import com.moon.enumeration.OperationType;
import com.moon.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 添加菜品
     *
     * @param setmeal
     */
    @AutoFill(OperationType.INSERT)
    void add(Setmeal setmeal);

    /**
     * 根据条件查找
     * @return
     */
    List<SetmealVO> findAllWithCategoryName(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 根据id返回状态集合
     * @param ids
     * @return
     */
    List<Integer> getStatusesByIds(List<Long> ids);

    /**
     * 删除表中内容
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据id查找
     *
     * @param id
     * @return
     */
    @Select("SELECT * FROM setmeal WHERE id = #{id}")
    Setmeal getById(Long id);

    /**
     * 修改套餐
     * @param setmeal
     */
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    /**
     * 根据 categoryId 查找
     * @param categoryId
     * @return
     */
    @Select("""
            SELECT * FROM setmeal WHERE category_id = #{categoryId}
            """)
    List<Setmeal> findByCategoryId(Long categoryId);
}
