package com.moon.service;

import com.moon.vo.BusinessDataVO;
import com.moon.vo.DishOverViewVO;
import com.moon.vo.OrderOverViewVO;
import com.moon.vo.SetmealOverViewVO;

import java.time.LocalDate;

public interface WorkspaceService {


    /**
     * 查询今日运营数据
     */
    BusinessDataVO business(LocalDate beginDate, LocalDate endDate);

    /**
     * 查询订单管理数据
     */
    OrderOverViewVO order();

    /**
     * 查询菜品管理数据
     */
    DishOverViewVO dish();

    /**
     * 查询订单管理数据
     */
    SetmealOverViewVO setmeal();
}
