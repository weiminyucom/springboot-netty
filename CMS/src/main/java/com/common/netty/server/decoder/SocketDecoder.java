package com.common.netty.server.decoder;

import com.common.netty.server.NettySocketServer;
import com.common.netty.server.entity.HttpHeaders;
import com.common.netty.server.entity.TarsHttpResponse;
import com.common.netty.server.utils.HttpUtils;
import com.common.util.SpringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * @author weimin
 * @ClassName SocketDecoder
 * @Description TODO
 * @date 2020/6/23 15:07
 */
public class SocketDecoder extends ByteToMessageDecoder {
    /**
     * WebSocket握手的协议前缀
     */
    private final String HTTP_RESPONSE_PREFIX = "HTTP/1.1";

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        String trim = in.toString(CharsetUtil.UTF_8).trim();
        System.out.println(trim);
        String[] split = trim.split("\r\n\r\n");
        String[] split1 = split[0].split("\r\n");
        int count = 0;
        boolean isConn = false;
        for (String s : split1) {
            if (s.startsWith(HttpHeaders.CONTENT_LENGTH)) {
                String[] split2 = s.split(":");
                count = Integer.parseInt(split2[1].trim());
                break;
            }
            if (s.startsWith(HttpHeaders.UPGRADE)) {
                String[] up = s.split(":");
                if (HttpHeaders.UPGRADE.equalsIgnoreCase(up[0]) && "websocket".equalsIgnoreCase(up[1])) {
                    SpringUtils.getBean(NettySocketServer.class).websocketAdd(ctx);
                    in.resetReaderIndex();
                    ctx.pipeline().remove(this.getClass());
                    return;
                }
            }
            if(s.startsWith(HttpHeaders.LONG_CONNECTION_HEAD)){
                isConn = true;
            }
        }
        if(!isConn){
            SpringUtils.getBean(NettySocketServer.class).httpAdd(ctx);
            in.resetReaderIndex();
            ctx.pipeline().remove(this.getClass());
            return;
        }
        in.resetReaderIndex();
        int length = in.readableBytes();
        int beginIndex = in.readerIndex();
        if (count != 0 && split[1].getBytes().length < count) {
            in.readerIndex(beginIndex);
            return;
        }
        in.readerIndex(beginIndex + length);
        ByteBuf otherByteBufRef = in.slice(beginIndex, length);
        otherByteBufRef.retain();
        String s = otherByteBufRef.toString(CharsetUtil.UTF_8);
        if (s.startsWith(HTTP_RESPONSE_PREFIX)) {
            out.add(TarsHttpResponse.convertToObject(s));
            return;
        }
        out.add(HttpUtils.createServletRequest(s));
    }

}
