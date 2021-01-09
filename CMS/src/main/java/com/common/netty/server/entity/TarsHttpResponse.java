package com.common.netty.server.entity;

import com.alibaba.fastjson.JSONObject;
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
public class TarsHttpResponse<T> {
    private Integer status = 200;
    private Map<String, String> header = new HashMap<>();
    private T body;
    private String version = "HTTP/1.1";

    public static TarsHttpResponse<String> convertToObject(String trim){
        TarsHttpResponse<String> response = new TarsHttpResponse<String>();
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
                if(HttpHeaders.CONTENT_LENGTH.equals(k)){
                    return;
                }
                builder.append(k).append(": ").append(v).append("\r\n");
            });
        }
        builder.append(HttpHeaders.CONNECTION).append(": keep-alive\n");
        builder.append(HttpHeaders.CONTENT_LENGTH).append(": ");
        String s = JSONObject.toJSONString(this.body);
        if(StringUtils.isEmpty(s) || "\"\"".equals(s)){
            builder.append("0\r\n");
        }else {
            builder.append(s.getBytes(CharsetUtil.UTF_8).length).append("\r\n");
            builder.append("\r\n");
            builder.append(s);
        }
        return Unpooled.copiedBuffer(builder.toString().getBytes(CharsetUtil.UTF_8));
    }

    public void setHeader(MockHttpServletResponse response) {
        for (String name : response.getHeaderNames()) {
            this.header.put(name, response.getHeader(name));
        }
    }
}
