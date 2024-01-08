package com.moon.controller.user;


import com.moon.entity.Setmeal;
import com.moon.result.Result;
import com.moon.service.DishService;
import com.moon.service.SetmealService;
import com.moon.vo.DishItemVO;
import com.moon.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据 categoryId 查询对应 setmeal
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    public Result<List<Setmeal>> list(@RequestParam Long categoryId) {
        log.info("获取分类 {} 的套餐列表", categoryId);
        List<Setmeal> setmeals = setmealService.findList(categoryId);
        return Result.success(setmeals);
    }

    /**
     * 根据setmeal id查找对应信息
     * @param setmealId
     * @return
     */
    @GetMapping("/dish/{id}")
    public Result<List<DishItemVO>> findDish(@PathVariable("id") Long setmealId) {
        log.info("查找套餐对应菜品 : {}", setmealId);
        List<DishItemVO> dishes = setmealService.findDishItems(setmealId);
        return Result.success(dishes);
    }
}
