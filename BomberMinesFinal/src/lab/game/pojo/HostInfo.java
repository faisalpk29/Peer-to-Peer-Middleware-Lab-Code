package lab.game.pojo;

import java.io.Serializable;

/**
 * 
 */

/**
 * @author Faisal Arshad
 *
 */
public class HostInfo implements Serializable {
	private String superIp;
	private int port;
	private  Integer playerId;
	private String remoteIp;
	private int regionId;
	
	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}
	/**
	 * @return the playerId
	 */
	public Integer getPlayerId() {
		return playerId;
	}
	/**
	 * @param playerId the playerId to set
	 */
	public void setPlayerId(Integer playerId) {
		this.playerId = playerId;
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
	 * @return the remoteIp
	 */
	public String getRemoteIp() {
		return remoteIp;
	}
	/**
	 * @param remoteIp the remoteIp to set
	 */
	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
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
	
	
	
	
}
