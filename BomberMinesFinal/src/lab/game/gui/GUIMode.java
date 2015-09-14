package lab.game.gui;


import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.logging.Level;
import lab.game.entities.Player;
import lab.game.utility.Constants;



/**
 * Runs the game in GUI mode. Main game thread looks for player movement according to keys pressed and updates the player
 * position in GUI accordingly. Places the bomb if space bar is pressed. Mutable objects of the region are blasted when
 * bomb is placed. the bomb is blasted after the blast time. The events occurring in game are sent to respective peers via
 * super peer 
 *
 */

public class GUIMode extends Game implements Runnable {
	
	private static final long serialVersionUID = 1L;
	public static boolean running = false;
	
	public Thread gameThread;
	
	
	public GUIMode(){
		super();		
	}
	
	/**
	 * Game initialization parameters
	 */
	public void initGUI(){
								
		try{
			
		int playerSpeed = 32;
		
		if(Constants.getImagesize()==64)
			playerSpeed = 64;
			
		
				
		player = new Player(0, 0, imageManager,this);
		player.setSpeed(playerSpeed);
					
		//regionController = new RegionController();		
		//toolBar.setRegionLabel(activeRegion.getRegionID());
		player.setCurrentRegionId(activeRegion.getRegionID());
				
				
		toolBar.setRegionLabel(activeRegion.getRegionID());
		}catch(Exception e){
			
			logger.log(Level.SEVERE, e.getMessage());
			
		}
	}
	
	/**
	 * Starts the main game thread
	 */
	
	public synchronized void start(){
		
		if(running)return;
		running = true;
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	
	/**
	 * Stops the game thread
	 */
	public synchronized void stop(){
		if(!running)return;
		running = false;
		try {
			gameThread.join();
		} catch (InterruptedException e) {e.printStackTrace();}
	}
	
	
	/**
	 * Runs the main game thread. Look for player movement, bomb placement
	 */
	
	public void run() {	
				
		long lastTime = System.nanoTime();
		final double amountOfTicks = 30D;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;		
		
		
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;	//sets the GUI update rate
			lastTime = now;			
			
			
			if (delta >= 1) {
				if (player.isUp() || player.isDown() || player.isLeft() || player.isRight()|| player.isSpace())
				{
					tick();	
					
				}
				
				render();
				
				delta--;
			}				
			
		}
		stop();
	}
	
	private void tick(){
		player.tick(activeRegion);
		
	}
	
	public  BufferStrategy getBufferStategy(){
		return this.getBufferStrategy();
	}
	
	
	/**
	 * Renders the graphics in game e.g player position, bomb position, bomb blasting, destroying the mutable objects
	 */
	
	public void render(){
		BufferStrategy bs = this.getBufferStrategy();
		
		if(bs ==  null){
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();		
		
		//checks if region is changed
		if(player.rBYUpFlag || player.rBYDownFlag  || player.rBXLeftFlag || player.rBXRightFlag){
						
			//logger.info("Before===Active region Id = " + activeRegion.regionID);
			int regID = regionController.getRegionID(activeRegion, player)-1;
			
			activeRegion = region[regID];
			//logger.info("After===Active region Id = " + activeRegion.getRegionID());
			
			toolBar.setRegionLabel(activeRegion.getRegionID());
		}
		
		//////
		
		g.fillRect(0, 0, Constants.getScreenWidth() * Constants.getScale(), Constants.getScreenHeight() * Constants.getScale());		
		player.render(g);		
		
		activeRegion.renderPeer(g);		
		activeRegion.render(g);
		
		//END RENDER
		g.dispose();
		
		bs.show();
		 
	}
	
	/**
	 * Adds new peer in the region if not exists already. otherwise updates the peer existing position		
	 */
	
	public void insertUpdatePeers(int regionID,int playerID,int x,int y,boolean isStateUpdate,int[][] regionMap){		
		
		if (isStateUpdate) {
			//logger.info(regionID + " region length: "+region.length);
			activeRegion = region[regionID-1];
			this.activeRegion.setRegionID(regionID);
			this.activeRegion.setRegionMap(regionMap);
			toolBar.setRegionLabel(activeRegion.getRegionID());
			getPlayer().setCurrentRegionId(regionID);
			//this.activeRegion = region[regionID-1];
		}else{
//			if (this.activeRegion.getRegionID() != regionID) {
//					checkAndRemovePeerFromRegion(regionID,playerID);
//			}
			
			region[regionID-1].insertUpdatePeers(playerID,x,y,imageManager);
		}	
		
		
	}
	
    /**
     * Removes the peer from the region
     */
	
	public void checkAndRemovePeerFromRegion(int regionID,int playerID){
	//	logger.info("Peer left on region change  ....");
		this.region[this.activeRegion.getRegionID()-1].removePlayer(playerID);
		
	}
	
	
	public void updateScreenMap(int regionID,int screenMap[][]){
		
		region[regionID-1].setRegionMap(screenMap);
	}
	
	
	public void placeBomb(int regionID,int x,int y){
		
		region[regionID-1].placeBomb(x, y);
	}
	
	
	public Player getPlayer(){
		return player;
	}
	
	public static ImageManager getImageManager(){
		return imageManager;
	}
	
	public Region getActiveRegion(){
		return activeRegion;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Canvas#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics paramGraphics) {
		// TODO Auto-generated method stub
		super.paint(paramGraphics);
		
	}
	
	

	/**
	 * @return the toolBar
	 */
	public ToolBar getToolBar() {
		return toolBar;
	}

	/**
	 * @param toolBar the toolBar to set
	 */
	public void setToolBar(ToolBar toolBar) {
		this.toolBar = toolBar;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 */
	

	
}