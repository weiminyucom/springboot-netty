package com.common.netty.server.decoder;

import com.common.netty.server.entity.TarsHttpResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author weimin
 * @ClassName SocketEncoder
 * @Description TODO
 * @date 2021/1/8 16:53
 */
public class SocketEncoder extends MessageToByteEncoder<TarsHttpResponse> {
    @Override
    protected void encode(ChannelHandlerContext ctx, TarsHttpResponse msg, ByteBuf out) throws Exception {
        System.out.println("====================");
        out.writeBytes(msg.convertToByte());
    }
}
