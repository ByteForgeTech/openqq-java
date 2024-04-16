package cn.byteforge.openqq.ws;

import cn.byteforge.openqq.exception.ErrorCheckException;
import cn.byteforge.openqq.ws.entity.enumerate.OpCode;
import cn.byteforge.openqq.ws.handler.ChainHandler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class EventChannelHandler extends SimpleChannelInboundHandler<Object> {

    private final ChainHandler chainHandler;

    @Setter
    private WebSocketClientHandshaker handshaker;

    @Getter
    private ChannelPromise handshakeFuture;

    public EventChannelHandler(ChainHandler chainHandler) {
        this.chainHandler = chainHandler;
    }

    // 使用 wss 连接时，应该在 added 时就进行挥手（ws 连接时可以在 active 时挥手）
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        this.handshakeFuture = ctx.newPromise();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        if (!this.handshaker.isHandshakeComplete()){
            FullHttpResponse response = (FullHttpResponse) msg;
            this.handshaker.finishHandshake(ctx.channel(), response);
            this.handshakeFuture.setSuccess();
            return;
        }

        if (msg instanceof TextWebSocketFrame) {
            JsonObject json = new Gson().fromJson(((TextWebSocketFrame) msg).text(), JsonObject.class);
            log.debug("Received json message: {}", json);
            try {
                chainHandler.handle(json);
            } catch (ErrorCheckException e) {
                log.error("Error check exception: ", e);
            } catch (Exception e) {
                log.error("Handler exception: ", e);
            }
        } else if (msg instanceof CloseWebSocketFrame){
            log.info("WebSocket client closed with signal");
            ctx.channel().close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("WebSocket connection closed with exception: ", cause);
        ctx.close();
    }

}
