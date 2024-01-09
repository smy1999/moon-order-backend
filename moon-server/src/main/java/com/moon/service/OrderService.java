package com.moon.service;

import com.moon.dto.OrdersSubmitDTO;
import com.moon.vo.OrderSubmitVO;
import org.springframework.beans.factory.annotation.Autowired;

public interface OrderService {



    /**
     * 提交订单
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);
}
