package com.moon.mapper;

import com.moon.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderDetailMapper {

    /**
     * 批量添加
     * @param orderDetails
     */
    void addBatch(List<OrderDetail> orderDetails);

    /**
     * 根据order id 查询
     *
     * @param orderId
     * @return
     */
    @Select("SELECT * FROM order_detail WHERE order_id = #{orderId}")
    List<OrderDetail> findByOrderId(Long orderId);
}
