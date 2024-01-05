package com.moon.service;

public interface ShopService {

    /**
     * 设置店铺状态
     * @param status
     */
    void setStatus(Integer status);

    /**
     * 获取店铺状态
     * @return
     */
    Integer getStatus();
}
