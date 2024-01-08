package com.moon.controller.user;


import com.moon.result.Result;
import com.moon.service.DishService;
import com.moon.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 根据 categoryId 查询对应 DishVO
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    public Result<List<DishVO>> list(@RequestParam Long categoryId) {
        log.info("获取分类 {} 的菜品列表", categoryId);
        List<DishVO> dishes = dishService.findDishVOByCategoryId(categoryId);
        return Result.success(dishes);
    }
}
