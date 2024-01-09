package com.moon.controller.admin;


import com.moon.dto.OrdersCancelDTO;
import com.moon.dto.OrdersConfirmDTO;
import com.moon.dto.OrdersPageQueryDTO;
import com.moon.dto.OrdersRejectionDTO;
import com.moon.result.PageResult;
import com.moon.result.Result;
import com.moon.service.OrderService;
import com.moon.vo.OrderStatisticsVO;
import com.moon.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Slf4j
public class OrderController {


    @Autowired
    private OrderService orderService;

    /**
     * 完成订单
     * @param id
     * @return
     */
    @PutMapping("/complete/{id}")
    public Result<String> complete(@PathVariable Long id) {
        log.info("完成订单 : {}", id);
        orderService.complete(id);
        return Result.success();
    }



    /**
     * 派送订单
     * @param id
     * @return
     */
    @PutMapping("/delivery/{id}")
    public Result<String> delivery(@PathVariable Long id) {
        log.info("派送订单 : {}", id);
        orderService.delivery(id);
        return Result.success();
    }

    /**
     * 取消订单
     * @param ordersCancelDTO
     * @return
     */
    @PutMapping("/cancel")
    public Result<String> cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) {
        log.info("取消: {}", ordersCancelDTO);
        orderService.cancelByAdmin(ordersCancelDTO);
        return Result.success();
    }


    /**
     * 拒单
     * @param ordersRejectionDTO
     * @return
     */
    @PutMapping("/rejection")
    public Result<String> rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO) {
        log.info("拒单 : {}", ordersRejectionDTO);
        orderService.rejection(ordersRejectionDTO);
        return Result.success();
    }


    /**
     * 接单
     * @param ordersConfirmDTO
     * @return
     */
    @PutMapping("/confirm")
    public Result<String> confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        log.info("接单 : {}", ordersConfirmDTO);
        orderService.confirm(ordersConfirmDTO);
        return Result.success();
    }


    /**
     * 查看订单细节
     * @param id
     * @return
     */
    @GetMapping("/details/{id}")
    public Result<OrderVO> detail(@PathVariable Long id) {
        log.info("查看订单细节 {}", id);
        OrderVO vo = orderService.findById(id);
        return Result.success(vo);
    }


    /**
     * 统计各类型订单数
     * @return
     */
    @GetMapping("/statistics")
    public Result<OrderStatisticsVO> statistics() {
        log.info("统计各类型订单数");
        OrderStatisticsVO orderStatisticsVO = orderService.statistics();
        return Result.success(orderStatisticsVO);
    }


    /**
     * 服务端查询
     * @param ordersPageQueryDTO
     * @return
     */
    @GetMapping("/conditionSearch")
    public Result<PageResult> search(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("服务端查询: {}", ordersPageQueryDTO);
        PageResult data = orderService.search(ordersPageQueryDTO);
        return Result.success(data);
    }

}
