package com.moon.service.impl;

import com.moon.constant.StatusConstant;
import com.moon.entity.Orders;
import com.moon.mapper.DishMapper;
import com.moon.mapper.ReportMapper;
import com.moon.mapper.SetmealMapper;
import com.moon.service.WorkspaceService;
import com.moon.vo.BusinessDataVO;
import com.moon.vo.DishOverViewVO;
import com.moon.vo.OrderOverViewVO;
import com.moon.vo.SetmealOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;


@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private ReportMapper reportMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;



    @Override
    public BusinessDataVO business() {
        LocalDate today = LocalDate.now();
        LocalDateTime begin = LocalDateTime.of(today, LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(today, LocalTime.MAX);

        Map<String, Object> param = new HashMap<>();
        param.put("begin", begin);
        param.put("end", end);

        Integer newUser = reportMapper.sumUserByMap(param);
        Integer totalOrder = reportMapper.sumOrderByMap(param);

        param.put("status", Orders.COMPLETED);
        Integer validOrder = reportMapper.sumOrderByMap(param);
        Double turnover = reportMapper.sumTurnoverByMap(param);

        Double unitPrice = 0.0;
        if (totalOrder != 0) {
            unitPrice = turnover / validOrder;
        }

        Double orderCompletionRate = 0.0;
        if (totalOrder != 0) {
            orderCompletionRate = validOrder.doubleValue() / totalOrder;
        }

        return BusinessDataVO
                .builder()
                .newUsers(newUser)
                .orderCompletionRate(orderCompletionRate)
                .validOrderCount(validOrder)
                .turnover(turnover)
                .unitPrice(unitPrice)
                .build();
    }

    @Override
    public OrderOverViewVO order() {

        Map<String, Object> param = new HashMap<>();
        Integer allOrders = reportMapper.sumOrderByMap(param);


        param.put("status", Orders.TO_BE_CONFIRMED);
        Integer waitingOrders = reportMapper.sumOrderByMap(param);

        param.put("status", Orders.CONFIRMED);
        Integer deliveredOrders = reportMapper.sumOrderByMap(param);

        param.put("status", Orders.COMPLETED);
        Integer completedOrders = reportMapper.sumOrderByMap(param);

        param.put("status", Orders.CANCELLED);
        Integer cancelledOrders = reportMapper.sumOrderByMap(param);


        return OrderOverViewVO.builder()
                .allOrders(allOrders)
                .waitingOrders(waitingOrders)
                .deliveredOrders(deliveredOrders)
                .cancelledOrders(cancelledOrders)
                .completedOrders(completedOrders)
                .build();
    }

    @Override
    public DishOverViewVO dish() {
        Integer discontinued = dishMapper.sumByStatus(StatusConstant.DISABLE);
        Integer sold = dishMapper.sumByStatus(StatusConstant.ENABLE);
        return DishOverViewVO
                .builder()
                .discontinued(discontinued)
                .sold(sold)
                .build();
    }

    @Override
    public SetmealOverViewVO setmeal() {
        Integer discontinued = setmealMapper.sumByStatus(StatusConstant.DISABLE);
        Integer sold = setmealMapper.sumByStatus(StatusConstant.ENABLE);
        return SetmealOverViewVO
                .builder()
                .discontinued(discontinued)
                .sold(sold)
                .build();
    }

}
