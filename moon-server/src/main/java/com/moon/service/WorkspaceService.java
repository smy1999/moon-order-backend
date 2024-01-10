package com.moon.service;

import com.moon.vo.BusinessDataVO;
import com.moon.vo.DishOverViewVO;
import com.moon.vo.OrderOverViewVO;
import com.moon.vo.SetmealOverViewVO;

public interface WorkspaceService {


    /**
     * 查询今日运营数据
     * @return
     */
    BusinessDataVO business();

    /**
     * 查询订单管理数据
     * @return
     */
    OrderOverViewVO order();

    /**
     * 查询菜品管理数据
     * @return
     */
    DishOverViewVO dish();

    /**
     * 查询订单管理数据
     * @return
     */
    SetmealOverViewVO setmeal();
}
