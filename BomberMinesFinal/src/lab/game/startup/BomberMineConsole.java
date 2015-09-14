package lab.game.startup;


import java.io.IOException;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import lab.game.entities.Player;
import lab.game.gui.ConsoleMode;
import lab.game.gui.Game;
import lab.game.gui.Region;
import lab.game.gui.RegionController;
import lab.game.network.Network;
import lab.game.network.SuperPeerHandler;
import lab.game.utility.Constants;
import lab.game.utility.InitGame;
import lab.game.utility.Message;
import lab.game.utility.Util;

/**
 * The BomberMineConsole.java class runs the game in console mode. This is especially helpful when running over
 * testbed without any GUI support. Different movements of player are defined : left to right and then right to left,
 * top to down and down to top, from start point to end point and from end point to start point, random movement
 * The specific movement is configured via configuration file
 *
 *
 */

public class BomberMineConsole extends Thread{
	private final static Logger logger= Constants.getLogger(BomberMineConsole.class.getName());
	private BotMovement botMovement = new BotMovement();
	private enum WayPoint{Right,Left,Down,Up};
	private final int threadSleepTime = 500;	
	
	private String superPeerAddress;
	private int superPeerPortNo;
	
	private Game game;
	private Player player;
	
	//resets the player position to start or end in case of horizontal or vertical motion to avoid player struck
	private int posResetTime = 50;  
	
	public BomberMineConsole(){
		
	}

	/**
	 * Creates player to run in console mode
	 * @param game
	 * @return
	 */
	public Player createPlayer(Game game){
		
		Player player1 =null;		
		
	    player1 = new Player(game);    
	    player1.setSpeed(Constants.getImagesize());		
		player1.setCurrentRegionId(game.getActiveRegion().getRegionID());		
				
		return player1;
	}
	
	public void leavePlayer(){	
   	 
	try {	         
        	if (game.getPlayer().isConnected() || SuperPeerHandler.getConnectedPeers() > 0) {
        		//logger.info("Disconnecting player ..." + game.getPlayer().getPlayerID());
        		
        		//assuming in console mode peer lives in current region
        		game.getPlayer().getController().getNetwork().disconnect(false,game.getActiveRegion().getRegionID(),game.getActiveRegion().getRegionID());
								
			}        	
        }catch (Exception ex) {
        	
        		logger.info(ex.getMessage());
        }
        
        
	}

	/**
	 * Selects the player movement and moves the player according to the criteria specified in configuration file
	 * 
	 */
	
	public void run(){		
			
		if(Constants.getWayPoint().equals("right")){			
			moveHorizontally();
		}
		
		else if(Constants.getWayPoint().equals("down")){			
			moveVertically();
		}
		
		else if(Constants.getWayPoint().equals("point")){
			moveToSpecificPoint();
		}		
		
		else if(Constants.getWayPoint().equals("randomPoint")){
			moveToRandomPoint();			
		}
		
		else if(Constants.getWayPoint().equals("verticalRegion")){
			moveFromRegionVertically();
		}
		
		else if(Constants.getWayPoint().equals("horizontalRegion")){
			moveFromRegionHorizontally();
		}
	}
	
/**
 *  In moveToRandomPoint method, the next movement of player is randomly selected (left,right,top or bottom) and
 *  player continues to move in such a fashion 	
 */
  
 public void moveToRandomPoint(){
		
		Region region = game.getActiveRegion();
		Player player = game.getPlayer();
		Util util = new Util();
		
		
		
		int extremeX = region.rX2;
		int extremeY = region.rY2;
		
		int curX=0,curY=0;
		int prevX = 0,prevY=0;
		boolean randomFlag = false;
		
		try{			
		
		 while(player.isConnected() || SuperPeerHandler.getConnectedPeers()>0)
		    {	    	    	    	
		    	curX = player.getPlayerCurXPos();
		    	curY = player.getPlayerCurYPos();
		    	
		    	randomFlag = false;
		    	
		    	while(randomFlag==false){
		    	int pointX1 = generateRandRange(1,4);
		    	
		    	//1 left, 2 right, 3 top, 4 down
		    	if(pointX1 ==1 && curX!=0 && !region.isAnyObject(curX - Constants.getImagesize(), curY)){
		    		curX = curX - Constants.getImagesize();
		    		randomFlag=true;
		    	}
		    	
		    	//right
		    	else if(pointX1 ==2 && curX!=extremeX && !region.isAnyObject(curX + Constants.getImagesize(), curY)){
		    		curX = curX + Constants.getImagesize();
		    		randomFlag=true;
		    	}
		    	
		    	//top
		    	else if(pointX1 ==3 && curY!=0 && !region.isAnyObject(curX , curY - Constants.getImagesize())){
		    		curY = curY - Constants.getImagesize();
		    		randomFlag=true;
		    	}
		    	
		    	//bottom
		    	else if(pointX1 ==4 && curY!=extremeY && !region.isAnyObject(curX , curY + Constants.getImagesize())){
		    		curY = curY + Constants.getImagesize();
		    		randomFlag=true;
		    	}
		    	
		    	}
		    	
		    					
				Message message = new Message();
				message.setPlayerId(player.getPlayerID());
				message.setMessageId(Constants.PPU);
				message.setRegionId(region.getRegionID());
				
				message.setxPlayerPos(curX);
				message.setyPlayerPos(curY);			
				player.getController().sendPlayerPos(message);
			
				player.setPlayerCurXPos(curX);
				player.setPlayerCurYPos(curY);				
				
				
				Thread.sleep(threadSleepTime);
		    }//end while
		 
		   } catch(Exception e){
		    	logger.info("Exception in BomberMineConsole.java-- method moveToSpecificPoint  = " + e.toString());
		    }
	}
 
	
	/**
	 * Moves player from right region to left region if current region is not in extreme left position or moves player from
	 * left region to right region if current region is at extreme left position
	 */
 
 	public void moveFromRegionHorizontally(){		
		
		Region activeRegion = game.getActiveRegion();
		Player player = game.getPlayer();
		RegionController regionController  = game.getRegionController();
		
		boolean isLeftRegion = regionController.isLeftRegion(activeRegion.getRegionID());
		int offset = Constants.getImagesize();
		logger.info("Is left region = " + isLeftRegion);
		
		if(isLeftRegion==false){	//i.e move player from right region to left
			offset = -Constants.getImagesize();
		}
		
		int posX = 10 * Constants.getImagesize();
		int posY = 0;
		
		boolean flagNewReg = false;
		
		int countFlag =0;
		
		while(true){
			try{	
				
				
				if(isLeftRegion){	//check for peer moving from left region to right
					if(posX>activeRegion.rX2)	//if region right boundary is reached and entering towards right region
						flagNewReg = true;
				}
				
				else if(!isLeftRegion){	//check for peer moving from right region to left
					if(posX<0)	//if region left boundary is reached and entering from right region to left region
						flagNewReg = true;
				}
				
				
				if(flagNewReg)
				{	
					flagNewReg = false;
					
					++countFlag;
					
					if(countFlag==2){	//check just one region is changed. After that if region boundary is reached then move vertically in the same region
						
						moveVertically();
						break;						
					}
					
					
					
					int regID = regionController.getRegionID(activeRegion, player);					
					
					activeRegion = game.getRegion(regID-1);					
					
					Region r1 = game.getRegion(regID-1);					
					
					player.detectCollision(r1);					
					
					
					if(!isLeftRegion)
						posX = r1.rX2 + offset;						
					else
						posX = offset;
					
					player.setPlayerCurXPos(posX);
									
					Thread.sleep(threadSleepTime*2);
				}
				
				//sends player position change message
				Message message = new Message();				
				message.setPlayerId(player.getPlayerID());				
				message.setMessageId(Constants.PPU);
				
				
				message.setxPlayerPos(posX);
				message.setyPlayerPos(posY);
				
				
				message.setRegionId(activeRegion.getRegionID());			
				
				player.getController().sendPlayerPos(message);					
				
				posX = posX + offset;
				player.setPlayerCurXPos(posX);
				player.setPlayerCurYPos(posY);				
				
				player.detectCollision(activeRegion);
				
				
				Thread.sleep(threadSleepTime);	//wait for some time before next position is changed			
				
			
			}catch(Exception e){
				logger.info("Exception In BomberMineconsole.java in method moveFromOneRegionToOther e =" + e.toString());
			}
		}
		
	}
 	
 	/**
	 * Moves player from top region to bottom region if current region is not in  top position or moves player from
	 * bottom region to up region if current region is not at top position
	 */
 
 	public void moveFromRegionVertically(){		
		
		Region activeRegion = game.getActiveRegion();
		Player player = game.getPlayer();
		RegionController regionController  = game.getRegionController();
		
		boolean isUpRegion = regionController.isUpRegion(activeRegion.getRegionID());
		int offset = Constants.getImagesize();
		//logger.info("Is Up region = " + isUpRegion);
		
		if(isUpRegion==false){	//i.e move player from up region to down
			offset = -Constants.getImagesize();
		}
		
		int posX = 10 * Constants.getImagesize();
		int posY =  Constants.getImagesize();
		
		boolean flagNewReg = false;
		
		int countFlag =0;
		
		while(true){
			try{	
				
				
				if(isUpRegion){	//check for peer moving from up region to down
					if(posY>activeRegion.rY2){	//if region right boundary is reached and entering towards down region
						flagNewReg = true;
					logger.info("check for moving from up region to down");	
					}
				}
				
				else if(!isUpRegion){	//check for peer moving from down region to up
					if(posY<0){	//if region up boundary is reached and entering from down region to up region
						flagNewReg = true;
						logger.info("check for moving from down region to up");
					}
				}
				
				
				if(flagNewReg)
				{	
					flagNewReg = false;
					
					++countFlag;
					
					if(countFlag==2){	//check just one region is changed. After that if region boundary is reached then move horizontally in the same region
						
						moveHorizontally();
						break;						
					}
					
					
					
					int regID = regionController.getRegionID(activeRegion, player);					
					
					activeRegion = game.getRegion(regID-1);					
					
					Region r1 = game.getRegion(regID-1);					
					
					//logger.info("???New Region ID = " + r1.getRegionID());
					
					player.detectCollision(r1);					
					
					
					if(isUpRegion)
						posY =  offset;						
					else
						posY = r1.rY2 + offset;
					
					player.setPlayerCurXPos(posY);
									
					Thread.sleep(threadSleepTime*2);
					
					
				}
				
				//sends player position change message
				Message message = new Message();				
				message.setPlayerId(player.getPlayerID());				
				message.setMessageId(Constants.PPU);
				
				//logger.info("PosX = " + posX + "  PosY= " + posY);
				message.setxPlayerPos(posX);
				message.setyPlayerPos(posY);
				
				
				message.setRegionId(activeRegion.getRegionID());			
				
				player.getController().sendPlayerPos(message);					
				
				posY = posY + offset;
				player.setPlayerCurXPos(posX);
				player.setPlayerCurYPos(posY);				
				
				player.detectCollision(activeRegion);
				
				
				Thread.sleep(threadSleepTime);	//wait for some time before next position is changed			
				
			
			}catch(Exception e){
				logger.info("Exception In BomberMineconsole.java in method moveFromTopRegionToBottom e =" + e.toString());
			}
		}
		
	}
	
	
	/**
	 * In this method player moves from up to down and from down to up
	 */
	
	public void moveVertically(){
		Region region = game.getActiveRegion();
	    
		   try{		
			WayPoint wayPoint =  WayPoint.Down;  
		    
			Player player = game.getPlayer();
			
			long curTime = System.currentTimeMillis()/1000;
			long prevTimeMark = curTime;
		    
		   while(player.isConnected() || SuperPeerHandler.getConnectedPeers()>0)
		    {	    	    	    	
		    	int xPos = player.getPlayerCurXPos();
		    	int yPos = player.getPlayerCurYPos();
		    	int pos[] = null;
		    	//logger.info("X1 = " + game.getPlayer().getPlayerCurXPos() + "  Y1= " + game.getPlayer().getPlayerCurYPos());
		    	
		    	int regCordID = botMovement.getRegionBoundary(game.getActiveRegion(), xPos, yPos);
		    	
		    	Message message = new Message();
				message.setPlayerId(player.getPlayerID());
				message.setMessageId(Constants.PPU);
				message.setRegionId(region.getRegionID());
					    	
		    	
		    	if(wayPoint==WayPoint.Down){
		    		//logger.info("1Down======== regId  = " + botMovement.getRegionBoundary(game.getActiveRegion(), xPos, yPos));
		    		pos = botMovement.moveDown(player,regCordID,xPos,yPos,region);
		    		//logger.info("2Down======== regId  = " + botMovement.getRegionBoundary(game.getActiveRegion(), xPos, yPos));
		    		
		    	 if(regCordID==3 || regCordID==4 || regCordID==8){	//extreme down  position
		    		 wayPoint = WayPoint.Up;
		    		 // logger.info("Down====Up flag set"); 
		    	 }
		    	}
		    	
		    	
		    	if(wayPoint==WayPoint.Up){
		    		//logger.info("1Up======== regId  = " + botMovement.getRegionBoundary(game.getActiveRegion(), xPos, yPos));
		    		pos = botMovement.moveUp(player,regCordID,xPos,yPos,region);
		    		
		    		//logger.info("2Up======== regId  = " + botMovement.getRegionBoundary(game.getActiveRegion(), xPos, yPos));
		    	if( regCordID==2 || regCordID==1 || regCordID==5){	//extreme up position
		    		 wayPoint = WayPoint.Down;
		    		 //logger.info("Up====Down flag set");	 
		    	}
		    	 		    		 
		    	}
		    	
		    	
		    	curTime = System.currentTimeMillis()/1000;
		    	//if player is struck between some points due to some scenarios, in order to get rid of this. 
		    	//To show movement, player is moved to extreme top or extreme left bottom side
		    	if(curTime-prevTimeMark>posResetTime){
		    		prevTimeMark = curTime;
		    		
		    		pos[0] = 0;	//x position to make sure that player is in upper row
		    		
		    		if(pos[1]>=region.rY2/2)
		    			pos[1] = 0;
		    		else
		    			pos[1] = region.rY2-Constants.getImagesize();
		    	}
		    	
		    	message.setxPlayerPos(pos[0]);
				message.setyPlayerPos(pos[1]);			
				player.getController().sendPlayerPos(message);
				
				player.setPlayerCurXPos(pos[0]);
				player.setPlayerCurYPos(pos[1]);
				//logger.info("====send message");
				
				Thread.sleep(threadSleepTime);
		    }
		    
		    } catch(Exception e){
		    	logger.info("Exception in runBots =" + e.toString());
		     }
	}
	
	/**
	 * moves the peer from left to right and right to left
	 */
	public void moveHorizontally(){
		
		Region region = game.getActiveRegion();
	    
		   try{		
			WayPoint wayPoint =  WayPoint.Right;  
		    
			Player player = game.getPlayer();	
			
			long curTime = System.currentTimeMillis()/1000;
			long prevTimeMark = curTime;
		    
		   while(player.isConnected() || SuperPeerHandler.getConnectedPeers()>0)
		    {	    	    	    	
			    
			    
			    int xPos = player.getPlayerCurXPos();
		    	int yPos = player.getPlayerCurYPos();
		    	int pos[] = null;
		    	//logger.info("X1 = " + game.getPlayer().getPlayerCurXPos() + "  Y1= " + game.getPlayer().getPlayerCurYPos());
		    	
		    	int regCordID = botMovement.getRegionBoundary(game.getActiveRegion(), xPos, yPos);
		    	
		    	Message message = new Message();
				message.setPlayerId(player.getPlayerID());
				message.setMessageId(Constants.PPU);
				message.setRegionId(region.getRegionID());
				    	 
		    	if(wayPoint==WayPoint.Right){
		    		pos = botMovement.moveRight(player,regCordID,xPos,yPos,region);
		    		//Thread.sleep(threadSleepTime);
		    	 if(regCordID==2 || regCordID==4 || regCordID==7)	//extreme right position
		    		 wayPoint = WayPoint.Left;
		    	}
		    	
		    	
		    	if(wayPoint==WayPoint.Left){
		    		pos = botMovement.moveLeft(player,regCordID,xPos,yPos,region);
		    		//Thread.sleep(threadSleepTime);
		    	 if(regCordID==1 || regCordID==3 || regCordID==6)	//extreme left position
		    		 wayPoint = WayPoint.Right;
		    	}
		    	
		    	if(wayPoint==WayPoint.Down){
		    		
		    		pos =botMovement.moveDown(player,regCordID,xPos,yPos,region);
		    		//Thread.sleep(threadSleepTime);
		    	 if(regCordID==3 || regCordID==4 || regCordID==8)	
		    		 wayPoint = WayPoint.Right;
		    	}
		    	
		    	curTime = System.currentTimeMillis()/1000;
		    	//if player is struck between some points due to some scenarios, in order to get rid of this. 
		    	//To show movement, player is moved to extreme left or extreme top right side
		    	if(curTime-prevTimeMark>posResetTime){
		    		prevTimeMark = curTime;
		    		
		    		pos[1] = 0;	//y position to make sure that player is in upper row
		    		
		    		if(pos[0]>=region.rX2/2)
		    			pos[0] = 0;
		    		else
		    			pos[0] = region.rX2-Constants.getImagesize();
		    	}
		    	
		    	message.setxPlayerPos(pos[0]);
				message.setyPlayerPos(pos[1]);			
				player.getController().sendPlayerPos(message);
				
				player.setPlayerCurXPos(pos[0]);
				player.setPlayerCurYPos(pos[1]);
				//logger.info("====send message");
				
				Thread.sleep(threadSleepTime);
		    }
		    
		    } catch(Exception e){
		    	logger.info("Exception in runBots =" + e.toString());
		     }
		
	}
	
	
	/**
	 * In this method player moves to specific point and then comes back again. The specific point is set in
	 *  configuration file
	 */
	
	public void moveToSpecificPoint(){
		
		Region region = game.getActiveRegion();
		Player player = game.getPlayer();
		Util util = new Util();
		
		int arrSrcDes[][] = util.extractRegionCoordinates(Constants.getSrcDestPoints(),region.getRowsInRegion(),region.getColsInRegion());		
		
		int x1 = arrSrcDes[0][0] * Constants.getImagesize();
		int y1 = arrSrcDes[0][1] * Constants.getImagesize();
		
		int x2 = arrSrcDes[1][0] * Constants.getImagesize();
		int y2 = arrSrcDes[1][1] * Constants.getImagesize();
		
		
		player.setPlayerCurXPos(x1);
		player.setPlayerCurYPos(y1);
		
		int extremeX = region.rX2;
		int extremeY = region.rY2;
		
		int curX=0,curY=0;
		int prevX = 0,prevY=0;
		
		try{			
		
		 while(player.isConnected() || SuperPeerHandler.getConnectedPeers()>0)
		    {	    	    	    	
		    	curX = player.getPlayerCurXPos();
		    	curY = player.getPlayerCurYPos();
		    	
		    	if(Math.abs(curX-x2)>= Math.abs(curY-y2)){//logger.info("In X loop");
			    	if(Math.abs(curX-x2)>0){
						if(curX<x2){
							if(curX!=extremeX && !region.isAnyObject(curX + Constants.getImagesize(), curY))		//no collision
								curX = curX + Constants.getImagesize();
							else{	//collision try different options
								
								if(curY!=extremeY && !region.isAnyObject(curX , curY + Constants.getImagesize()))  //try bottom
									curY = curY + Constants.getImagesize();
								else if(curY!=0 && !region.isAnyObject(curX , curY - Constants.getImagesize()))  //try up
									curY = curY - Constants.getImagesize();
								else if(curX!=0 && !region.isAnyObject(curX - Constants.getImagesize(), curY))		//try left 
									curX = curX - Constants.getImagesize();
								
							}
						}
						else if(curX>x2){
							if(curX!=0 && !region.isAnyObject(curX - Constants.getImagesize(), curY))		//no collision
								curX = curX - Constants.getImagesize();
							
							else{	//collision try different options
																 
							    if(curY!=0 && !region.isAnyObject(curX , curY - Constants.getImagesize()))  //try up
									curY = curY - Constants.getImagesize();
							    else if(curY!=extremeY && !region.isAnyObject(curX , curY + Constants.getImagesize()))  //try bottom
									curY = curY + Constants.getImagesize();
							    else if(curX!=0 && !region.isAnyObject(curX - Constants.getImagesize(), curY))		//try left 
										curX = curX - Constants.getImagesize();
								
							}
							
						}
			    	}
		    	}
		    	
		    	else{
		    		if(Math.abs(curY-y2)>0){//logger.info("In Y loop");
						if(curY<y2){
							if(curY!=extremeY && !region.isAnyObject(curX , curY + Constants.getImagesize()))
								curY = curY + Constants.getImagesize();
							
							else{	//collision try different options
								 
							    if(curX!=extremeX && !region.isAnyObject(curX + Constants.getImagesize(), curY))  //try right
									curX = curX + Constants.getImagesize();
							    else if(curX!=0 && !region.isAnyObject(curX - Constants.getImagesize(), curY))		//try left 
										curX = curX - Constants.getImagesize();
							    else if(curY!=0 && !region.isAnyObject(curX , curY - Constants.getImagesize()))  //try up
									curY = curY - Constants.getImagesize();								
							}
						}
						else if(curY>y2){
							
							if(curY!=0 && !region.isAnyObject(curX , curY - Constants.getImagesize()))
								curY = curY - Constants.getImagesize();
							
							else{	//collision try different options
								 
							    if(curX!=extremeX && !region.isAnyObject(curX + Constants.getImagesize(), curY))  //try right
									curX = curX + Constants.getImagesize();
							    else if(curX!=0 && !region.isAnyObject(curX - Constants.getImagesize(), curY))		//try left 
										curX = curX - Constants.getImagesize();
							    else if(curY!=extremeY && !region.isAnyObject(curX , curY + Constants.getImagesize()))  //try down
									curY = curY + Constants.getImagesize();								
							}
						}//end else if curY
			    	}
		    	}
		
				//logger.info("BombermineConsole.java=====Cur X = "+ curX + "  Cur Y = " + curY);
				
				Message message = new Message();
				message.setPlayerId(player.getPlayerID());
				message.setMessageId(Constants.PPU);
				message.setRegionId(region.getRegionID());
				
				message.setxPlayerPos(curX);
				message.setyPlayerPos(curY);			
				player.getController().sendPlayerPos(message);
			
				player.setPlayerCurXPos(curX);
				player.setPlayerCurYPos(curY);
				
				if(curX==x2 && curY==y2){
					
					if(Constants.getWayPoint().equals("randomPoint")){
						while(true){
						x2 = generateRandRange(0,region.getColsInRegion()) * Constants.getImagesize();
						y2 = generateRandRange(0,region.getRowsInRegion())* Constants.getImagesize();
						
						//logger.info("X2 = " + x2 + "  Y2= " + y2);
						
						if(!region.isAnyObject(x2,y2))
						  break;
						}
					}
					
					else {						
						x2 = x1;
						y2 = y1;
					}
						
					x1 = curX;
					y1 = curY;
				}
				//logger.info("====send message");
				
				Thread.sleep(threadSleepTime);
		    }//end while
		 
		   } catch(Exception e){
		    	logger.info("Exception in BomberMineConsole.java-- method moveToSpecificPoint  = " + e.toString());
		    }
	}
	
	
	public void setSuperAddressPort(String superPeerAddress,int superPeerPortNo){
		this.superPeerAddress = superPeerAddress;
		this.superPeerPortNo = superPeerPortNo;
	}
	
	public void setGame(Game game){
		this.game = game;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	
	/*
	    connects to supe peer and then starts the thread where player is moved automatically according to the movement type
	 	set in configuration file
	*/
	public void startGame(boolean isSuper){
		//logger.info("selfPortNo ="+Constants.getPort());
		ConsoleMode consoleMode = new ConsoleMode();
		setGame(consoleMode);		
		player = createPlayer(consoleMode);			
		player.setSpeed(Constants.getPlayerImageSize());
		consoleMode.setPlayer(player);
		
		if(!isSuper){
			
			player.connectToPeer(superPeerAddress,superPeerPortNo);
		}
									
		try{Thread.sleep(500); } catch(Exception e){ }
		
		//is there any acknowledgment that player is connected or not???
		int xPos = player.getPlayerCurXPos();
		int yPos = player.getPlayerCurYPos();
					
		start();		//After connecting starts the peer movement according to configuration parameters
		Constants.setPort(Constants.getPort()+1);		
	
	}
	
	/*
	 * Runs the game in console mode after initializing parameters from configuration file
	 */
	
	public void runBombermineConsole(){
		
		FileHandler fileHandler;
		try {
			fileHandler = new FileHandler("./logs/BomberMines-"+Constants.getPort()+"-%u.txt");
			SimpleFormatter formatter = new SimpleFormatter();  
	        fileHandler.setFormatter(formatter);
	        Constants.setFileHandler(fileHandler);
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
       		
		
		String superPeerAddress = Constants.getSuperPeerAddress();
		int superPeerPortNo = Constants.getSuperPeerPort();		//port number of super peer to which this peer is going to connect
		int noOfPeers = Constants.getNoOfPeers();	//no of peers run from this instance of game

		
		BomberMineConsole bomberMineConsole[] = new BomberMineConsole[noOfPeers];		
		
		for(int i=0;i<noOfPeers;i++){
			bomberMineConsole[i] = new BomberMineConsole();
			bomberMineConsole[i].setSuperAddressPort(superPeerAddress,superPeerPortNo);
			bomberMineConsole[i].startGame(false);
			//if there are more than one peers then some delay is given before next per starts motion
			try{Thread.sleep(3000);	} catch(Exception e){}
			
		}
	}
	
	/**
	 * gets the random number between min and max values specified 
	 * @param min
	 * @param max
	 * @return
	 */
	public int generateRandRange(int min, int max) {
	   
	    Random rand = new Random();	    
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	   
	    
	    return randomNum;
	}
	
	
}