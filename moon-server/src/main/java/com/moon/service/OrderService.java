package com.moon.service;

import com.moon.dto.*;
import com.moon.result.PageResult;
import com.moon.vo.OrderPaymentVO;
import com.moon.vo.OrderStatisticsVO;
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

    /**
     * 服务端根据条件查询
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult search(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 统计各类型订单数
     * @return
     */
    OrderStatisticsVO statistics();


    /**
     * 接单
     * @param ordersConfirmDTO
     */
    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * 拒单
     * @param ordersRejectionDTO
     */
    void rejection(OrdersRejectionDTO ordersRejectionDTO);

    /**
     * 商家取消订单
     * @param ordersCancelDTO
     */
    void cancelByAdmin(OrdersCancelDTO ordersCancelDTO);

    /**
     * 派送订单
     * @param id
     */
    void delivery(Long id);

    /**
     * 完成订单
     * @param id
     */
    void complete(Long id);

    /**
     * 催单
     * @param id
     */
    void reminder(Long id);
}
