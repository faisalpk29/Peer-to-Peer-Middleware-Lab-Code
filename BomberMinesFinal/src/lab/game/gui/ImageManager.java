package lab.game.gui;

import java.awt.image.BufferedImage;

import lab.game.utility.Constants;

/**
 * Image manager class manging the images used for various mutable and immutable objects used in game
 * 
 *
 */

public class ImageManager {
	
	private SpriteSheet spriteSheet;
	private BufferedImage player;
	private BufferedImage brickTile;
	private BufferedImage wallTile;
	private BufferedImage bomb;
	private BufferedImage flame;
	private BufferedImage peer;
	private BufferedImage grassTile;
	
	
	
	
	public ImageManager(SpriteSheet ss){
		this.spriteSheet = ss;
		
		if(Constants.getImagesize()==64){
			this.setPlayer(ss.crop(1, 1, Constants.getImagesize(), Constants.getImagesize()));		
			this.setBrickTile(ss.crop(0, 0,Constants.getImagesize(), Constants.getImagesize()));
			this.setWallTile(ss.crop(2, 0, Constants.getImagesize(), Constants.getImagesize()));
			this.setBomb(ss.crop(8, 1, Constants.getImagesize(), Constants.getImagesize()));
			this.setFlame(ss.crop(0, 4, Constants.getImagesize(), Constants.getImagesize()));
			this.setPeer(ss.crop(2, 2, Constants.getImagesize(), Constants.getImagesize()));
			this.setGrassTile(ss.crop(2, 0, Constants.getImagesize(), Constants.getImagesize()));
			
		}
		
		//logger.info("Constant  = " + Constants.getImagesize());
		else if(Constants.getImagesize()==32){
			
			this.setPlayer(ss.crop(0, 5, Constants.getImagesize(), Constants.getImagesize()));		
			this.setBrickTile(ss.crop(0, 2,Constants.getImagesize(), Constants.getImagesize()));
			this.setWallTile(ss.crop(0, 4, Constants.getImagesize(), Constants.getImagesize()));
			this.setBomb(ss.crop(0, 1, Constants.getImagesize(), Constants.getImagesize()));
			this.setFlame(ss.crop(0, 3, Constants.getImagesize(), Constants.getImagesize()));
			this.setPeer(ss.crop(0, 6, Constants.getImagesize(), Constants.getImagesize()));
			this.setGrassTile(ss.crop(0, 0, Constants.getImagesize(), Constants.getImagesize()));
			
			
		}
		
	}
	
	public SpriteSheet getSpriteSheet(){
		return this.spriteSheet;
	}

	/**
	 * @return the player
	 */
	public BufferedImage getPlayer() {
		return player;
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(BufferedImage player) {
		this.player = player;
	}

	/**
	 * @return the brickTile
	 */
	public BufferedImage getBrickTile() {
		return brickTile;
	}

	/**
	 * @param brickTile the brickTile to set
	 */
	public void setBrickTile(BufferedImage brickTile) {
		this.brickTile = brickTile;
	}

	/**
	 * @return the wallTile
	 */
	public BufferedImage getWallTile() {
		return wallTile;
	}

	/**
	 * @param wallTile the wallTile to set
	 */
	public void setWallTile(BufferedImage wallTile) {
		this.wallTile = wallTile;
	}

	/**
	 * @return the bomb
	 */
	public BufferedImage getBomb() {
		return bomb;
	}

	/**
	 * @param bomb the bomb to set
	 */
	public void setBomb(BufferedImage bomb) {
		this.bomb = bomb;
	}

	/**
	 * @return the flame
	 */
	public BufferedImage getFlame() {
		return flame;
	}

	/**
	 * @param flame the flame to set
	 */
	public void setFlame(BufferedImage flame) {
		this.flame = flame;
	}

	/**
	 * @return the peer
	 */
	public BufferedImage getPeer() {
		return peer;
	}

	/**
	 * @param peer the peer to set
	 */
	public void setPeer(BufferedImage peer) {
		this.peer = peer;
	}

	/**
	 * @param spriteSheet the spriteSheet to set
	 */
	public void setSpriteSheet(SpriteSheet spriteSheet) {
		this.spriteSheet = spriteSheet;
	}

	/**
	 * @return the grassTile
	 */
	public BufferedImage getGrassTile() {
		return grassTile;
	}

	/**
	 * @param grassTile the grassTile to set
	 */
	public void setGrassTile(BufferedImage grassTile) {
		this.grassTile = grassTile;
	}
	
	

}
