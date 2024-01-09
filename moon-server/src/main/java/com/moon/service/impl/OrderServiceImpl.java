package com.moon.service.impl;

import com.moon.constant.MessageConstant;
import com.moon.context.BaseContext;
import com.moon.dto.OrdersSubmitDTO;
import com.moon.entity.*;
import com.moon.exception.AddressBookBusinessException;
import com.moon.exception.ShoppingCartBusinessException;
import com.moon.mapper.AddressBookMapper;
import com.moon.mapper.OrderMapper;
import com.moon.mapper.OrderDetailMapper;
import com.moon.mapper.ShoppingCartMapper;
import com.moon.mapper.UserMapper;
import com.moon.service.OrderService;
import com.moon.vo.OrderSubmitVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
//    @Autowired
//    private UserMapper userMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;


    @Override
    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {

        // 处理业务异常
        AddressBook address = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (address == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCartUserId = ShoppingCart.builder().id(userId).build();
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.find(shoppingCartUserId);
        if (shoppingCarts == null || shoppingCarts.isEmpty()) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        // 查找对应信息，加入 orders 表
        Orders order = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, order);
        order.setNumber(String.valueOf(System.currentTimeMillis()));
        order.setUserId(userId);
        order.setOrderTime(LocalDateTime.now());
        order.setStatus(Orders.PENDING_PAYMENT);
        order.setPayStatus(Orders.UN_PAID);

//        User user = userMapper.findById(userId);
//        order.setUserName(user.getName());

        order.setPhone(address.getPhone());
        order.setConsignee(address.getConsignee());
        order.setAddress(address.getProvinceName() + address.getCityName() + address.getDistrictName() + address.getDetail());

        orderMapper.add(order);

        // 商品从shopping_cart中查找,存入order_detail
        List<OrderDetail> orderDetails = new ArrayList<>();
        Long orderId = order.getId();
        shoppingCarts.forEach(shoppingCart -> {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(shoppingCart, orderDetail);
            orderDetail.setOrderId(orderId);
            orderDetails.add(orderDetail);
        });
        orderDetailMapper.addBatch(orderDetails);

        shoppingCartMapper.deleteByUserId(userId);

        OrderSubmitVO result = OrderSubmitVO.builder()
                .id(orderId)
                .orderAmount(order.getAmount())
                .orderTime(order.getOrderTime())
                .orderNumber(order.getNumber())
                .build();
        return result;
    }
}