
package lab.game.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.logging.Logger;

import lab.game.controller.Controller;
import lab.game.controller.MessageHandler;
import lab.game.pojo.HostInfo;
import lab.game.utility.Constants;
import lab.game.utility.Message;
import lab.game.utility.Util;

/**
 * 
 * @author FAISAL
 *
 */
public class PeerHandler extends SimpleChannelInboundHandler<Message> {

    private static final Logger logger = Constants.getLogger(PeerHandler.class.getName());
    private Controller controller;
    private MessageHandler msgHandler;
    private static Queue<HostInfo> backupQueue;
    private static HostInfo superInfo;
   
    public PeerHandler() {
       
        backupQueue = new LinkedList<HostInfo>();
      }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws IOException {
    	this.msgHandler.setPeerHandler(this);
    	
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	Message m = (Message)msg;
    	msgHandler.handleMessage(m,ctx.channel(),false);
    	
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
       ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        //logger.log(Level.SEVERE, "super Peer Terminated -", cause.getMessage());
        ctx.close();
        cause.printStackTrace();
       // logger.severe(cause.toString());
      //  new Peer("localhost", 9005, 256).run();
       // 
    }

	/**
	 * @return the controller
	 */
	public Controller getController() {
		return controller;
	}

	/**
	 * @param controller the controller to set
	 */
	public void setController(Controller controller) {
		this.controller = controller;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message msg)
			throws Exception {
		Message m = (Message)msg;
    //	msgHandler.handleMessage(m);
		
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
	}

	/**
	 * @return the backupQueue
	 */
//	public Queue<String> getBackupQueue() {
//		return backupQueue;
//	}

	/**
	 * @param backupQueue the backupQueue to set
	 */
	public static void setBackupQueue(Queue<HostInfo> bQueue) {
		backupQueue = bQueue;
	}

	/**
	 * Receive message on any new ordinary peer joining the game via super peer.
	 * @param message
	 */
	public void receiveJoinMessage(Message message){
		logger.info( "received join message ...."+message.getPlayerId());
		HostInfo info= new HostInfo();
		info.setPlayerId(message.getPlayerId());
		info.setPort(message.getSuperPort());
		info.setSuperIp(message.getSuperIp());
		
		backupQueue.add(info);
		
	}
	
	/**
	 * Receive graceful leave message of Ordinary peer 
	 * via super peer.
	 * @param message
	 */
	public void receiveLeaveMessage(Message message){
		logger.info( "received gracefull leave message ....");
		HostInfo host = new HostInfo();
		for (HostInfo info : backupQueue) {
			if (info.getPlayerId() == message.getPlayerId()) {
				host = info;
				logger.info("Player left : "+info.getPort());
			}
		}
		if (backupQueue.size() > 0 && backupQueue.contains(host)) {
			
			backupQueue.remove(host);
			logger.info("queue size after leave : "+backupQueue.size());
		}
		
	}
	/**
	 * Receive state update from Super Peer
	 * @param message
	 */
	public void stateUpdate(Message message){
		superInfo = new HostInfo();
		superInfo.setPlayerId(message.getPlayerId());
		superInfo.setRemoteIp(message.getSuperIp());
		superInfo.setRegionId(message.getRegionId());
		superInfo.setSuperIp(message.getSuperIp());
		superInfo.setPort(message.getSuperPort());
		Constants.setSuperNeighbor(message.isSuper());
		logger.info(message.getPlayerId()+" : "+ message.getSuperIp()+":"+message.getSuperPort());
		this.msgHandler.getControl().getNetwork().setSuperNeighbors(message.getSuperNeighbors());

		if (!Util.isNull(message.getExistingPeers())) {
			backupQueue = message.getExistingPeers();
		}
	}

	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelInactive(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.info("channelInactive");
		super.channelInactive(ctx);
	}

	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelUnregistered(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		super.channelUnregistered(ctx);
		logger.info("channelUnregistered : ");
		this.processPeerLeave(ctx.channel(),null);
	
	}
	
	/**
	 * Receive super neighbor information via Super Peer 
	 * @param message
	 */
	public void neighborUpdate(Message message){
		
		logger.info("New neighbour arrived in Region ["+message.getNeighbor().getRegionId()+"]");
		msgHandler.getControl().getNetwork().getSuperNeighbors().put(message.getNeighbor().getRegionId(), message.getNeighbor());
		
	}
		
	/**
	 * When ordinary peer leave the region 
	 * @param rlm
	 * @param incoming
	 */
	public void processRegionLeave(Message rlm,Channel incoming){
		logger.info("inside processRegionLeave");
		this.processPeerLeave(incoming,rlm);
	}
	
	/**
	 * Select the next super peer in case of any peer
	 * leave the region and also connect to super Neighbor.
	 * @param incoming
	 * @param regionLeaveMsg
	 */
	private void processPeerLeave(Channel incoming,Message regionLeaveMsg){
	Message m = new Message();
		if (!Util.isNull(regionLeaveMsg)) {
			logger.info("Region id to remove : "+regionLeaveMsg.getRegionId());
			msgHandler.getControl().getNetwork().getSuperNeighbors().remove(regionLeaveMsg.getRegionId());
		}
	
		
	if (!Util.isNull(superInfo)) {
			m.setPlayerId(superInfo.getPlayerId());
			m.setMessageId(Constants.PEER_LEAVE);
			msgHandler.handleMessage(m, incoming, false);
		}

		if (backupQueue.size()>0) {
			
				superInfo =backupQueue.remove();
				logger.info("Next Super Peer Informaiton  ");
				logger.info("Port "+superInfo.getPort() +" Player id  "+superInfo.getPlayerId()
						+" Super ip "+superInfo.getSuperIp()+" Region id "+superInfo.getRegionId());
				logger.info("current from players : "+ msgHandler.getControl().getPlayer().getCurrentRegionId());
				Constants.setActiveRegion(msgHandler.getControl().getPlayer().getCurrentRegionId()-1);
				
				if (superInfo.getPort() != Constants.getPort() || !superInfo.getSuperIp().equalsIgnoreCase(Constants.IP_ADDRESS)) {
					
					Constants.setSuperNeighbor(false) ; 
					Peer peer = new Peer(superInfo.getSuperIp(), superInfo.getPort(), msgHandler.getControl());
					peer.start();
					msgHandler.getControl().getNetwork().setPeer(peer);
				}else {
					logger.info("I am new Super Peer : "+Constants.getMyIpAddress() + "For Region : "+Constants.getActiveRegion());
					Constants.setSuperNeighbor(true); 
					msgHandler.getControl().getPlayer().setConnected(false);
					
					//logger.info(info.getSuperIp()+":"+info.getPort());
					Map<Integer,HostInfo> superNeighbors = msgHandler.getControl().getNetwork().getSuperNeighbors();
					switch ( Constants.getActiveRegion()) {
					case 0:
					case 3:
						if (!Util.isNull(superNeighbors.get(1))) {
							connectToSuperNeighbor((HostInfo)superNeighbors.get(1));
						}else if (!Util.isNull(superNeighbors.get(2))){
							connectToSuperNeighbor((HostInfo)superNeighbors.get(2));
						}
						break;
					case 1:
					case 2:
						if (!Util.isNull(superNeighbors.get(0))) {
							connectToSuperNeighbor((HostInfo)superNeighbors.get(0));
						}else if (!Util.isNull(superNeighbors.get(3))){
							connectToSuperNeighbor((HostInfo)superNeighbors.get(3));
						}
						break;

					
					default:
						logger.warning("Invalid region id ...");
						break;
					}

				}
			
			
			
			
			
			
		}
	}
	
	public static void cleanUp(){
		backupQueue = new LinkedList<HostInfo>();
		logger.info("Peer cleanup successfull...");
	}
	
	private void connectToSuperNeighbor(HostInfo superHost){
		
		 logger.info(" Super Neighbor  ...."+superHost.getSuperIp()+":"+ superHost.getPort());
		 Peer peer = new Peer(superHost.getSuperIp(), superHost.getPort(), msgHandler.getControl());
		 peer.start();
		 msgHandler.getControl().getNetwork().setPeer(peer);
	}

	public static Queue<HostInfo> getBackupQueue() {
		return backupQueue;
	}

	public static HostInfo getSuperInfo() {
		return superInfo;
	}

	
	
	
	}
	
	
	
