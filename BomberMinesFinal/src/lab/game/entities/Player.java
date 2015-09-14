package lab.game.entities;

import java.awt.Graphics;
import java.util.logging.Logger;

import lab.game.controller.Controller;
import lab.game.gui.Game;
import lab.game.gui.ImageManager;
import lab.game.gui.Region;
import lab.game.gui.RegionController;
import lab.game.network.SuperPeerHandler;
import lab.game.utility.Constants;
import lab.game.utility.Message;
import lab.game.utility.Util;

public class Player {
	
	private int bombX,bombY;
	private int x,y,x1,y1;	
	private int SPEED = 3;
	
	//isBombPlaced flag makes sure that at a time only one bomb can be placed by user
	private boolean isBombPlaced;
	private boolean isNetworkMessageSent=false;
	private long bombTime =0;
	private final static Logger logger = Constants.getLogger(Player.class.getName());
	//private PlayerDirections playerDirection;

	private ImageManager im;
	private boolean up,down,left,right;
	
	private boolean space;
	private int playerID;
	
	public boolean isPlayerActive = true;
		
	public boolean rBYUpFlag=false,rBYDownFlag = false;
	public boolean rBXLeftFlag=false,rBXRightFlag=false;
	
	private RegionController regionController = new RegionController();
	private boolean isConnected;
	private Controller controller = null;
	private Game game ;
	private int currentRegionId;
	public Player(int x, int y, ImageManager im,Game game){
		
		this.x = x;
		this.y = y;
		this.im = im;	
		this.game = game;
		//this.currentRegionId = this.game.getActiveRegion().getRegionID();
		x1=y1=0;
		if (Util.isNull(this.controller)) {
			this.controller = new Controller(this, game);
			this.setPlayerID(Util.generatePlayerId(9));
			
		}
	}
	
	public Player(int x, int y, ImageManager im, Game game,Controller controller) {

		this.x = x;
		this.y = y;
		this.im = im;
		this.game = game;
		x1 = y1 = 0;
		if (Util.isNull(this.controller)) {
			this.controller =controller;
		}
		
		x1=y1=0;
		//this.controller = new Controller(this);
	}
	
	/*
	 * Constructor for console base game
	 * 
	 * */
	public Player(Game game){
		
		this.setPlayerID(Util.generatePlayerId(9));
		logger.info("Player ID = " + getPlayerID());
		logger.setLevel(Constants.getLogLevel());			
		this.controller = new Controller(this,game);
	}
	
	public void setPlayerID(int playerID){
		logger.info(" "+ playerID);
		this.playerID = playerID;
	}
	
	public void tick(Region r){
		//logger.info("tick called ........");
		detectCollision(r);
		
		if(isBombPlaced)
			checkBombActiveTime();	
		
	}
	
	public void connectToPeer(String address,int portNo){		
		controller.connectToNetwork(address, portNo);
	}
	
	public void detectCollision(Region r){
		
		x1 = x;
		y1 = y;
		
		boolean isPositionChange = false;
		
		Message message = null;
		if (isConnected() || (SuperPeerHandler.getConnectedPeers()>0|| SuperPeerHandler.getConnectedSuper() > 0)) {
			message = new Message();
			message.setPlayerId(this.getPlayerID());
			
			
		}
		//makes sure that at a time player can place only one bomb
		if(space && !isBombPlaced){
			//logger.info( "bomb placed X=" +x  + "    Y=" + y);
			bombX = x;
			bombY = y;
			
			r.placeBomb(x,y);
			bombTime = System.currentTimeMillis();
			isBombPlaced = true;
			
			isNetworkMessageSent = false;
		}
		
		if(isUp()){
			
			y1-=SPEED;
			isPositionChange = true;			
			up = false;
			
		}else if(isDown()){
			
			y1+=SPEED;
			isPositionChange = true;
			down = false;
			
		}else if(isLeft()){
			
			x1-=SPEED;
			isPositionChange = true;
			left = false;
			
		}else if(isRight()){
			x1+=SPEED;
			isPositionChange = true;
			right = false;
		}
		
		
		//detects the collision if any object is in player path then the player position is not changed
		if(r.isAnyObject(x1, y1))
			return;
		//logger.info("=====Chk1.1 PrevReg = " + this.currentRegionId + "   new=" + r.getRegionID() + " x1= " + x1 + " y1 =" +y1);
		if (!Util.isNull(message) && this.currentRegionId != r.getRegionID()) {
			logger.info("Region Change detected : Previous Region :"+this.currentRegionId + " New region :  "+r.getRegionID());
			//logger.info("1.=====Chk1.1 PrevReg = " + this.currentRegionId + "   new=" + r.getRegionID() + " x1= " + x1 + " y1 =" +y1);	
			this.controller.getNetwork().disconnect(true,this.currentRegionId,r.getRegionID());
			logger.info("2. After disconnect");
		}else if (!Util.isNull(message) && isBombPlaced && !isNetworkMessageSent) {
			message.setMessageId(Constants.PB);			
			
			message.setxBombPos(bombX);
			message.setyBombPos(bombY);
			
			this.controller.sendBombPos(message);
			
			isNetworkMessageSent = true;
			
		}else if (!Util.isNull(message) && isPositionChange ) {
			
			message.setMessageId(Constants.PPU);
			message.setxPlayerPos(x1);
			message.setyPlayerPos(y1);
			message.setRegionId(r.getRegionID());
			this.controller.sendPlayerPos(message);
			isPositionChange = false;
			   
		}
		
		
		if(this.currentRegionId!=r.getRegionID())
			this.currentRegionId = r.getRegionID();
		
			
		
				
		x=x1;
		y=y1;
		
		regionController.controlRegionEntry(r,this);
				
	}
	
	
	public void render(Graphics g){		
		
		if(isPlayerActive)
			g.drawImage(im.getPlayer(), x, y, Constants.getPlayerImageSize(), Constants.getPlayerImageSize(), null);
		
	}
	
	
	public void checkBombActiveTime(){
		long time = System.currentTimeMillis();
		
		if(time-bombTime>3000){
			isBombPlaced = false;
		}
	}
	
	
		
	
	
		/**
	 * @return the up
	 */
	public boolean isUp() {
		return up;
	}

	/**
	 * @param up the up to set
	 */
	public void setUp(boolean up) {
		this.up = up;
	}

	/**
	 * @return the down
	 */
	public boolean isDown() {
		return down;
	}

	/**
	 * @param down the down to set
	 */
	public void setDown(boolean down) {
		this.down = down;
	}

	/**
	 * @return the left
	 */
	public boolean isLeft() {
		return left;
	}

	/**
	 * @param left the left to set
	 */
	public void setLeft(boolean left) {
		this.left = left;
	}

	/**
	 * @return the right
	 */
	public boolean isRight() {
		return right;
	}

	/**
	 * @param right the right to set
	 */
	public void setRight(boolean right) {
		this.right = right;
	}

	/**
	 * @return the space
	 */
	public boolean isSpace() {
		return space;
	}

	/**
	 * @param space the space to set
	 */
	public void setSpace(boolean space) {
		this.space = space;
	}
	
	
	public int getPlayerCurXPos(){
		return x;
	}
	
	public int getPlayerCurYPos(){
		return y;
	}
	
	
	public void setPlayerCurXPos(int x){
		this.x = x;
	}
	
	public void setPlayerCurYPos(int y){
		this.y = y;
	}
	
	public void setSpeed(int speed){
		SPEED = speed;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
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

	/**
	 * @return the playerID
	 */
	public int getPlayerID() {
		return playerID;
	}

	/**
	 * @return the currentRegionId
	 */
	public int getCurrentRegionId() {
		return currentRegionId;
	}

	/**
	 * @param currentRegionId the currentRegionId to set
	 */
	public void setCurrentRegionId(int currentRegionId) {
		this.currentRegionId = currentRegionId;
	}
	
	
	
}
