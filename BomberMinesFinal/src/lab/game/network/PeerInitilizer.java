package lab.game.network;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lab.game.controller.MessageHandler;



public class PeerInitilizer extends ChannelInitializer<SocketChannel> {

	private MessageHandler handler;
	
	public PeerInitilizer(MessageHandler handler) {
		this.handler = handler;
	}

	@Override
	protected void initChannel(SocketChannel arg0) throws Exception {
		ChannelPipeline pipeline = arg0.pipeline();
		pipeline.addLast("decoder",new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
		pipeline.addLast("encoder",new ObjectEncoder());
		PeerHandler handler = new PeerHandler();
		handler.setMsgHandler(this.handler);
		
		pipeline.addLast("handler",handler);
		
	}

}
