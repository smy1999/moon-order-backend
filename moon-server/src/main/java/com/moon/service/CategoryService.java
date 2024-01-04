package com.moon.service;


import com.moon.dto.CategoryDTO;
import com.moon.dto.CategoryPageQueryDTO;
import com.moon.entity.Category;
import com.moon.result.PageResult;

import java.util.List;

public interface CategoryService {
    /**
     * 增加分类
     * @param categoryDTO
     */
    void addCategory(CategoryDTO categoryDTO);


    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult findPageByType(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 修改状态
     * @param id
     * @param status
     */
    void changeStatus(Long id, int status);

    /**
     * 修改分类
     * @param categoryDTO
     */
    void editCategory(CategoryDTO categoryDTO);

    /**
     * 删除分类
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    List<Category> findByType(Integer type);
}
