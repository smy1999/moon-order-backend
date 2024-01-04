package com.moon.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.moon.constant.MessageConstant;
import com.moon.constant.StatusConstant;
import com.moon.context.BaseContext;
import com.moon.dto.CategoryDTO;
import com.moon.dto.CategoryPageQueryDTO;
import com.moon.entity.Category;
import com.moon.exception.DeletionNotAllowedException;
import com.moon.mapper.CategoryMapper;
import com.moon.mapper.DishMapper;
import com.moon.mapper.SetmealMapper;
import com.moon.result.PageResult;
import com.moon.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public void addCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);

        category.setStatus(StatusConstant.DISABLE);

        categoryMapper.addCategory(category);

    }

    @Override
    public PageResult findPageByType(CategoryPageQueryDTO categoryPageQueryDTO) {

        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
        List<Category> categories = categoryMapper.findPage(categoryPageQueryDTO);

        PageInfo<Category> pageInfo = new PageInfo<>(categories);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public void changeStatus(Long id, int status) {
        Category category = Category.builder()
                .id(id)
                .status(status)
                .build();
        categoryMapper.updateCategory(category);
    }

    @Override
    public void editCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        categoryMapper.updateCategory(category);
    }

    @Override
    public void deleteById(Long id) {

        //查询当前分类是否关联了菜品，如果关联了就抛出业务异常
        Integer count;
        count = dishMapper.countByCategoryId(id);
        if(count > 0){
            //当前分类下有菜品，不能删除
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }

        //查询当前分类是否关联了套餐，如果关联了就抛出业务异常
        count = setmealMapper.countByCategoryId(id);
        if(count > 0){
            //当前分类下有菜品，不能删除
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }

        categoryMapper.deleteCategory(id);
    }

    @Override
    public List<Category> findByType(Integer type) {
        return categoryMapper.findByType(type);
    }
}
