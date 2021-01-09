package com.common.netty.server.handler;

import com.common.netty.server.entity.HttpHeaders;
import com.common.netty.server.entity.TarsHttpRequest;
import com.common.netty.server.entity.TarsHttpResponse;
import com.common.netty.server.utils.HttpUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.stream.ChunkedStream;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @author weimin
 * @ClassName ConnectionHandler
 * @Description TODO
 * @date 2020/6/23 14:56
 */
@Component
@Slf4j
@ChannelHandler.Sharable
public class AppHandler extends SimpleChannelInboundHandler<Object> {

    private WebSocketServerHandshaker handShaker;

    @Autowired
    private DispatcherServlet dispatcherServlet;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        try {
            if (msg instanceof TarsHttpRequest){
                TarsHttpRequest tarsHttpRequest = (TarsHttpRequest)msg;
                MockHttpServletRequest mockHttpServletRequest = HttpUtils.createServletRequest(tarsHttpRequest);
                MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
                dispatcherServlet.service(mockHttpServletRequest,mockHttpServletResponse);
                byte[] contentAsByteArray = mockHttpServletResponse.getContentAsByteArray();
                System.out.println(contentAsByteArray.length);
                TarsHttpResponse<byte[]> response = new TarsHttpResponse<>();
                response.setBody(contentAsByteArray);
                response.setHeader(mockHttpServletResponse);
                ctx.writeAndFlush(response);
            }
            if(msg instanceof FullHttpRequest){
                FullHttpRequest fullHttpRequest = (FullHttpRequest)msg;
                MockHttpServletRequest mockHttpServletRequest = HttpUtils.createServletRequest(fullHttpRequest);
                MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();

                dispatcherServlet.service(mockHttpServletRequest,mockHttpServletResponse);
                ByteBuf byteBuf = Unpooled.wrappedBuffer(mockHttpServletResponse.getContentAsByteArray());

                HttpResponseStatus status = HttpResponseStatus.valueOf(mockHttpServletResponse.getStatus());

                FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,byteBuf);
                for (String name : mockHttpServletResponse.getHeaderNames()) {
                    response.headers().add(name, mockHttpServletResponse.getHeader(name));
                }
                response.headers().add(HttpHeaders.CONTENT_LENGTH,byteBuf.readableBytes());
                ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            }
            if (msg instanceof WebSocketFrame) {
                if (msg instanceof CloseWebSocketFrame) {
                    CloseWebSocketFrame close = ((CloseWebSocketFrame) msg).retain();
                    handShaker.close(ctx.channel(), close);
                    return;
                }
                if (msg instanceof TextWebSocketFrame) {
                    TextWebSocketFrame frame = (TextWebSocketFrame) msg;
                    System.out.println(frame.text());
                }
            }
        }catch (Exception e) {
            log.error("Netty 数据传输异常,ip:port={}",HttpUtils.getIpAndPort(ctx), e);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("{}连接异常",HttpUtils.getIpAndPort(ctx), cause);
        ctx.close();
    }

}
