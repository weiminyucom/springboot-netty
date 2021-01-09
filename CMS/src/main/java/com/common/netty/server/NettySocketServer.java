package com.common.netty.server;

import com.common.netty.server.decoder.SocketDecoder;
import com.common.netty.server.decoder.SocketEncoder;
import com.common.netty.server.handler.ChannelInboundHandler;
import com.common.netty.server.handler.WebSocketHandler;
import com.common.netty.server.handler.WebSocketTimeoutHandler;
import com.common.util.SpringUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketFrameAggregator;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

/**
 * @author weimin
 * @ClassName NettySocketServer
 * @Description TODO
 * @date 2020/6/23 14:53
 */
@Component
@Slf4j
public class NettySocketServer {

    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();
    private ServerBootstrap serverBootstrap = new ServerBootstrap();
    private ChannelFuture future;
    private com.common.netty.server.handler.ChannelInboundHandler channelInboundHandler;
    private WebSocketHandler webSocketHandler;

    @Value("${netty.server.port}")
    private Integer port;
    @Value("${netty.server.timeout}")
    private long timeout;
    @Value("${netty.server.ip}")
    private String ip;

    @Autowired
    public NettySocketServer(ChannelInboundHandler channelInboundHandler, WebSocketHandler webSocketHandler) {
        this.channelInboundHandler = channelInboundHandler;
        this.webSocketHandler = webSocketHandler;
    }


    public void nettyStart(){
        try {
            ServerBootstrap group = serverBootstrap.group(bossGroup, workerGroup);
            group.channel(NioServerSocketChannel.class);
            group.childOption(ChannelOption.SO_REUSEADDR, true);
            group.option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(1024*1024));
            group.option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(512, 1024, 2048 * 1000));
            group.childHandler(new ChannelInitializer<SocketChannel>(){
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast("initConnectionHandler",channelInboundHandler);
                    pipeline.addLast("timeoutHandle",new WebSocketTimeoutHandler(timeout, TimeUnit.SECONDS));
                    pipeline.addLast("socketDecoder",new SocketDecoder());
                    pipeline.addLast("socketEncoder", new SocketEncoder());
                    pipeline.addLast("socketHandler",webSocketHandler);
                }
            });
            future = serverBootstrap.bind(port).sync();
            if(future.isSuccess()){
                log.info("Netty started on port: {} success",port);
            }else {
                log.info("Netty started on port: {} failure",port);
                SpringUtils.stopSpring();
            }
            future.channel().closeFuture().sync();
        }catch (Exception e){
            log.error("Netty error on port: {}",port);
            e.printStackTrace();
            SpringUtils.stopSpring();
        }
    }

    @PreDestroy
    public void nettyStop() {
        if (future != null) {
            future.channel().close().addListener(ChannelFutureListener.CLOSE);
            future.awaitUninterruptibly();
            future = null;
            log.info("Netty Server shut down");
        }
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }


    public void websocketAdd(ChannelHandlerContext ctx) throws Exception {
        ctx.pipeline().addBefore("socketHandler","http-codec",new HttpServerCodec());
        ctx.pipeline().addBefore("socketHandler","aggregator",new HttpObjectAggregator(512*1024));
        ctx.pipeline().addBefore("socketHandler","http-chunked",new ChunkedWriteHandler());
        ctx.pipeline().addBefore("socketHandler","WebSocketAggregator",new WebSocketFrameAggregator(512*1024));
    }

    public void httpAdd(ChannelHandlerContext ctx) throws Exception {
        ctx.pipeline().addBefore("socketHandler","http-codec",new HttpServerCodec());
        ctx.pipeline().addBefore("socketHandler","aggregator",new HttpObjectAggregator(512*1024));
        ctx.pipeline().addBefore("socketHandler","chunked-write",new ChunkedWriteHandler());

    }

    public Integer getPort() {
        return port;
    }


    public String getIp() {
        return ip;
    }

}
