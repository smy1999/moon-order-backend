package com.moon.service;

import com.moon.dto.OrdersPaymentDTO;
import com.moon.dto.OrdersSubmitDTO;
import com.moon.vo.OrderPaymentVO;
import com.moon.vo.OrderSubmitVO;
import org.springframework.beans.factory.annotation.Autowired;

public interface OrderService {



    /**
     * 提交订单
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);


    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);
}
