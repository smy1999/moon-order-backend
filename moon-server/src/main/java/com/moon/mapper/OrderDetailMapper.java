package com.moon.mapper;

import com.moon.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderDetailMapper {

    /**
     * 批量添加
     * @param orderDetails
     */
    void addBatch(List<OrderDetail> orderDetails);

}
