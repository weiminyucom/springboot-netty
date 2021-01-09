package com.common.netty.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author weimin
 * @ClassName WebSocketTimeoutHandler
 * @Description TODO
 * @date 2020/8/11 10:33
 */

public class WebSocketTimeoutHandler extends ReadTimeoutHandler {
    public WebSocketTimeoutHandler(long timeout, TimeUnit unit) {
        super(timeout, unit);
    }

    @Override
    protected void readTimedOut(ChannelHandlerContext ctx) throws Exception {
        super.readTimedOut(ctx);
    }
}
