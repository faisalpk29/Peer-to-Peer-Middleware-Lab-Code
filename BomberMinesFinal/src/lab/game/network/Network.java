package lab.game.network;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.logging.Logger;

import lab.game.controller.Controller;
import lab.game.pojo.HostInfo;
import lab.game.utility.Constants;
import lab.game.utility.Message;
import lab.game.utility.Util;

/**
 * Network class initiate the Super peer for listening to 
 * messages and also Normal peer 
 * @author FAISAL
 *
 */
public class Network {
	private final static Logger logger= Constants.getLogger(Network.class.getName());
	private Peer peer = null ; 
	private SuperPeer superPeer= null;
	private Controller controller;
	private Map<Integer,HostInfo> superNeighbors;
	
	private Queue<HostInfo> existingPeers ;
	private Queue<HostInfo> backupQueue ;
	private HostInfo superInfo ;
	public Network(Controller controller) throws InterruptedException{
		
		 this.controller = controller;
	     this.superPeer= new SuperPeer(Constants.getPort(),controller);
	     this.superPeer.start();
	     superNeighbors = new HashMap<Integer, HostInfo>();
	    
	 }

	public void disconnect(boolean isRegionChange,int preRegion,int currentRegion){
		 
		
		if (!Util.isNull(this.peer) && !isRegionChange) {
				this.peer.shudownGracefully();
			
		}else if (isRegionChange) {
			this.processRegionChange(preRegion,currentRegion);
		}
		
		
	}
	
	/**
	 * Connect to mentioned host for further communication
	 * @param host
	 * @param port
	 * @return
	 */
	public boolean connectToNetwork(String host,int port){
		
		logger.info("Connecting to super peer ["+host+":"+port+"]");
	        try {
	        	
	        	this.peer= new Peer(host, port, controller);
		        this.peer.start();
		         return peer.isConnected();
					       
			} catch (Exception e) {
				e.printStackTrace();
			}
	        
	        return false;
	}
	/**
	 * @return the peer
	 */
	public Peer getPeer() {
		return peer;
	}

	/**
	 * @param peer the peer to set
	 */
	public void setPeer(Peer peer) {
		this.peer = peer;
		
	}

	/**
	 * @return the superPeer
	 */
	public SuperPeer getSuperPeer() {
		return superPeer;
	}

	/**
	 * @param superPeer the superPeer to set
	 */
	public void setSuperPeer(SuperPeer superPeer) {
		this.superPeer = superPeer;
	}

	/**
	 * @return the superNeighbors
	 */
	public Map<Integer, HostInfo> getSuperNeighbors() {
		return superNeighbors;
	}
	/**
	 * @param superNeighbors the superNeighbors to set
	 */
	public void setSuperNeighbors(Map<Integer, HostInfo> superNeighbors) {
		this.superNeighbors = superNeighbors;
	}
	
	/**
	 * Process region change scenario
	 * Ordinary Peer : Gracefully leave
	 * Super Peer : select the next super peer in queue if not 
	 * found become the super peer.
	 * @param previousRegion
	 * @param currentRegion
	 */
	public void processRegionChange(int previousRegion,int currentRegion){
		logger.info("Region change detected....");
		controller.getPlayer().setConnected(false);
		 --currentRegion;
		 
		try {
			if (Constants.isSuperNeighbor()) {
				 --previousRegion;
				superNeighbors.remove(previousRegion);
				Message rcm =  new Message();
				rcm.setMessageId(Constants.REGION_CHANGE);
				rcm.setRegionId(previousRegion);
				rcm.setPlayerId(this.controller.getPlayer().getPlayerID());
				rcm.setSuper(Constants.isSuperNeighbor());
				sendRegionChange(rcm);
				logger.info("New Region id : "+currentRegion);
			    Constants.setSuperNeighbor(false) ;
			    
			}
			 existingPeers = SuperPeerHandler.getExistingPeers();
			 backupQueue = PeerHandler.getBackupQueue();
			 superInfo = 	PeerHandler.getSuperInfo();
			cleanUp();
			this.controller.getPlayer().setCurrentRegionId(currentRegion);
			logger.info("Current Region ID : "+currentRegion); 
		     HostInfo info =(HostInfo) this.superNeighbors.get(currentRegion);
		     Constants.setActiveRegion(currentRegion);
		     if (!Util.isNull(info)) {
		    	
		    	 Constants.setSuperNeighbor(false);
		    	
		    	 
		    	 if (info.getPort() != Constants.getPort() || !info.getSuperIp().equalsIgnoreCase(Constants.IP_ADDRESS)) {
		    		 logger.info("Super Neighbor  found .....");
		    		 logger.info("Connecting to neighbor SP as ordinary peer  ...."+info.getSuperIp()+":"+ info.getPort());
		    		 connectToNetwork(info.getSuperIp(), info.getPort());

				    
		    	 }else {
		    		 //TODO logic for connecting other SP on becoming new super peer...
		    		 logger.info("No Super Peer Exist in this Region : Connecting to neighbor SP as super node ....");
					Constants.setSuperNeighbor(true);
					logger.info(info.getSuperIp()+":"+info.getPort());
					connectAsSuperNeighbor(Constants.getActiveRegion());
					
					
				}
		    	 

			}else {
				logger.info("No super peer for this Region still exist ....");

				logger.info("i am back to super in Region : "+Constants.getActiveRegion()+ " looking for neighbor super peers");
				Constants.setSuperNeighbor(true); 
				connectAsSuperNeighbor(Constants.getActiveRegion());
			
			     }
		     
		} catch (Exception e) {
			logger.severe(e.getMessage());
			e.printStackTrace();
		}finally{
			
		}
		 
	}
	
	
	/**
	 * In case of super Peer leaving the region 
	 * inform all connected peers about region change.
	 * @param message
	 */
	private void sendRegionChange(Message message){
		logger.info("inside sendRegionChange ....... : "+SuperPeerHandler.getConnectedPeers());
		if (SuperPeerHandler.getConnectedPeers() >0 | SuperPeerHandler.getConnectedSuper()>0) {
			logger.info("super peer leaving region ......");
			superPeer.sendRegionChange(message);
			
			
		}else {
			peer.sendRegionChange(message);
			//peer.shudownGracefully();
		}
				
	}
	
	private void connectToSuperNeighbor(HostInfo superHost){
		
		 logger.info(" Super Neighbor  ...."+superHost.getSuperIp()+":"+ superHost.getPort());
		 connectToNetwork(superHost.getSuperIp(), superHost.getPort());
	}
	
	/**
	 * On region change select the super neighbor to connect 
	 * Region change Implementation is limited to just 4 regions.
	 * 
	 * First it will search in super neighbors list if not found any 
	 * then it will select one from top of queue of last region table it left.
	 * @param regionId
	 */
	private void connectAsSuperNeighbor(int regionId){
		logger.info("connectAsSuperNeighbor ....");
		switch (regionId) {
		case 0:
		case 3:
			if (!Util.isNull(this.superNeighbors.get(1))) {
				connectToSuperNeighbor((HostInfo)this.superNeighbors.get(1));
			}else if (!Util.isNull(this.superNeighbors.get(2))){
				connectToSuperNeighbor((HostInfo)this.superNeighbors.get(2));
			}else {
				selectNeighborAndConnect();
			}
			break;
		case 1:
		case 2:
			if (!Util.isNull(this.superNeighbors.get(0))) {
				connectToSuperNeighbor((HostInfo)this.superNeighbors.get(0));
			}else if (!Util.isNull(this.superNeighbors.get(3))){
				connectToSuperNeighbor((HostInfo)this.superNeighbors.get(3));
			}else {
				selectNeighborAndConnect();
				
			}
			break;

		
		default:
			logger.warning("Invalid region id ...");
			break;
		}
	}
	/**
	 * Cleaning up all routing tables on region change.
	 */
	private void cleanUp(){
		if (!Util.isNull(this.peer) ) {
			this.peer.shudownGracefully();
		}
		SuperPeerHandler.cleanUp();
		PeerHandler.cleanUp();
	}
	/**
	 * Connect to neighboring super peer as super 
	 * Either from the ordinary nodes list or super neighbors list 
	 * of previous region.
	 */
	private void selectNeighborAndConnect(){
		logger.info("super map size : "+ SuperPeerHandler.getSuperMap().size());
		if (existingPeers.size() > 0 | SuperPeerHandler.getSuperMap().size()>0 ) {
			logger.info("Super become super");
			
			if (existingPeers.size()>0) {
				HostInfo host = existingPeers.remove();
				logger.info(host.getPort() +"  Region "+ host.getRegionId());
				connectToSuperNeighbor(host);
			}else if (SuperPeerHandler.getSuperMap().size()>0) {
				logger.info("super map with enteries found ....");
				Map<Integer,HostInfo> superMap = SuperPeerHandler.getSuperMap();
				switch (Constants.getActiveRegion()) {
				case 0:
				case 3:
					if (!Util.isNull(superMap.get(1))) {
						connectToSuperNeighbor((HostInfo)superMap.get(1));
					}else if (!Util.isNull(superMap.get(2))){
						connectToSuperNeighbor((HostInfo)superMap.get(2));
					}
					break;
				case 1:
				case 2:
					if (!Util.isNull(superMap.get(0))) {
						connectToSuperNeighbor((HostInfo)superMap.get(0));
					}else if (!Util.isNull(superMap.get(3))){
						connectToSuperNeighbor((HostInfo)superMap.get(3));
					}
					break;

				
				default:
					logger.warning("Invalid region id ...");
					break;
				
			}
			}
		}else {
			
			try {
				if ( !Util.isNull(superInfo)) {
					
					 logger.info(superInfo.getPort() +"  Region "+ superInfo.getRegionId());
					 connectToSuperNeighbor(superInfo);
				}else{
					if (backupQueue.size()>0) {
						HostInfo host = PeerHandler.getBackupQueue().remove();
						logger.info(host.getPort() +"  [Region] "+ host.getRegionId());
						connectToSuperNeighbor(host);
					}
				}
				 
			} catch (Exception e) {
				logger.severe("catch code here "+e.getMessage());
			}
			
			

			
			
			
		}
		
	}
}
