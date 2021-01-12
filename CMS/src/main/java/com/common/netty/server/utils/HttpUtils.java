package com.common.netty.server.utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.CharsetUtil;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

/**
 * @author weimin
 * @ClassName HttpUtils
 * @Description TODO
 * @date 2021/1/8 17:50
 */
public class HttpUtils {

    public static MockHttpServletRequest createServletRequest(String request) {
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();

        String[] requestList = request.split("\r\n\r\n");
        String[] headers = requestList[0].split("\r\n");
        String[] paths = headers[0].split("\\s+");

        servletRequest.setMethod(paths[0].trim());

        String url = paths[1].trim();

        UriComponents uriComponents = UriComponentsBuilder.fromUriString(url).build();

        servletRequest.setRequestURI(uriComponents.getPath());
        servletRequest.setPathInfo(uriComponents.getPath());


        servletRequest.setRequestURI(paths[1].trim());
        servletRequest.setPathInfo(paths[1].trim());


        for (int i = 1; i < headers.length; i++) {
            String[] head = headers[i].split(":");
            servletRequest.addHeader(head[0].trim(), head.length > 1 ? head[1].trim() : "");
        }

        servletRequest.setContent(requestList[1].trim().getBytes(CharsetUtil.UTF_8));

        if (uriComponents.getQuery() != null) {
            servletRequest.setQueryString(uriComponents.getQuery());
        }

        for (Map.Entry<String, List<String>> entry : uriComponents.getQueryParams().entrySet()) {
            for (String value : entry.getValue()) {
                servletRequest.addParameter(
                        UriUtils.decode(entry.getKey(), "UTF-8"),
                        UriUtils.decode(value, "UTF-8"));
            }
        }

        return servletRequest;
    }

    public static MockHttpServletRequest createServletRequest(FullHttpRequest fullHttpRequest) {
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(fullHttpRequest.uri()).build();

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setRequestURI(uriComponents.getPath());
        servletRequest.setPathInfo(uriComponents.getPath());
        servletRequest.setMethod(fullHttpRequest.method().name());

        if (uriComponents.getScheme() != null) {
            servletRequest.setScheme(uriComponents.getScheme());
        }
        if (uriComponents.getHost() != null) {
            servletRequest.setServerName(uriComponents.getHost());
        }
        if (uriComponents.getPort() != -1) {
            servletRequest.setServerPort(uriComponents.getPort());
        }

        for (String name : fullHttpRequest.headers().names()) {
            servletRequest.addHeader(name, fullHttpRequest.headers().get(name));
        }


        ByteBuf bbContent = fullHttpRequest.content();
        String s = bbContent.toString(CharsetUtil.UTF_8);
        servletRequest.setContent(s.getBytes(CharsetUtil.UTF_8));


        if (uriComponents.getQuery() != null) {
            String query = UriUtils.decode(uriComponents.getQuery(), "UTF-8");
            servletRequest.setQueryString(query);
        }

        for (Map.Entry<String, List<String>> entry : uriComponents.getQueryParams().entrySet()) {
            for (String value : entry.getValue()) {
                servletRequest.addParameter(
                        UriUtils.decode(entry.getKey(), "UTF-8"),
                        UriUtils.decode(value, "UTF-8"));
            }
        }

        return servletRequest;
    }

    public static String getIpAndPort(ChannelHandlerContext ctx) {
        InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = socketAddress.getAddress().getHostAddress();
        String clientPort = String.valueOf(socketAddress.getPort());
        return clientIp + ":" + clientPort;
    }

}
