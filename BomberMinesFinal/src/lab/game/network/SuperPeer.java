
package lab.game.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

import lab.game.controller.Controller;
import lab.game.controller.MessageHandler;
import lab.game.utility.Constants;
import lab.game.utility.Message;

/**
 * Echoes back any received data from a client.
 */
public class SuperPeer extends Thread {

    private final int port;
    //private Controller controller;
    private MessageHandler msgHandler;
    private static final Logger logger = Constants.getLogger(SuperPeer.class.getName());
    EventLoopGroup bossGroup ;
    EventLoopGroup workerGroup;
    Channel channel ;
    SuperPeerInitilizer supPeerInit;
    public SuperPeer(int port, Controller controller) {
        this.port = port;
        //this.controller = controller;
        this.msgHandler = new MessageHandler(controller);
        this.supPeerInit = new SuperPeerInitilizer(msgHandler);
        logger.setLevel(Constants.getLogLevel());
    }

    public void run() {
        // Configure the server.
         bossGroup = new NioEventLoopGroup(1);
         workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .option(ChannelOption.SO_BROADCAST,true)
             .option(ChannelOption.SO_KEEPALIVE, true)
             .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(supPeerInit);

            // Start the server.
            ChannelFuture f = b.bind(port).sync();

            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
            
        } catch (InterruptedException e) {
        	logger.log(Level.SEVERE,e.getMessage());
		} finally {
			logger.log(Level.INFO,"Gracefull Shutdown Super peer .....");
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
    
    public void shutDownGracefully(){
 	   logger.log(Level.INFO,"Gracefull Shutdown Super peer .....");
        // Shut down all event loops to terminate all threads.
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
    
    public void sendPlayerPos(Message playerPostion){
    	
    	this.supPeerInit.getHandler().sendPlayerPos(playerPostion,null);
    	
    }
   
    public void placeBomb(Message message){
    	
    	this.supPeerInit.getHandler().sendBombPos(message,null);
    	
    }
    
    public void sendRegionChange(Message message){
    	
    	this.supPeerInit.getHandler().sendRegionChange(message,null);
    	
    }
}
