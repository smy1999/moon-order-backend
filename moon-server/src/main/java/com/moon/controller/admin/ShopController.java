package com.moon.controller.admin;


import com.moon.constant.StatusConstant;
import com.moon.result.Result;
import com.moon.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@Slf4j
@RequestMapping("/admin/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;

    /**
     * 设置店铺状态
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    public Result<String> setStatus(@PathVariable Integer status) {
        log.info("修改状态: {}", status.equals(StatusConstant.ENABLE) ? "营业中" : "打烊中");
        shopService.setStatus(status);
        return Result.success();
    }

    /**
     * 获取店铺状态
     * @return
     */
    @GetMapping("/status")
    public Result<Integer> getStatus() {
        Integer status = shopService.getStatus();
        log.info("服务端获取状态 : {}", status == 1 ? "营业中" : "打烊中");
        return Result.success(status);
    }
}
