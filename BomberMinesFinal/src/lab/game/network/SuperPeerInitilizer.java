package lab.game.network;

import lab.game.controller.Controller;
import lab.game.controller.MessageHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;



public class SuperPeerInitilizer extends ChannelInitializer<SocketChannel> {
	private MessageHandler msgHandler;
	private SuperPeerHandler handler;
	public SuperPeerInitilizer(MessageHandler msgHandler) {
		this.msgHandler = msgHandler;
	}
	@Override
	protected void initChannel(SocketChannel arg0) throws Exception {
	
		ChannelPipeline pipeline = arg0.pipeline();
		
		pipeline.addLast("decoder",new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
		pipeline.addLast("encoder",new ObjectEncoder());
		 handler = new SuperPeerHandler();
		handler.setMsgHandler(this.msgHandler);
		this.msgHandler.setSuperHandler(handler);
		pipeline.addLast("handler",handler);
		
		
		
	}
	
	
	
	/**
	 * @return the handler
	 */
	public SuperPeerHandler getHandler() {
		return handler;
	}
	/**
	 * @param handler the handler to set
	 */
	public void setHandler(SuperPeerHandler handler) {
		this.handler = handler;
	}

	
}
