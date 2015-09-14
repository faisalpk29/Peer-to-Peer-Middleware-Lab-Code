package lab.game.gui;

import java.util.ArrayList;
import java.util.logging.Logger;

import lab.game.entities.Player;
import lab.game.utility.Constants;

/**
 * The RegionController class divides the whole game space into regions. In this class matrix of regions is maintained e.g if there are
 * 9 regions in the game space then the matrix consists of region 1,2,3,4,5,6 comprises up regions and region 7,8,9 down regions (all regions
 * other than down region are up regions) and regions 1,2,4,5,7,8 comprises left regions and regions 3,6,9 comprises right regions. The 
 * categorization of regions to up,down,left,right is helpful in controlling player movements and entry of player from one region to other
 * region
 * @author Kamran Yaqub
 *
 */

public class RegionController {
	
	private int noOfRegions;
	private int rRows,rCols;
	private final static Logger logger = Constants.getLogger(RegionController.class.getName());
	private ArrayList<Integer> arrUpRegions = new ArrayList<Integer>();
	private ArrayList<Integer> arrDownRegions = new ArrayList<Integer>();
	private ArrayList<Integer> arrLeftRegions = new ArrayList<Integer>();
	private ArrayList<Integer> arrRightRegions = new ArrayList<Integer>();
	
	public RegionController(){
		
		noOfRegions = Constants.getRegions();	
		adjustRegionsInRowsCols(noOfRegions);
		logger.setLevel(Constants.getLogLevel());
	
	}
	
	
	/**
	 * The method adjustRegionsInRowsCols divides the regions into rows and columns based upon the number of regions. The list of up,down,
	 * left, right regions is manitained which is helpful in getting control of player entry to various regions
	 * @param noOfRegions
	 */
	
	public void adjustRegionsInRowsCols(int noOfRegions){		
			
			
			/*if(noOfRegions==4){
				rCols= rRows = 2;
			}
			
			else if(noOfRegions==6){
				rCols = 3;
				rRows = 2;
			}
			
			else if(noOfRegions==9){
				rRows = 3;
				rCols = 3;
			}
			else if(noOfRegions==12){
				rRows = 4;
				rCols = 3;
			}
			else if(noOfRegions==16){
				rRows = 4;
				rCols = 4;
			}*/
		  logger.info(""+(int)Math.sqrt(noOfRegions));
			rRows = (int)Math.sqrt(noOfRegions);
			rCols = rRows;
		
		    						
			for(int i=0;i<noOfRegions-rCols;i++){
				arrUpRegions.add(i+1);				
			}
			
			
			for(int i=rCols*(rRows-1);i<noOfRegions;i++){
				arrDownRegions.add(i+1);
				//logger.info("Down region=" + (i+1));
			}
			
			
			int count1 = 0;
			for(int i=0;i<rCols*rRows;i++){
				
				if(++count1==rCols){
					count1=0;
					continue;
				}
				
				//logger.info("Left Region======" + (i+1));
				arrLeftRegions.add(i+1);
			}
			
			for(int i=rCols-1;i<noOfRegions;i=i+rCols){
				arrRightRegions.add(i+1);
				//logger.info("Right region=" + (i+1));
			}			
			
	}
	
	private final float playerBoundaryCheck = Constants.getImagesize() *1.7f;
	
	/**
	 * This method calls the methods {@link #controlLeftRightRgionEntry(Region,Player)}, {@link #controlUpDownRegionEntry(Region,Player)}
	 *  and controls the entry to various regions. As player moves from one region to other region, the region number is changed accordingly
	 * @param r
	 * @param p
	 */
	
	public void controlRegionEntry(Region r, Player p){
		
		controlLeftRightRgionEntry(r,p);
		controlUpDownRegionEntry(r,p);		
	}
	
	
	/**
	 * This method determines if the region is at left side. If there are 9 regions in the game space then region no 3,6,9 are right regions
	 * All other are left regions i.e region no 1,2,4,5,7,8 are left regions
	 * @param regionID
	 * @return
	 */
	
	public boolean isLeftRegion(int regionID){
		
		for(int i=0;i<arrLeftRegions.size();i++){
			
			if(arrLeftRegions.get(i)==regionID)
				return true;
		}
		
		return false;			
	}
	
	
	/**
	 * This method determines if the region is at right side. If there are 9 regions in the game space then region no 3,6,9 are right regions
	 * All other are left regions
	 * @param regionID
	 * @return
	 */
	
	public boolean isRightRegion(int regionID){
		
		for(int i=0;i<arrRightRegions.size();i++){
			
			if(arrRightRegions.get(i)==regionID)
				return true;
		}
		
		return false;			
	}
	
	/**
	 * If there are 9 regions in the game space then region no 7,8,9 are down regions and regions 1,2,3,4,5,6 are up regions and region no
	 * 1,2,3 are top up regions
	 * @param regionID
	 * @return
	 */
	public boolean isUpRegion(int regionID){
		
		for(int i=0;i<arrDownRegions.size();i++){
			
			if(arrDownRegions.get(i)==regionID)
				return false;
		}
		
		return true;				
	}
	
	/**
	 * This method determines if the regions are at top e.g if there are 4 regions in the game space then region no 1 and 2 are up regions
	 * If there are 6 regions in the game space then region 1,2,3 are up regions
	 * If there are 9 regions in the game space then region no 7,8,9 are down regions and regions 1,2,3,4,5,6 are up regions and region no
	 * 1,2,3 are top up regions
	 * @param regionID
	 * @return
	 */
	
	private boolean isTopUpRegion(int regionID){
		
		if(regionID<=rCols)
			return true;
		else
			return false;
	}
	
 /**
  * This method determines whether region is a down side e.g if there are 4 regions in the game space then region 3 and 4 are at down. If
  * there are six regions in the game space then region 4,5,6 are at down. All other regions are up regions
  * @param regionID
  * @return
  */
	
  private boolean isDownRegion(int regionID){
		
		for(int i=0;i<arrDownRegions.size();i++){
			
			if(arrDownRegions.get(i)==regionID)
				return true;
		}
		
		return false;			
	}
  
  /**
   * The method determines whether region is extreme left e.g if there are 4 regions in the game space then regions 1 and 3 are extreme
   * left regions. If there are six regions in the game space then regions 1 and 4 are extreme left regions
   * @param regionID
   * @return
   */
  
  public boolean isExtremeLeftRegion(int regionID){
			 
	  int rID = 1;
	 
	  while(rID<noOfRegions){		  
		 		  
		  if(rID==regionID)
			  return true;
		  
		  rID = rID + rCols;

	  }	  	  
	  
	  return false;
  }
	
		
	/**
	 *  The method controlLeftRightRgionEntry controls the entry of the player to the left or right regions also considering the case
	 *  that the player does not crosses the left most or right most regions' boundaries
	 * @param r
	 * @param player
	 */
	
	
	public void controlLeftRightRgionEntry(Region r,Player player){
		
		int x = player.getPlayerCurXPos();		
		
		if(isExtremeLeftRegion(r.getRegionID()) && x<0){
			//logger.info("BMK4---RegionController.java--Extreme left region. X =" + x);
			//logger.info("Extreme left region. X =" + x);
			player.setPlayerCurXPos(0);
		}
		
		else if(isLeftRegion(r.getRegionID()))
		//if(r.regionID==1 || r.regionID==3)
		{
			
			//entering to region2 from region1 or region4 from region3
			if((x+ playerBoundaryCheck)>=r.rX2){
				
				
				player.rBYUpFlag = player.rBYDownFlag = false;
				player.rBXRightFlag = true;
				player.rBXLeftFlag = false;
				
				logger.info("RXRight flag is true");
				x = 0;
				player.setPlayerCurXPos(0);
			}
			
			//no regions to the left of region1 or region3
			else if(x<0){
				//logger.info("RXLeft flag is true");
				//player.setPlayerCurXPos(0);
				player.rBYUpFlag = player.rBYDownFlag = false;
				player.rBXRightFlag = false;
				player.rBXLeftFlag = true;
				x=r.rX2 - Constants.getImagesize();
				player.setPlayerCurXPos(x);
				
			}
		}
		
		else if(isRightRegion(r.getRegionID()))
		//else if(r.regionID==2 || r.regionID==4)		
		{
			
			//entering to region1 from region2 or region3 from region4
			if(x<0){
				//logger.info("BMK4---RegionController.java--entering to region 1 from region 2");
				player.rBYDownFlag = player.rBYUpFlag = false;
				
				player.rBXRightFlag = false;
				player.rBXLeftFlag = true;
				
				x=r.rX2 - Constants.getImagesize();
				player.setPlayerCurXPos(x);
			}
			
			//no further regions from region2 or region4
			else if((x+ playerBoundaryCheck)>=r.rX2)
			{	
				x = r.rX2 -Constants.getImagesize();
				player.setPlayerCurXPos(x);
			}
			
		}
		
	}
	
	/**
	 * The method controlUpDownRegionEntry controls the entry to the up or down regions also considering the case that player does not
	 * crosses the top most or bottom most regions' boundaries
	 * @param r
	 * @param player
	 */
	
	public void controlUpDownRegionEntry(Region r,Player player){
		
		int x = player.getPlayerCurXPos();
		int y = player.getPlayerCurYPos();		
		
		//Extreme top region and hence cannot go further up
		if(isTopUpRegion(r.getRegionID()) && y<0){
			
			player.setPlayerCurYPos(0);			
		}
		
		else if(isUpRegion(r.getRegionID()))
		{
			//moving towards down region
			if((y+playerBoundaryCheck) >= r.rY2){
		
			player.rBYDownFlag = true;
			player.rBYUpFlag = false;
			
			
			player.rBXLeftFlag = player.rBXRightFlag = false;
			
			y = 0;
			//x = 0;
			
			player.setPlayerCurXPos(x);
			player.setPlayerCurYPos(0);
		  }				
			
			else if(y<0){	//i.e moving towards up region
				
				player.rBYUpFlag = true;
				player.rBYDownFlag = false;				
				
				player.rBXLeftFlag = player.rBXRightFlag = false;				
				
				y = r.rY2 - Constants.getImagesize();
				
				player.setPlayerCurYPos(y);
				player.setPlayerCurXPos(x);
			}						
		
		}
		
		
		else if(isDownRegion(r.getRegionID()))
		{
			
			//moving towqrds up region
			if(y < 0){
				player.rBYUpFlag = true;
				player.rBYDownFlag = false;				
				
				player.rBXLeftFlag = player.rBXRightFlag = false;
				
				
				y = r.rY2 - Constants.getImagesize();
				
				player.setPlayerCurYPos(y);
			}
			
			//no further regions below. so restrict to last row
			else if(y+playerBoundaryCheck>=r.rY2){
				y = r.rY2 - Constants.getImagesize();
				player.setPlayerCurYPos(y);
			}
		}		
	
   }	
	
	  /**
	   * The method getRegionID gives the next region id when player moves from up to down or left to right or vice versa
	   * @param currentRegion
	   * @param player
	   * @return
	   */
	
		public int getRegionID(Region currentRegion,Player player){
			 
			 int curRegionID = currentRegion.getRegionID();
			 			 
			
			 if(player.rBYDownFlag){
										
					curRegionID = currentRegion.getRegionID() + rCols;							
										
					player.rBYDownFlag = player.rBYUpFlag = false;
					player.rBXLeftFlag = player.rBXRightFlag = false;
				}
			
			 else if(player.rBYUpFlag){
				 	
				    curRegionID = currentRegion.getRegionID() - rCols;					
										
					player.rBYDownFlag = player.rBYUpFlag = false;
					player.rBXLeftFlag = player.rBXRightFlag = false;
			 }
				
			 else if(player.rBXLeftFlag){
				   
				    curRegionID = currentRegion.getRegionID()-1;				   
				    
				  				 
					player.rBYDownFlag = player.rBYUpFlag = false;
					player.rBXLeftFlag = player.rBXRightFlag = false;
			 }
			 
			 else if(player.rBXRightFlag){				 
				 
				 curRegionID = currentRegion.getRegionID()+1;
				 
				 
				 player.rBYDownFlag = player.rBYUpFlag = false;
				 player.rBXLeftFlag = player.rBXRightFlag = false;
			 }
			 
			 			 			
			 
			 return curRegionID;
	}	
		
	
		public int getRows(){
			return rRows;
		}
		
		public int getCols(){
			return rCols;
		}
				
	
		
}
