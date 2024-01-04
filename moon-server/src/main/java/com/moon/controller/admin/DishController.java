package com.moon.controller.admin;


import com.moon.dto.CategoryPageQueryDTO;
import com.moon.dto.DishDTO;
import com.moon.dto.DishPageQueryDTO;
import com.moon.result.PageResult;
import com.moon.result.Result;
import com.moon.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/dish")
public class DishController {

    @Autowired
    private DishService dishService;


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
