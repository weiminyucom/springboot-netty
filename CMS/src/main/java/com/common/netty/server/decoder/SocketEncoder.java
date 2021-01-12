package com.common.netty.server.decoder;

import com.common.netty.server.entity.HttpHeaders;
import com.common.netty.server.entity.TarsHttpResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Objects;

/**
 * @author weimin
 * @ClassName SocketEncoder
 * @Description TODO
 * @date 2021/1/8 16:53
 */
public class SocketEncoder extends MessageToByteEncoder<MockHttpServletResponse> {
    @Override
    protected void encode(ChannelHandlerContext ctx, MockHttpServletResponse msg, ByteBuf out) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("HTTP/1.1 ");
        builder.append(msg.getStatus()).append(" ");
        builder.append(HttpStatus.valueOf(msg.getStatus())).append("\r\n");
        for (String name : msg.getHeaderNames()) {
            builder.append(name).append(": ").append(msg.getHeader(name)).append("\r\n");
        }
        builder.append(HttpHeaders.CONNECTION).append(": keep-alive\r\n");
        byte[] contentAsByteArray = msg.getContentAsByteArray();
        builder.append(HttpHeaders.CONTENT_LENGTH).append(": ");
        builder.append(contentAsByteArray.length).append("\r\n");
        builder.append("\r\n");
        byte[] bytes = builder.toString().getBytes(Objects.requireNonNull(msg.getCharacterEncoding()));
        ByteBuf buffer = Unpooled.buffer(contentAsByteArray.length + bytes.length);
        buffer.writeBytes(bytes);
        buffer.writeBytes(contentAsByteArray);
        out.writeBytes(buffer);
    }
}
