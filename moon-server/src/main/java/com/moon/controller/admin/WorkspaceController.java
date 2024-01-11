package com.moon.controller.admin;


import com.moon.result.Result;
import com.moon.service.WorkspaceService;
import com.moon.vo.BusinessDataVO;
import com.moon.vo.DishOverViewVO;
import com.moon.vo.OrderOverViewVO;
import com.moon.vo.SetmealOverViewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/admin/workspace")
@Slf4j
public class WorkspaceController {


    @Autowired
    private WorkspaceService workspaceService;



    /**
     * 查询套餐管理数据
     */
    @GetMapping("/overviewSetmeals")
    public Result<SetmealOverViewVO> setmeal() {
        log.info("查询套餐管理数据");
        SetmealOverViewVO vo =  workspaceService.setmeal();
        return Result.success(vo);
    }


    /**
     * 查询菜品管理数据
     */
    @GetMapping("/overviewDishes")
    public Result<DishOverViewVO> dish() {
        log.info("查询菜品管理数据");
        DishOverViewVO vo =  workspaceService.dish();
        return Result.success(vo);
    }


    /**
     * 查询订单管理数据
     */
    @GetMapping("/overviewOrders")
    public Result<OrderOverViewVO> order() {
        log.info("查询订单管理数据");
        OrderOverViewVO vo =  workspaceService.order();
        return Result.success(vo);
    }

    /**
     * 查询今日运营数据
     */
    @GetMapping("/businessData")
    public Result<BusinessDataVO> businessData() {
        log.info("查询今日运营数据");
        LocalDate today = LocalDate.now();
        BusinessDataVO vo =  workspaceService.business(today, today);
        return Result.success(vo);
    }
}
