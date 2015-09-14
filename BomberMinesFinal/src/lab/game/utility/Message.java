/**
 * 
 */
package lab.game.utility;

import java.io.Serializable;
import java.util.Map;
import java.util.Queue;

import lab.game.pojo.HostInfo;

/**
 * @author Faisal Arshad
 *
 */

public class Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1695565724518865254L;
	
	private int playerId;
	private int regionId;
	private int xPlayerPos;
	private int yPlayerPos;
	private int xBombPos;
	private int yBombPos;
	private String superIp;
	private int superPort;
	private String messageId;
	private Queue<HostInfo> existingPeers;
	private int[][] regionMap;
	private boolean isSuper;
	private Map<Integer,HostInfo> superNeighbors;
	private HostInfo neighbor;
	
	

	
	/**
	 * @return the playerId
	 */
	public int getPlayerId() {
		return playerId;
	}
	/**
	 * @param playerId the playerId to set
	 */
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	/**
	 * @return the regionId
	 */
	public int getRegionId() {
		return regionId;
	}
	/**
	 * @param regionId the regionId to set
	 */
	public void setRegionId(int regionId) {
		this.regionId = regionId;
	}
	/**
	 * @return the xPlayerPos
	 */
	public int getxPlayerPos() {
		return xPlayerPos;
	}
	/**
	 * @param xPlayerPos the xPlayerPos to set
	 */
	public void setxPlayerPos(int xPlayerPos) {
		this.xPlayerPos = xPlayerPos;
	}
	/**
	 * @return the yPlayerPos
	 */
	public int getyPlayerPos() {
		return yPlayerPos;
	}
	/**
	 * @param yPlayerPos the yPlayerPos to set
	 */
	public void setyPlayerPos(int yPlayerPos) {
		this.yPlayerPos = yPlayerPos;
	}
	/**
	 * @return the xBombPos
	 */
	public int getxBombPos() {
		return xBombPos;
	}
	/**
	 * @param xBombPos the xBombPos to set
	 */
	public void setxBombPos(int xBombPos) {
		this.xBombPos = xBombPos;
	}
	/**
	 * @return the yBombPos
	 */
	public int getyBombPos() {
		return yBombPos;
	}
	/**
	 * @param yBombPos the yBombPos to set
	 */
	public void setyBombPos(int yBombPos) {
		this.yBombPos = yBombPos;
	}
	
	/**
	 * @return the messageId
	 */
	public String getMessageId() {
		return messageId;
	}
	/**
	 * @param messageId the messageId to set
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	/**
	 * @return the superIp
	 */
	public String getSuperIp() {
		return superIp;
	}
	/**
	 * @param superIp the superIp to set
	 */
	public void setSuperIp(String superIp) {
		this.superIp = superIp;
	}
	/**
	 * @return the superPort
	 */
	public int getSuperPort() {
		return superPort;
	}
	/**
	 * @param superPort the superPort to set
	 */
	public void setSuperPort(int superPort) {
		this.superPort = superPort;
	}
	/**
	 * @return the existingPeers
	 */
	public Queue<HostInfo> getExistingPeers() {
		return existingPeers;
	}
	/**
	 * @param existingPeers the existingPeers to set
	 */
	public void setExistingPeers(Queue<HostInfo> existingPeers) {
		this.existingPeers = existingPeers;
	}
	/**
	 * @return the regionMap
	 */
	public int[][] getRegionMap() {
		return regionMap;
	}
	/**
	 * @param regionMap the regionMap to set
	 */
	public void setRegionMap(int[][] regionMap) {
		this.regionMap = regionMap;
	}
	/**
	 * @return the isSuper
	 */
	public boolean isSuper() {
		return isSuper;
	}
	/**
	 * @param isSuper the isSuper to set
	 */
	public void setSuper(boolean isSuper) {
		this.isSuper = isSuper;
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
	 * @return the neighbor
	 */
	public HostInfo getNeighbor() {
		return neighbor;
	}
	/**
	 * @param neighbor the neighbor to set
	 */
	public void setNeighbor(HostInfo neighbor) {
		this.neighbor = neighbor;
	}
	
	
	
	
	
}
