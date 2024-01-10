package com.moon.task;

import com.alibaba.fastjson.JSONObject;
import com.moon.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class WebSocketTask {
    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * 通过WebSocket每隔5秒向客户端发送消息
     */
//    @Scheduled(cron = "0/5 * * * * ?")
    public void sendMessageToClient() {
        webSocketServer.sendToAllClient("这是来自服务端的消息：" + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()));
    }


//    @Scheduled(cron = "0/5 * * * * ?")
    public void testJsonMessageToClient() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", 1);
        jsonObject.put("orderId", 15);
        jsonObject.put("content", "您有新订单");

        webSocketServer.sendToAllClient(jsonObject.toString());
    }
}
