package lab.game.controller;

import java.util.logging.Logger;

import lab.game.entities.Player;
import lab.game.gui.Game;
import lab.game.network.Network;
import lab.game.network.SuperPeerHandler;
import lab.game.utility.Constants;
import lab.game.utility.Message;

/**
 * 
 * @author FAISAL
 *
 *Controller class Coordinate communication messages to and from 
 *Network and GUI every incoming and outgoing message will pass
 * through the controller. 
 *
 */
public class Controller {
	
	private static final Logger logger = Constants.getLogger( Controller.class.getName());
	private Network network = null;
	private Player player = null;
	private Game game = null;
	
	public Controller(Player player){
		this.player = player;
		 
		try {
			this.network = new Network(this);
			
			
		} catch (InterruptedException e) {
			logger.severe(e.getMessage());
			
		}
	}
	public Controller(Player player,Game game) {
		this.player = player;
		this.game = game;
		try {
			this.network = new Network(this);
			logger.setLevel(Constants.getLogLevel());
		} catch (InterruptedException e) {
			logger.severe(e.getMessage());
			
		}
	}

	/**
	 * Connect to Super Peer as Ordinary peer or Super Peer
	 * @param host
	 * @param port
	 */
	public void connectToNetwork(String host,int port){
		 this.network.connectToNetwork(host, port);
	}
	/**
	 * Send player position messages over network
	 * Check if its connected to network as Ordinary Peer 
	 * or Is it connected as Super Peer
	 * @param posMsg
	 */
	public void sendPlayerPos(Message posMsg){
		if (SuperPeerHandler.getConnectedPeers() > 0) {
			this.network.getSuperPeer().sendPlayerPos(posMsg);
		}else if (!Constants.isSuperNeighbor()) {
			network.getPeer().sendPlayerPos(posMsg);
		}
	}
	/**
	 * Receive player position from Netowrk with player id 
	 * and x y coordinates to pass over the GUI
	 * @param msg
	 */
	public void receivePlayerPos(Message msg){
		if (msg.getMessageId().equals(Constants.STATE_UPDATE)) {
				this.game.insertUpdatePeers(msg.getRegionId(),this.player.getPlayerID(),
				msg.getxPlayerPos(),msg.getyPlayerPos(),true,msg.getRegionMap());
		}else{
			this.game.insertUpdatePeers(msg.getRegionId(),msg.getPlayerId(),
					msg.getxPlayerPos(),msg.getyPlayerPos(),false,null);
		}
		
	}
	/**
	 * Send bomb position coordinates over network.
	 * @param msg
	 */
	public void sendBombPos(Message msg){
		if (SuperPeerHandler.getConnectedPeers() > 0) {
			this.network.getSuperPeer().placeBomb(msg);
		}else{
			network.getPeer().placeBomb(msg);
		}
		
	}
	
	/**
	 * Recivie bomb position parameter from network
	 * and show bomb on GUI.
	 * @param msg
	 */
	public void receiveBombPos(Message msg){
		this.game.getActiveRegion().placeBomb(msg.getxBombPos(), msg.getyBombPos());
	}
	
	/**
	 * Recieve peer leave message and remove it from GUI.
	 * @param m
	 */
	public void receivePeerLeave(Message m){
		logger.info("peer leave :"+m.getPlayerId());
		this.game.getActiveRegion().removePlayer(m.getPlayerId());
	}
	
	/**
	 * @return the network
	 */
	public Network getNetwork() {
		return network;
	}

	/**
	 * @param network the network to set
	 */
	public void setNetwork(Network network) {
		this.network = network;
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * @return the game
	 */
	public Game getGame() {
		return game;
	}

	/**
	 * @param game the game to set
	 */
	public void setGame(Game game) {
		this.game = game;
	}
	
	
	
}
