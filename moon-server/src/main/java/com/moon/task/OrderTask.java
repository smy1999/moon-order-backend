package com.moon.task;


import com.moon.dto.OrdersPageQueryDTO;
import com.moon.entity.Orders;
import com.moon.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 每分钟检查未支付订单,超时取消
     */
    @Scheduled(cron = "0 0/1 * * * ?")
//    @Scheduled(cron = "0/5 * * * * *")
    public void checkPendingPayment() {
        log.info("每分钟检查未支付订单,超时取消 {}", new Date());
        OrdersPageQueryDTO condition = new OrdersPageQueryDTO();
        condition.setStatus(Orders.PENDING_PAYMENT);
        condition.setEndTime(LocalDateTime.now().minusMinutes(15));
        List<Orders> orders = orderMapper.findByOrder(condition);
        if (orders == null || orders.isEmpty()) {
            return ;
        }
        orders.forEach(order -> {
            System.out.println(order);
            Orders updateOrder = Orders.builder()
                    .id(order.getId())
                    .status(Orders.CANCELLED)
                    .cancelReason("超时未支付")
                    .cancelTime(LocalDateTime.now())
                    .build();
            orderMapper.update(updateOrder);
        });
    }

    /**
     * 每天检查配送中订单,超时完成
     */
    @Scheduled(cron = "0 0 1 * * ?")
//    @Scheduled(cron = "1/5 * * * * *")
    public void checkDeliveryInProcess() {
        log.info("每天检查配送中订单,超时完成 {}", new Date());
        OrdersPageQueryDTO condition = new OrdersPageQueryDTO();
        condition.setStatus(Orders.DELIVERY_IN_PROGRESS);
        condition.setEndTime(LocalDateTime.now().minusHours(1));
        List<Orders> orders = orderMapper.findByOrder(condition);
        if (orders == null || orders.isEmpty()) {
            return ;
        }
        orders.forEach(order -> {
            System.out.println(order);
            Orders updateOrder = Orders.builder()
                    .id(order.getId())
                    .status(Orders.COMPLETED)
                    .build();
            orderMapper.update(updateOrder);
        });
    }
}
