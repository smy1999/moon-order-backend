package com.moon.controller.user;


import com.moon.dto.OrdersPaymentDTO;
import com.moon.dto.OrdersSubmitDTO;
import com.moon.result.PageResult;
import com.moon.result.Result;
import com.moon.service.OrderService;
import com.moon.vo.OrderPaymentVO;
import com.moon.vo.OrderSubmitVO;
import com.moon.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/user/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 再来一单业务
     * @param id
     * @return
     */
    @PostMapping("/repetition/{id}")
    public Result<String> repetition(@PathVariable Long id) {
        log.info("再来一单 : {}", id);
        orderService.repetition(id);
        return Result.success();
    }

    /**
     * 取消订单
     * @param id
     * @return
     */
    @PutMapping("/cancel/{id}")
    public Result<String> cancel(@PathVariable Long id) {
        log.info("取消订单 : {}", id);
        orderService.cancel(id);
        return Result.success();
    }

    /**
     * 根据id查询订单具体信息
     * @param id
     * @return
     */
    @GetMapping("/orderDetail/{id}")
    public Result<OrderVO> orderDetail(@PathVariable Long id) {
        log.info("查询订单具体信息 : {}", id);
        OrderVO vo = orderService.findById(id);
        return Result.success(vo);
    }


    /**
     * 查询历史订单
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    @GetMapping("/historyOrders")
    public Result<PageResult> historyOrders(Integer page, Integer pageSize, Integer status) {
        log.info("查询历史订单 {} {} {}", page, pageSize, status);
        PageResult data = orderService.queryOrdersPage(page, pageSize, status);
        return Result.success(data);
    }



    /**
     * 提交订单
     * @param ordersSubmitDTO
     * @return
     */
    @PostMapping("/submit")
    public Result<OrderSubmitVO> submitOrder(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("提交订单 {}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }


    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }
}
