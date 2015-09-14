package lab.game.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import lab.game.controller.Controller;
import lab.game.controller.MessageHandler;
import lab.game.gui.Game;
import lab.game.pojo.HostInfo;
import lab.game.utility.Constants;
import lab.game.utility.Message;
import lab.game.utility.Util;


public class Peer extends Thread {

    private String host;
    private int port;
    private boolean isConnected;
    private  Channel channel ; 
    private final static Logger logger = Constants.getLogger(Peer.class.getName());
    private Controller controller;
    EventLoopGroup group ; 
    private MessageHandler msgHandler;
    public Peer(String host, int port, Controller controller) {
        this.host = host;
        this.port = port;
        this.controller = controller;
        this.msgHandler = new MessageHandler(controller);
        logger.setLevel(Constants.getLogLevel());
    }

    public void run() {
         group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioSocketChannel.class)
             .option(ChannelOption.TCP_NODELAY, true)
             .handler(new PeerInitilizer(msgHandler));
            
            channel = b.connect(host, port).sync().channel();
            controller.getPlayer().setConnected(true);
            this.setConnected(true);
            
            Message m = new Message();
            m.setMessageId(Constants.PEER_JOIN);
            m.setPlayerId(this.controller.getPlayer().getPlayerID());
            m.setSuper(Constants.isSuperNeighbor());
            m.setRegionId(Constants.getActiveRegion());
            m.setSuperIp(Constants.IP_ADDRESS);
            m.setSuperPort(Constants.getPort());
			
            channel.writeAndFlush(m);
        } catch (InterruptedException e) {
        	logger.severe(e.getMessage());
			
		} 
    }

    public void sendPlayerPos(Message playerPostion){
    	channel.writeAndFlush(playerPostion);
    }
   
    public void placeBomb(Message message){
    	channel.writeAndFlush(message);
    }
    
    public void shudownGracefully(){
    	group.shutdownGracefully();
    }

	/**
	 * @return the isConnected
	 */
	public boolean isConnected() {
		return isConnected;
	}

	/**
	 * @param isConnected the isConnected to set
	 */
	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}
    
	public void joinNetwork(Message message){
		channel.writeAndFlush(message);
	}
    
	public void sendRegionChange(Message message){
		channel.writeAndFlush(message);
	}

	public MessageHandler getMsgHandler() {
		return msgHandler;
	}

	public void setMsgHandler(MessageHandler msgHandler) {
		this.msgHandler = msgHandler;
	}
	
	
}
