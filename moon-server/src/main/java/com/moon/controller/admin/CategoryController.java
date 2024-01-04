package com.moon.controller.admin;


import com.moon.dto.CategoryDTO;
import com.moon.dto.CategoryPageQueryDTO;
import com.moon.entity.Category;
import com.moon.result.PageResult;
import com.moon.result.Result;
import com.moon.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    /**
     * 删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    public Result<String> deleteById(@RequestParam Long id) {
        log.info("删除分类 : {}", id);
        categoryService.deleteById(id);
        return Result.success();
    }


    /**
     * 修改分类信息
     * @param categoryDTO
     * @return
     */
    @PutMapping
    public Result<String> editCategory(@RequestBody CategoryDTO categoryDTO) {
        log.info("修改分类 : {}", categoryDTO);
        categoryService.editCategory(categoryDTO);
        return Result.success();
    }

    /**
     * 修改状态
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    public Result<String> changeStatus(
         @PathVariable int status,
         @RequestParam Long id
    ) {
        log.info("修改状态 {} : {}", id, status);
        categoryService.changeStatus(id, status);

        return Result.success();
    }

    /**
     * 分页分类查询
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> findPageByType(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("分类分页模糊查询: {}", categoryPageQueryDTO);
        PageResult pageResult = categoryService.findPageByType(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 增加分类
     * @param categoryDTO
     * @return
     */
    @PostMapping
    public Result<String> addCategory(@RequestBody CategoryDTO categoryDTO) {
        log.info("增加分类: {}", categoryDTO);
        categoryService.addCategory(categoryDTO);
        return Result.success();
    }

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    @GetMapping("/list")
    public Result<List<Category>> list(Integer type){
        List<Category> list = categoryService.findByType(type);
        return Result.success(list);
    }
}
