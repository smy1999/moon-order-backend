package com.moon.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.moon.constant.MessageConstant;
import com.moon.context.BaseContext;
import com.moon.dto.OrdersPaymentDTO;
import com.moon.dto.OrdersSubmitDTO;
import com.moon.entity.*;
import com.moon.exception.AddressBookBusinessException;
import com.moon.exception.OrderBusinessException;
import com.moon.exception.ShoppingCartBusinessException;
import com.moon.mapper.AddressBookMapper;
import com.moon.mapper.OrderMapper;
import com.moon.mapper.OrderDetailMapper;
import com.moon.mapper.ShoppingCartMapper;
import com.moon.mapper.UserMapper;
import com.moon.result.PageResult;
import com.moon.service.OrderService;
import com.moon.utils.WeChatPayUtil;
import com.moon.vo.OrderPaymentVO;
import com.moon.vo.OrderSubmitVO;
import com.moon.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private WeChatPayUtil weChatPayUtil;


    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);
    }

    @Override
    public PageResult queryOrdersPage(Integer page, Integer pageSize, Integer status) {
        PageHelper.startPage(page, pageSize);
        Orders orderCondition = Orders.builder()
                .userId(BaseContext.getCurrentId())
                .status(status)
                .build();
        List<Orders> orders = orderMapper.findByOrder(orderCondition);
        if (orders == null || orders.isEmpty()) {
            return new PageResult(0, null);
        }
        List<OrderVO> orderVOs = new ArrayList<>();
        orders.forEach(order -> {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(order, orderVO);
            List<OrderDetail> details = orderDetailMapper.findByOrderId(order.getId());
            orderVO.setOrderDetailList(details);
            orderVOs.add(orderVO);
        });
        PageInfo pageInfo = new PageInfo(orderVOs);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public OrderVO findById(Long orderId) {

        Orders order = orderMapper.findById(orderId);
        List<OrderDetail> details = orderDetailMapper.findByOrderId(orderId);

        OrderVO vo = new OrderVO();
        BeanUtils.copyProperties(order, vo);
        vo.setOrderDetailList(details);

        return vo;
    }

    @Override
    public void cancel(Long id) {
        Orders orderDB = orderMapper.findById(id);
        if (orderDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        // 如果是这些状态,则不能直接退款
        Integer status = orderDB.getStatus();
        if (status > 2) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        // 待接单取消后,支付状态改为退款
        Orders order = new Orders();
        if (status.equals(Orders.TO_BE_CONFIRMED)) {
            order.setPayStatus(Orders.REFUND);
        }

        order.setId(orderDB.getId());
        order.setCancelReason("用户取消");
        order.setStatus(Orders.CANCELLED);
        order.setCancelTime(LocalDateTime.now());
        orderMapper.update(order);
    }

    @Override
    public void repetition(Long id) {
        Long userId = BaseContext.getCurrentId();
        List<OrderDetail> details = orderDetailMapper.findByOrderId(id);
//        List<ShoppingCart> carts = new ArrayList<>();
//        details.forEach(detail -> {
//            ShoppingCart cart = new ShoppingCart();
//            BeanUtils.copyProperties(detail, cart, "id");
//            cart.setCreateTime(LocalDateTime.now());
//            cart.setUserId(userId);
//            carts.add(cart);
//        });

        List<ShoppingCart> carts = details.stream().map(detail -> {
            ShoppingCart cart = new ShoppingCart();
            BeanUtils.copyProperties(detail, cart, "id");
            cart.setUserId(userId);
            cart.setCreateTime(LocalDateTime.now());
            return cart;
        }).collect(Collectors.toList());

        shoppingCartMapper.addBatch(carts);

    }


    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.findById(userId);

        //调用微信支付接口，生成预支付交易单
//        JSONObject jsonObject = weChatPayUtil.pay(
//                ordersPaymentDTO.getOrderNumber(), //商户订单号
//                new BigDecimal(0.01), //支付金额，单位 元
//                "苍穹外卖订单", //商品描述
//                user.getOpenid() //微信用户的openid
//        );
//
//        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
//            throw new OrderBusinessException("该订单已支付");
//        }
//
//        jsonObject.put("code", "ORDERPAID");
//
//        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
//        vo.setPackageStr(jsonObject.getString("package"));

        paySuccess(ordersPaymentDTO.getOrderNumber());
        OrderPaymentVO vo = new OrderPaymentVO();
        return vo;
    }


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