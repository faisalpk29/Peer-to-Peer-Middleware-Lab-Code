/**
 * 
 */
package lab.game.controller;

import io.netty.channel.Channel;

import java.util.logging.Logger;

import lab.game.network.PeerHandler;
import lab.game.network.SuperPeerHandler;
import lab.game.utility.Constants;
import lab.game.utility.Message;

/**
 * @author Faisal Arshad
 *
 *Handle every  message received from network 
 */
public class MessageHandler {
	private static final Logger logger = Constants.getLogger( MessageHandler.class.getName());
	private Controller control ;
	private SuperPeerHandler superHandler;
	private PeerHandler peerHandler ;
	
	public MessageHandler(Controller control) {
		super();
		this.control = control;
		logger.setLevel(Constants.getLogLevel());
	}
/**
 * Parse every incoming message based on Message id 
 * @param message
 * @param incoming
 * @param isSuper
 */

	public void handleMessage(Message message,Channel incoming,boolean isSuper){
		
		switch (message.getMessageId()) {
		case Constants.PPU:
			if (isSuper) {
				superHandler.sendPlayerPos(message,incoming);
			}
				control.receivePlayerPos(message);
			break;
		case Constants.PB:
			if (isSuper) {
				superHandler.sendBombPos(message,incoming);
			}
			control.receiveBombPos(message);
			break;
		case Constants.PEER_JOIN:
			logger.info("Peer join "+isSuper);
			if (isSuper) {
				logger.info("Peer Joined as super: "+message.getSuperIp()+":"+message.getSuperPort());
				this.superHandler.sendJoinMessage(message,incoming);
			}else{
				logger.info("Ordinary Peer Joined recievie  : "+message.getSuperIp()+":"+message.getSuperPort());
				this.peerHandler.receiveJoinMessage(message);
			}
			
			break;
		case Constants.PEER_LEAVE:
			logger.info("PEER_LEAVE  : "+message.getPlayerId());
			this.control.receivePeerLeave(message);
			
			if (isSuper) {
				superHandler.processPeerLeave(incoming, message);
			}else {
				peerHandler.receiveLeaveMessage(message);
			}
			//this.superHandler.sendJoinMessage(message,incoming);
			break;
		case Constants.STATE_UPDATE:
			logger.info("STATE_UPDATE : "+message.getPlayerId());
			if (SuperPeerHandler.getConnectedPeers() > 0) {
				
				this.superHandler.updateNeighborState(message);
			}
			peerHandler.stateUpdate(message);
			if (!message.isSuper()) {
				logger.info("not super ..... ");
				control.receivePlayerPos(message);
			}
			break;
		case Constants.NEIGHBOR_UPDATE:
			logger.info("NEIGHBOR_UPDATE");
			if (isSuper) {
				superHandler.neighborUpdate(message, incoming);
			}else {
				peerHandler.neighborUpdate(message);
			}
		break;
		case Constants.REGION_CHANGE:
			logger.info("REGION_CHANGE");
			this.control.receivePeerLeave(message);
			if (isSuper) {
				superHandler.receiveRegionChange(message, incoming);
			}else {
				
				this.peerHandler.processRegionLeave(message,incoming);
			}
			
			break;
		default:
			logger.warning( "Invalid Message Id .."+message.getMessageId());
			
			break;
		}
	}

	/**
	 * @return the superHandler
	 */
	public SuperPeerHandler getSuperHandler() {
		return superHandler;
	}

	/**
	 * @param superHandler the superHandler to set
	 */
	public void setSuperHandler(SuperPeerHandler superHandler) {
		this.superHandler = superHandler;
	}


	/**
	 * @return the peerHandler
	 */
	public PeerHandler getPeerHandler() {
		return peerHandler;
	}


	/**
	 * @param peerHandler the peerHandler to set
	 */
	public void setPeerHandler(PeerHandler peerHandler) {
		this.peerHandler = peerHandler;
	}


	/**
	 * @return the control
	 */
	public Controller getControl() {
		return control;
	}


	/**
	 * @param control the control to set
	 */
	public void setControl(Controller control) {
		this.control = control;
	}
	
	
	
}
