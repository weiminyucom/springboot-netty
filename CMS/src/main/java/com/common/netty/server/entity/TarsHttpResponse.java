package com.common.netty.server.entity;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.util.CharsetUtil;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author weimin
 * @ClassName TarsHttpResponse
 * @Description TODO
 * @date 2021/1/8 16:54
 */
@Data
public class TarsHttpResponse<T> {
    private Integer status = 200;
    private Map<String, String> header;
    private T body;
    private String version = "HTTP/1.1";
    private final static String CONTENT_LENGTH = "Content-Length";

    public static TarsHttpResponse convertToObject(String trim){
        TarsHttpResponse response = new TarsHttpResponse();
        String[] split = trim.split("\r\n\r\n");
        if(split.length > 1){
            response.setBody(split[1]);
        }
        return response;
    }

    public ByteBuf convertToByte() {
        StringBuilder builder = new StringBuilder();
        builder.append(version).append(" ");
        builder.append(status).append(" ");
        builder.append(status == 200 ? "OK" : "ERROR").append("\r\n");
        if(header != null && header.size() > 0){
            header.forEach((k,v)->{
                if(CONTENT_LENGTH.equals(k)){
                    return;
                }
                builder.append(k).append(": ").append(v).append("\r\n");
            });
        }
        builder.append(CONTENT_LENGTH).append(": ");
        String s = JSONObject.toJSONString(this.body);
        builder.append(s.getBytes(CharsetUtil.UTF_8).length).append("\r\n");
        builder.append("\r\n");
        builder.append(body);
        return Unpooled.copiedBuffer(builder.toString().getBytes(CharsetUtil.UTF_8));
    }
}
