package com.common.netty.server.entity;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author weimin
 * @ClassName TarsHttpRequest
 * @Description TODO
 * @date 2021/1/8 15:55
 */
public class TarsHttpRequest {
    private String method;
    private String url;
    private String version;
    private Map<String,String> header = new HashMap<>();
    private String json;
    private String query;

    public static TarsHttpRequest convertToObject(String trim){
        String[] split = trim.split("\r\n\r\n");
        String[] split1 = split[0].split("\r\n");
        TarsHttpRequest request = new TarsHttpRequest();
        for (int i = 0; i < split1.length; i++) {
            if(i == 0){
                String[] h = split1[0].split("\\s+");
                request.setMethod(h[0]);
                request.setVersion(h[2]);
                if(h[1].contains("?")){
                    String[] split2 = h[1].split("\\?");
                    request.setUrl(split2[0].trim());
                    request.setQuery(split2[1].trim());
                }else {
                    request.setUrl(h[1].trim());
                }
                continue;
            }
            String[] head = split1[i].split(":");
            request.header.put(head[0].trim(),head[1].trim());
        }
        if(split.length > 1){
            request.setJson(split[1]);
        }else {
            request.setJson("");
        }
        return request;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getHeader(String key) {
        return this.header.get(key);
    }

    public Map<String,String> getHeaders() {
        return this.header;
    }

    public void setHeader(String key,String value) {
        this.header.put(key,value);
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
