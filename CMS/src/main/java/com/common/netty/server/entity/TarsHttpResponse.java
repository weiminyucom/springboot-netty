package com.common.netty.server.entity;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.util.CharsetUtil;
import lombok.Data;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author weimin
 * @ClassName TarsHttpResponse
 * @Description TODO
 * @date 2021/1/8 16:54
 */
@Data
public class TarsHttpResponse {
    private Integer status = 200;
    private Map<String, String> header = new HashMap<>();
    private byte[] body;
    private String version = "HTTP/1.1";

    public static TarsHttpResponse convertToObject(String trim){
        TarsHttpResponse response = new TarsHttpResponse();
        String[] split = trim.split("\r\n\r\n");
        if(split.length > 1){
            response.setBody(split[1].getBytes());
        }
        return response;
    }
}
