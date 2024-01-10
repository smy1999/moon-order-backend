package com.moon.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="moon.baidu")
@Data
public class BaiduProperties {

    private String address;
    private String ak;
}
