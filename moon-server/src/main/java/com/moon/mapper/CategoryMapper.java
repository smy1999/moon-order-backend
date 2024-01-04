package com.moon.mapper;


import com.moon.annotation.AutoFill;
import com.moon.dto.CategoryPageQueryDTO;
import com.moon.entity.Category;
import com.moon.enumeration.OperationType;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {

    /**
     * 添加数据
     * @param category
     */
    @Insert("""
            INSERT INTO category (type, name, sort, status, create_time, update_time, create_user, update_user)
            VALUES (#{type}, #{name}, #{sort}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})
            """)
    @AutoFill(OperationType.INSERT)
    void addCategory(Category category);

    /**
     * 根据名称和类型分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    List<Category> findPage(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 通用更新方法
     * @param category
     */
    @AutoFill(OperationType.UPDATE)
    void updateCategory(Category category);

    /**
     * 删除分类
     * @param id
     */
    @Delete("DELETE FROM category WHERE id = #{id}")
    void deleteCategory(Long id);
}
