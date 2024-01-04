package com.moon.controller.admin;


import com.moon.dto.DishDTO;
import com.moon.dto.DishPageQueryDTO;
import com.moon.entity.Dish;
import com.moon.result.PageResult;
import com.moon.result.Result;
import com.moon.service.DishService;
import com.moon.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @GetMapping("/list")
    public Result<List<Dish>> findByCategoryId(@RequestParam Long categoryId) {
        log.info("根据分类id查找 {}", categoryId);
        List<Dish> data = dishService.findByCategoryId(categoryId);
        return Result.success(data);
    }


    /**
     * 修改状态
     * @param status
     * @return
     */
    @PostMapping("/status/{status}")
    public Result<String> changeStatus(@RequestParam Long id, @PathVariable Integer status) {
        log.info("修改状态: {}", status);
        dishService.changeStatus(id, status);
        return Result.success();
    }


    /**
     * 修改菜品
     * @param dishDTO
     * @return
     */
    @PutMapping
    public Result<String> update(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品: {}", dishDTO);
        dishService.updateDish(dishDTO);
        return Result.success();
    }

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<DishVO> findById(@PathVariable Long id) {
        log.info("查找菜品 : {}", id);
        DishVO dishVO = dishService.findById(id);
        return Result.success(dishVO);
    }


    /**
     * 批量删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<String> deleteBatch(@RequestParam List<Long> ids) {
        log.info("批量删除: {}", ids);
        dishService.deleteBatch(ids);
        return Result.success();
    }


    /**
     * 分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> findPage(DishPageQueryDTO dishPageQueryDTO) {
        log.info("分页查询菜品 {}", dishPageQueryDTO);
        PageResult page = dishService.findPage(dishPageQueryDTO);
        return Result.success(page);
    }

    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @PostMapping
    public Result<String> addDish(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品: {}", dishDTO);
        dishService.addDish(dishDTO);
        return Result.success();
    }

}
