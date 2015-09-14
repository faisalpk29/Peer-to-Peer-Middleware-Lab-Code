package lab.game.gui;

import java.awt.Canvas;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import lab.game.entities.Player;
import lab.game.utility.Constants;
import lab.game.utility.Util;

/**
 * Abstract game class from which ConsoleMode and GUIMode classes inheret to run the game in console mode or GUI mode
 * Provides the basic functionality for whole game space e.g regions, active region, region controller, GUI components
 * Active region is the region where player is currently moving. Each region contains mutable and immutable objects
 *
 */


public abstract class Game extends Canvas {
	
	
	private static final long serialVersionUID = 1L;
	protected final static Logger logger = Constants.getLogger(Game.class.getName());
	protected BufferedImage spriteSheet;
	protected static ImageManager imageManager;
	protected Player player;
	
	protected ToolBar toolBar;
	
	protected Region activeRegion;
	protected RegionController regionController;
	protected Region region[];
	
	public Game(){		
		init();		
	}
	
	public void init(){
		
					
		try{
			
		int playerSpeed = 32;
			
		logger.setLevel(Constants.getLogLevel());
		region = new Region[Constants.getRegions()];
		
		if(Constants.getImagesize()==32){
			spriteSheet = Util.loadImage("/spritesheet.png");
			
			playerSpeed = 32;
			
		}
		else if(Constants.getImagesize()==64){
			spriteSheet = Util.loadImage("/spritesheet1.png");
			playerSpeed = 64;
		}
		
		SpriteSheet ss = new SpriteSheet(spriteSheet);
		imageManager = new ImageManager(ss);
		
		
		Util util = new Util();
				
		regionController = new RegionController();			
		
		int rowsInRegion = Constants.getScreenHeight()/Constants.getImagesize();
		int colsInRegion = Constants.getScreenWidth()/Constants.getImagesize();
		
		for(int i=0;i<region.length;i++){
			region[i] = new Region(imageManager, i+1,this);
			region[i].setRegionBoundary(Constants.getScreenWidth(), Constants.getScreenHeight());
					
			if(Constants.getBricksCoordinate().get("rbricks"+i)!=null){
				
				region[i].setBrickCoordinatesForRegion(util.extractRegionCoordinates(Constants.getBricksCoordinate().get("rbricks"+i),rowsInRegion,colsInRegion));			
				
			}	
			//region[i].setGrassCoordinatesForRegion(util.extractRegionCoordinates(Constants.getGrassCoordinate().get("rgrass"+i)));
			if(Constants.getBlocksCoordinate().get("rblocks"+i)!=null)
				region[i].setWallCoordinatesForRegion(util.extractRegionCoordinates(Constants.getBlocksCoordinate().get("rblocks"+i),rowsInRegion,colsInRegion));
			
			region[i].initRegionParams();
			
			region[i].createRegionMap(rowsInRegion,colsInRegion);			
		}			
		
		
		activeRegion = region[Constants.getActiveRegion()];
		
		
		
		}catch(Exception e){
			
			logger.log(Level.SEVERE, e.getMessage());
			
		}
	}
	
	
	
	
	
	public void insertUpdatePeers(int regionID,int playerID,int x,int y,boolean isStateUpdate,int[][] regionMap){		
		
		if (isStateUpdate) {			
			activeRegion = region[regionID-1];
			this.activeRegion.setRegionID(regionID);
			this.activeRegion.setRegionMap(regionMap);
			//kkam to be set for gui mode toolBar.setRegionLabel(activeRegion.getRegionID());
			getPlayer().setCurrentRegionId(regionID);
			
		}else{		
			
			region[regionID-1].insertUpdatePeers(playerID,x,y,imageManager);
		}	
		
	}
	
	/**
	 * Each region contains the active players in the region. Removes the player from the region
	 * @param regionID
	 * @param playerID
	 */

	public void checkAndRemovePeerFromRegion(int regionID,int playerID){
	//	logger.info("Peer left on region change  ....");
		this.region[this.activeRegion.getRegionID()-1].removePlayer(playerID);		
	}
	
	/**
	 * Updates the screen map whenever new player joins the region
	 * @param regionID
	 * @param screenMap
	 */
	
	public void updateScreenMap(int regionID,int screenMap[][]){
		
		region[regionID-1].setRegionMap(screenMap);
	}
	
	/**
	 * Places the bomb in the region at coordinates x and y
	 * @param regionID
	 * @param x
	 * @param y
	 */
	
	public void placeBomb(int regionID,int x,int y){
		
		region[regionID-1].placeBomb(x, y);
	}
	
	
	public Player getPlayer(){
		return player;
	}
	
	public void setPlayer(Player player){
		this.player = player;
	}
	
	public static ImageManager getImageManager(){
		return imageManager;
	}
	
	public Region getActiveRegion(){
		return activeRegion;
	}
	
	public void setActiveRegion(Region region){
		this.activeRegion = region;
	}
	
	
	public RegionController getRegionController(){
		return regionController;
	}
	
	/**
	 * Gets the region as specified by index of regionID (the function expects index starting from zero)
	 * @param regionID
	 * @return
	 */
	public Region getRegion(int regionID){
		return region[regionID];
	}
		
	public abstract ToolBar getToolBar();
	
	
}