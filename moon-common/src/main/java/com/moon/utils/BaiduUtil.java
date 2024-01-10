package com.moon.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.moon.exception.OrderBusinessException;
import com.moon.properties.BaiduProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BaiduUtil {

    @Autowired
    private BaiduProperties baiduProperties;

    public static final String GEOCODING_API = "https://api.map.baidu.com/geocoding/v3/";
    public static final String DIRECTION_LITE_API = "https://api.map.baidu.com/directionlite/v1/riding";

    /**
     * 计算客户地址与店铺地址之间的距离是否<500
     * @param address
     * @return
     */
    public boolean validate(String address) {

        String shopLocation = getLocation(baiduProperties.getAddress());
        String userLocation = getLocation(address);

        Integer distance = getDistance(shopLocation, userLocation);

        return distance <= 5000;
    }


    /**
     * 调用百度路径规划,根据输入目的地和出发地的经纬度,计算二者之间的骑行距离
     * @param origin
     * @param destination
     * @return
     */
    private Integer getDistance(String origin, String destination) {
        // 解析地址
        JSONObject originJson = JSON.parseObject(origin);
        JSONObject destinationJson = JSON.parseObject(destination);

        Double originLng = Double.valueOf(originJson.getString("lng"));
        Double originLat = Double.valueOf(originJson.getString("lat"));
        Double destinationLng = Double.valueOf(destinationJson.getString("lng"));
        Double destinationLat = Double.valueOf(destinationJson.getString("lat"));

        String originLngStr = String.format("%.6f", originLng);
        String originLatStr = String.format("%.6f", originLat);
        String destinationLngStr = String.format("%.6f", destinationLng);
        String destinationLatStr = String.format("%.6f", destinationLat);

        // 封装请求参数
        Map<String, String> paraMap = new HashMap<>();
        paraMap.put("origin", originLatStr + "," + originLngStr);
        paraMap.put("destination", destinationLatStr + "," + destinationLngStr);
        paraMap.put("ak", baiduProperties.getAk());
        paraMap.put("steps_info", "0");

        // 请求骑行路径规划API
        String json = HttpClientUtil.doGet(DIRECTION_LITE_API, paraMap);
        if (!JSON.parseObject(json).getString("status").equals("0")) {
            throw new OrderBusinessException("无法到达");
        }

        // 解析请求结果
        String result = JSON.parseObject(json).getString("result");
        String routes = JSON.parseObject(result).getString("routes");
        String element = JSON.parseArray(routes).getString(0);
        Integer distance = JSON.parseObject(element).getInteger("distance");

        return distance;
    }



    /**
     * 传入地址, 调用地理编码, 获取经纬度信息, 返回JSON串
     * @return "location":{"lng":xxx,"lat":xxx}
     */
    private String getLocation(String address) {
        // 封装参数
        Map<String, String> paraMap = new HashMap<>();
        paraMap.put("address", address);
        paraMap.put("ak", baiduProperties.getAk());
        paraMap.put("output", "json");

        // 调用地理编码,发送请求
        String json = HttpClientUtil.doGet(GEOCODING_API, paraMap);
        if (!JSON.parseObject(json).getString("status").equals("0")) {
            throw new OrderBusinessException("地址解析失败");
        }
//        System.out.println(address);
//        System.out.println(json);

        // 解析结果
        String result = JSON.parseObject(json).getString("result");
        String location = JSON.parseObject(result).getString("location");

        return location;
    }



}
