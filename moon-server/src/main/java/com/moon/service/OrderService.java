package com.moon.service;

import com.moon.dto.OrdersPaymentDTO;
import com.moon.dto.OrdersSubmitDTO;
import com.moon.result.PageResult;
import com.moon.vo.OrderPaymentVO;
import com.moon.vo.OrderSubmitVO;
import com.moon.vo.OrderVO;
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

    /**
     * 分页查询历史订单
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    PageResult queryOrdersPage(Integer page, Integer pageSize, Integer status);

    /**
     * 根据id查询详细信息
     * @param orderId
     * @return
     */
    OrderVO findById(Long orderId);

    /**
     * 取消订单
     * @param id
     */
    void cancel(Long id);

    /**
     * 再来一单
     * @param id
     */
    void repetition(Long id);
}
