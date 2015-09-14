package lab.game.startup;


import java.util.logging.Logger;

import lab.game.entities.Player;
import lab.game.gui.Region;
import lab.game.utility.Constants;

public class BotMovement {
	private final static Logger logger= Constants.getLogger(BotMovement.class.getName());
	
	/**
	 * for restricting entry to specific region, following flags need to be checked:
	 * left: 1,3,6    right: 2,4,7   up: 1,2,5   down: 3,4,8
	 * @param xPos
	 * @param yPos
	 * @return
	 */
	
	public int getRegionBoundary(Region region1,int xPos,int yPos){
		
		//Region region1 = game.getActiveRegion();
		
		int x1 = region1.getColsInRegion()-1;
		int y1 = region1.getRowsInRegion()-1;
		
		int tempXPos = xPos/Constants.getImagesize();
		int tempYPos = yPos/Constants.getImagesize();
		
		//logger.info("X Pos =" + tempXPos + "  Y Pos=" + tempYPos + " x1=" +x1 + "  y1=" + y1);
		
		if(tempXPos==0 && tempYPos==0) //extreme top left side
			return 1;
		else if(tempXPos==x1 && tempYPos==0)  //extreme top right position
			return 2;
		else if(tempXPos==0 && tempYPos==y1)	//extreme bottom left position
			return 3;
		else if(tempXPos==x1 && tempYPos==y1)	//extreme bottom right position
			return 4;
		else if(tempYPos==0)	//top row position
			return 5;
		else if(tempXPos==0)	//extreme left position
			return 6;
		else if(tempXPos==x1)	//extreme right position
			return 7;
		else if(tempYPos==y1)	//bottom row position
			return 8;
					
		return 0;		//not in the region boundary
	}
	
	/**
	 * For right way point, right direction is tried first, if obstacle then down, if still obstacle then
	 * up/left values are tried
	 */
	
	public int[] moveRight(Player player,int regCordID,int xPos,int yPos,Region region){
		
		int pos[] = new int[2];
		pos[0] = xPos;
		pos[1] = yPos;
		
		try{						
						
			if((regCordID!=2 && regCordID!=4 && regCordID!=7) && !region.isAnyObject(yPos,xPos+Constants.getImagesize())) {
				//player.setRight(true);
				pos[0] = xPos+Constants.getImagesize();	
				    		
	    	}
	    	
			else if((regCordID!=3 && regCordID!=4 && regCordID!=8) && !region.isAnyObject(yPos + Constants.getImagesize(),xPos)){
	    		//logger.info("Moving down");
	    		//player.setDown(true);
	    		 pos[1] = yPos + Constants.getImagesize();
	    		
	    	}
	    	
	    	else if((regCordID!=1 && regCordID!=2 && regCordID!=5)  && !region.isAnyObject(xPos, yPos - Constants.getImagesize())){
	    		//logger.info("Moving up");
	    		//player.setUp(true);
	    		pos[1] =yPos - Constants.getImagesize();
	    		
	    	}
	    	
	    	else if((regCordID!=1 && regCordID!=3 && regCordID!=6)  && !region.isAnyObject(xPos - Constants.getImagesize(), yPos)){
	    		//logger.info("Moving left");
	    		//player.setLeft(true);
	    		pos[0] = xPos - Constants.getImagesize();
	    	}
	    	 
	    	
			
		}catch(Exception e){
			logger.info("Exception in moveRight");
		}
		
		 return pos;
	}
	
	
	/**
	 * For left way point, left direction is tried first, if obstacle then down, if still obstacle then
	 * up/right values are tried
	 */	
	
	public int[] moveLeft(Player player,int regCordID,int xPos,int yPos,Region region){
		
		int pos[] = new int[2];
		pos[0] = xPos;
		pos[1] = yPos;
		
		if((regCordID!=1 && regCordID!=3 && regCordID!=6) && !region.isAnyObject(xPos-Constants.getImagesize(), yPos)) {
    		//player.setLeft(true);
			pos[0] = xPos - Constants.getImagesize();
    		
    	}    	
		
		else if((regCordID!=1 && regCordID!=2 && regCordID!=5)  && !region.isAnyObject(xPos, yPos - Constants.getImagesize())){
    		//player.setUp(true);    
			pos[1] = yPos - Constants.getImagesize();
    	}
		
		else if((regCordID!=3 && regCordID!=4 && regCordID!=8) && !region.isAnyObject(xPos, yPos + Constants.getImagesize())){
    		//player.setDown(true); 
			pos[1] = yPos + Constants.getImagesize();    		
    	}	
    	
    	else if((regCordID!=2 && regCordID!=4 && regCordID!=7)  && !region.isAnyObject(xPos + Constants.getImagesize(), yPos)){
    		
    		//player.setRight(true); 
    		pos[0] = xPos + Constants.getImagesize();
    	}	
		return pos;
		
	}
	
	
	/**
	 * For down way point, down direction is tried first, if obstacle then right, if still obstacle then
	 * left/up values are tried
	 */
	public int[] moveDown(Player player,int regCordID,int xPos,int yPos,Region region){
		
		int pos[] = new int[2];
		pos[0] = xPos;
		pos[1] = yPos;
		
		//logger.info("move Down called regID= " + regCordID);
		
		if((regCordID!=3 && regCordID!=4 && regCordID!=8) && !region.isAnyObject(xPos, yPos + Constants.getImagesize())){
    		
    		//player.setDown(true); 
			pos[1] = yPos + Constants.getImagesize();
    	}
		
		else if((regCordID!=2 && regCordID!=4 && regCordID!=7)  && !region.isAnyObject(xPos + Constants.getImagesize(), yPos)){
    		//logger.info("movedwon----right called");
    		//player.setRight(true); 
			pos[0] = xPos + Constants.getImagesize();
    	}		
		
		else if((regCordID!=1 && regCordID!=3 && regCordID!=6) && !region.isAnyObject(xPos-Constants.getImagesize(), yPos)) {
    		//player.setLeft(true);
			pos[0] = xPos - Constants.getImagesize();
    		
    	}    	
		
		else if((regCordID!=1 && regCordID!=2 && regCordID!=5)  && !region.isAnyObject(xPos, yPos - Constants.getImagesize())){
    		//player.setUp(true);    
			pos[1] = yPos - Constants.getImagesize();
    	}   	
			
		return pos;
		
	}
	
	/**
	 * For up way point, up direction is tried first, if obstacle then right, if still obstacle then
	 * left/down values are tried
	 */
	public int[] moveUp(Player player,int regCordID,int xPos,int yPos,Region region){
		
		int pos[] = new int[2];
		pos[0] = xPos;
		pos[1] = yPos;
		
		//logger.info("move Up called regID= " + regCordID);
		
		if((regCordID!=1 && regCordID!=2 && regCordID!=5) && !region.isAnyObject(xPos, yPos - Constants.getImagesize())){
    		
    		//player.setUp(true); 
			pos[1] = yPos - Constants.getImagesize();
    	}
		
		else if((regCordID!=2 && regCordID!=4 && regCordID!=7)  && !region.isAnyObject(xPos + Constants.getImagesize(), yPos)){
			//logger.info("moveUp ---right called");
    		//player.setRight(true); 
			pos[0] = xPos + Constants.getImagesize();
    	}		
		
		else if((regCordID!=1 && regCordID!=3 && regCordID!=6) && !region.isAnyObject(xPos-Constants.getImagesize(), yPos)) {
    		//player.setLeft(true);
			pos[0] = xPos - Constants.getImagesize();
    		
    	}    	
		
		else if((regCordID!=3 && regCordID!=4 && regCordID!=8)  && !region.isAnyObject(xPos, yPos + Constants.getImagesize())){
    		//player.setDown(true);    
			pos[1] = yPos + Constants.getImagesize();
    	}   	
			
		return pos;
		
	}
	
	
}
