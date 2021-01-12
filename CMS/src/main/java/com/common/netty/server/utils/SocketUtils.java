package com.common.netty.server.utils;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author weimin
 * @ClassName SocketUtils
 * @Description TODO
 * @date 2021/1/11 17:50
 */
public class SocketUtils {
    private static Map<String, Channel> socketMap = new ConcurrentHashMap<>();

    public static void put(String id,Channel channel){
        socketMap.put(id,channel);
    }

    public static Channel get(String id){
        return socketMap.get(id);
    }

    public static void sendSocketMessage(String id,Object o){
        Channel channel = get(id);
        if(channel != null){
            // 发送消息
        }
    }
}
