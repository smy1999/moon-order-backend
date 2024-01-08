package com.moon.controller.user;


import com.moon.result.Result;
import com.moon.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userShopController")
@Slf4j
@RequestMapping("/user/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;

    /**
     * 获取店铺状态
     * @return
     */
    @GetMapping("/status")
    public Result<Integer> getStatus() {
//        Integer status = shopService.getStatus();
        Integer status = 1;
        log.info("用户端获取状态 : {}", status == 1 ? "营业中" : "打烊中");
        return Result.success(status);
    }
}
