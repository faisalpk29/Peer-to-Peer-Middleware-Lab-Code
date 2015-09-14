package lab.game.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

import lab.game.controller.MessageHandler;
import lab.game.entities.Player;
import lab.game.pojo.HostInfo;
import lab.game.utility.Constants;
import lab.game.utility.Message;
import lab.game.utility.Util;

/**
 * 
 */

public class SuperPeerHandler extends SimpleChannelInboundHandler<Message> {

    private static final Logger logger = Constants.getLogger(SuperPeerHandler.class.getName());
    private static  ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE) ;
    private static  ChannelGroup superChannels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE) ;
    private MessageHandler msgHandler;
    private static Queue<HostInfo> existingPeers = new LinkedList<HostInfo>();
    private static Map<Integer,HostInfo> superMap = new HashMap<Integer, HostInfo>();
   // private  LinkedHashMap<String,Integer> backupMap;
    
 public SuperPeerHandler() {
	 
	  
       
        
        
     }
    
    /* (non-Javadoc)
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelActive(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
		super.channelActive(ctx);
	}

	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelUnregistered(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		logger.info("");
		super.channelUnregistered(ctx);
		//this.processPeerLeave(ctx);
	}

	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	Message m = (Message)msg;
    
    	msgHandler.handleMessage(m,ctx.channel(),true);
    	
//    	Channel incoming = ctx.channel();
//    	for (Channel channel: channels) {
//			if (channel!= incoming) {
//				
//				//logger.log(Level.INFO,"sending to other channel ["+channel.remoteAddress()+"]");
//				channel.writeAndFlush(m);
//			}
//				
//		}
       // ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        
        logger.severe(cause.getMessage());
        ctx.close();
    }

    @Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    	logger.info("connected channels : "+channels.size());
    	if (getConnectedPeers()>0) {
    		msgHandler.getControl().getGame().getToolBar().getIsSuper().setText( "Super Peer : Yes");
		}
    	
	}

	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelHandlerAdapter#handlerRemoved(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		
		//logger.info("");
		this.processPeerLeave(ctx.channel(),null);
	}

	/**
	 * @return the msgHandler
	 */
	public MessageHandler getMsgHandler() {
		return msgHandler;
	}

	/**
	 * @param msgHandler the msgHandler to set
	 */
	public void setMsgHandler(MessageHandler msgHandler) {
		this.msgHandler = msgHandler;
		 this.msgHandler.setSuperHandler(this);
	}


	public void sendJoinMessage(Message message,Channel incoming){
		logger.info( "sending join message to others....");
		
		HostInfo info= new HostInfo();
		info.setPlayerId(message.getPlayerId());
		info.setPort(message.getSuperPort());
		info.setSuperIp(message.getSuperIp());
		info.setRemoteIp(incoming.remoteAddress().toString().trim());
		info.setRegionId(message.getRegionId());
		Message msgState = new Message();
		Player plyer = msgHandler.getControl().getPlayer();
		msgState.setMessageId(Constants.STATE_UPDATE);
		//********************************************************************//
		HostInfo myInfo = new HostInfo();
		myInfo.setPlayerId(msgHandler.getControl().getPlayer().getPlayerID());
		myInfo.setRegionId(Constants.getActiveRegion());
		myInfo.setSuperIp(Constants.IP_ADDRESS);
		myInfo.setPort(Constants.getPort());
		
		msgHandler.getControl().getNetwork().getSuperNeighbors().put(Constants.getActiveRegion(), myInfo);
		superMap.put(Constants.getActiveRegion(), myInfo);
		
	//	msgHandler.getControl().getNetwork().getSuperNeighbors().put(Constants.getActiveRegion(), myInfo);
		//logger.info("Super neigbors in State update");
		//Util.traverseSuperNeighbors(msgHandler.getControl().getNetwork().getSuperNeighbors());
		//********************************************************************//
		msgState.setPlayerId(plyer.getPlayerID());
		msgState.setRegionId(msgHandler.getControl().getGame().getActiveRegion().getRegionID());
		
		if (message.getRegionId() == Constants.getActiveRegion()) {
			message.setSuper(false);
		}
		if (message.isSuper()) {
			logger.info("Super neighbour Joined with  RegionId : "+message.getRegionId());
			superMap.put(message.getRegionId(), info);
			msgState.setSuperNeighbors(msgHandler.getControl().getNetwork().getSuperNeighbors());
			//superNeighbors.put(message.getRegionId(), info);
			msgHandler.getControl().getNetwork().getSuperNeighbors().put(message.getRegionId(), info);
			msgState.setSuperIp(Constants.IP_ADDRESS);
        	msgState.setSuperPort(Constants.getPort());
        	msgState.setSuper(message.isSuper());
        	//Send state update only to newly joined peer 
        	
        	
        	incoming.writeAndFlush(msgState);
        	
        	
        	
        	Message msgNu = new Message();
        	msgNu.setMessageId(Constants.NEIGHBOR_UPDATE);
        	msgNu.setNeighbor(info);
        	

        	for (Channel channel : superChannels) {
        		if (channel !=incoming) {
					logger.info(message.getMessageId()+" NU sending to super Neighbors : ");
					channel.writeAndFlush(msgNu);
				}
			}
        	
        	for (Channel channel: channels) {
				if (channel !=incoming) {
					logger.info(message.getMessageId()+"NU sending to ON : ");
					channel.writeAndFlush(msgNu);
				}
						
			}
        	superChannels.add(incoming);
		}else{
			
			logger.info("Ordinary Peer Joined .....");
			channels.add(incoming);
			SuperPeerHandler.existingPeers.add(info);
			//Util.traverseSuperNeighbors(msgHandler.getControl().getNetwork().getSuperNeighbors());
			msgState.setSuperNeighbors(msgHandler.getControl().getNetwork().getSuperNeighbors());
			//msgState.setNeighbor(info);
			msgState.setSuper(false);
			msgState.setExistingPeers(existingPeers);
    		msgState.setxPlayerPos(plyer.getPlayerCurXPos()+10);
    		msgState.setyPlayerPos(plyer.getPlayerCurYPos()+10);
    		msgState.setRegionMap(msgHandler.getControl().getGame().getActiveRegion().getRegionMap());
        	msgState.setSuperIp(Constants.getMyIpAddress());
			incoming.writeAndFlush(msgState);
			
			logger.info("existing peer size : "+existingPeers.size());
			for (Channel channel: channels) {
				if (channel !=incoming) {
					logger.info(message.getMessageId()+"   sending to : "+channel.remoteAddress());
					channel.writeAndFlush(message);
				}
						
			}
		}
		
		
		if (channels.size() <=0) {
			msgHandler.getControl().getGame().getToolBar().getIsSuper().setText( "Super Peer : NO");
		}
		
	}


	public static int getConnectedPeers(){
		return SuperPeerHandler.channels.size();
	}

	public static int getConnectedSuper(){
		return SuperPeerHandler.superChannels.size();
	}
	public void sendPlayerPos(Message message,Channel incoming){
		for (Channel channel: channels) {
			if (!Util.isNull(incoming) & channel !=incoming) {
				channel.writeAndFlush(message);
			}else channel.writeAndFlush(message);
		
			
		}
	}

	
	/**
	 * @return the existingPeers
	 */
	public static Queue<HostInfo> getExistingPeers() {
		return SuperPeerHandler.existingPeers;
	}

	public void sendBombPos(Message message,Channel incoming){
		for (Channel channel: channels) {
			
			if (!Util.isNull(incoming) & channel !=incoming) {
				channel.writeAndFlush(message);
			}else channel.writeAndFlush(message);
		}
	}
	
	public void processPeerLeave(Channel incoming,Message message){
		
		logger.info(""+incoming.remoteAddress());
		
		HostInfo info = new HostInfo();
		for (HostInfo host :SuperPeerHandler.existingPeers) {
			logger.info(host.getPort()+ "  Remote "+host.getRemoteIp() );
			//logger.info(incoming.remoteAddress());
			if (host.getRemoteIp().equals(incoming.remoteAddress().toString().trim())) {
				logger.info("host left : "+host.getPort());
				logger.info("player id : "+host.getPlayerId());
				info = host;
			}
			
		}
		if (SuperPeerHandler.existingPeers.size()> 0) {
			SuperPeerHandler.existingPeers.remove(info);
			logger.info("existing peers size on leave "+SuperPeerHandler.existingPeers.size());
			Message leaveMessage = null;
			if (Util.isNull(message)) {
				 leaveMessage = new Message();
				leaveMessage.setMessageId(Constants.PEER_LEAVE);
				leaveMessage.setPlayerId(info.getPlayerId());
			}else leaveMessage = message;
			
			this.msgHandler.getControl().receivePeerLeave(leaveMessage);
			logger.info("Channels size : "+channels.size());
			for (Channel channel: channels) {
				if (channel!= incoming) {
					logger.info("sending to other channel on gracefull leave  .... "+channel.remoteAddress());
					channel.writeAndFlush(leaveMessage);
				}
				
				
			}
			if (channels.size()>0) {
				channels.remove(incoming);
			}
			
		}
			
			
			if (channels.size() <=0) {
				msgHandler.getControl().getGame().getToolBar().getIsSuper().setText( "Super Peer : NO");
			}
	}
	
	public void neighborUpdate(Message message, Channel incoming) {

		logger.info("received Neighbor Update .....");
		HostInfo neighbor = message.getNeighbor();
		//superNeighbors.put(message.getRegionId(), neighbor);
		msgHandler.getControl().getNetwork().getSuperNeighbors().put(neighbor.getRegionId(), neighbor);
		superMap.put(neighbor.getRegionId(), neighbor);
		for (Channel channel : channels) {
			if (channel != incoming) {
				logger.info(message.getMessageId() + "Neighbor update sending to Normal Peers : ");
				channel.writeAndFlush(message);
			}
		}
	}
	
	public void sendRegionChange(Message message, Channel incoming) {
		if (!Util.isNull(incoming) && superChannels.size() > 0) {
			superChannels.remove(incoming);
		}

		//superNeighbors.remove(message.getRegionId());
		
		msgHandler.getControl().getNetwork().getSuperNeighbors().remove(message.getRegionId());
		superMap.remove(message.getRegionId());
		for (Channel sc : superChannels) {
			logger.info("Sending region change to SP ");
			sc.writeAndFlush(message);
		}
		for (Channel channel : channels) {
			logger.info(message.getMessageId() + "sending region change to all on ");
			channel.writeAndFlush(message);

		}

	}
	public void receiveRegionChange(Message message,Channel incoming){
		logger.info("Previous region id to remove : "+message.getRegionId());
		msgHandler.getControl().getNetwork().getSuperNeighbors().remove(message.getRegionId());
		superMap.remove(message.getRegionId());
		//superNeighbors.remove(message.getRegionId());
		//msgHandler.getControl().getNetwork().getSuperNeighbors().remove(message.getRegionId());
		for (Channel channel : channels) {
			if (channel !=incoming) {
				logger.info(message.getMessageId() + "sending region change to all on ");
				channel.writeAndFlush(message);
			}
			

		}
		
		superChannels.remove(incoming);
		
	}
	
	public static void cleanUp(){
		 channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE) ;
	     superChannels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE) ;
	     existingPeers = new LinkedList<HostInfo>();
	    // superNeighbors = new HashMap<Integer, HostInfo>();
	     logger.info("super clean up successfull .... ");
	}

	/* (non-Javadoc)
	 * @see io.netty.channel.SimpleChannelInboundHandler#channelRead0(io.netty.channel.ChannelHandlerContext, java.lang.Object)
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext arg0, Message arg1)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public void updateNeighborState(Message message){
		logger.info("updating neighbor info to own Ordinary peers......");
		for (Channel channel : channels) {
			 	logger.info(message.getMessageId() + "sending neighbor info to all on ");
				channel.writeAndFlush(message);
			}
		}

	public static Map<Integer, HostInfo> getSuperMap() {
		return superMap;
	}

	public static void setSuperMap(Map<Integer, HostInfo> superMap) {
		SuperPeerHandler.superMap = superMap;
	}
	
	
	}

