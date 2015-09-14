package lab.game.gui;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Timer;

import lab.game.entities.MutableObject;
import lab.game.entities.Player;
import lab.game.utility.Constants;
/**
 *   @author Kamran Yaqub create on  05-05-14
 *   The Region class contains the whole map of the game screen. The game area is divided into regions where each region is identified by
 *   region ID. Whole region is divided into rows and columns obtained by dividing the screen width and height with player image size to
 *   give respectively the number of columns and rows respectively. Each region contains objects like bricks, walls, bombs. Some objects 
 *   are immutable i.e they remain the same during the whole course of game and some are mutable they may change with the passage of time
 *   e.g walls may be broken. These objects are identified by unique IDs
 */


public class Region implements ActionListener{
		
	private int[][] wallPos;
	private final static Logger logger = Constants.getLogger(Region.class.getName());
	private int[][] brickPos;
	
	
	private int noOfRowsInRegion;
	private int noOfColsInRegion;
	
	private final int brickObjectID = 11;  //ids 11-20 for immutable objects
	private final int wallObjectID = 1;  //ids 1-10 for mutable object
	private final int bombObjectID = 2;	 //ids 1-10 for mutable object
	
	private int[][] regionMap;
		
	private int testCounter = 0;

	private int regionID;
	public boolean isBombActive= false;
	private ImageManager im;
	
	public static final boolean isDebugMode = true;	
	
	public HashMap<Integer,Player> mapPlayer = new HashMap<Integer,Player>();
	
	public int rX2=0,rY2=0;	//region boundary coordinates
	//private ArrayList<StaticObject> arrBrick;
	//private ArrayList<MutableObject> arrWall;
	private ArrayList<MutableObject> arrBomb;
	
	private Game game ;
	
	public Region(ImageManager im,int regionID,Game game){
		this.im = im;
		this.regionID = regionID;
		this.game = game;
		//arrBrick = new ArrayList<StaticObject>();
		//arrWall = new ArrayList<MutableObject>();
		arrBomb = new ArrayList<MutableObject>();
		//arrGrass = new ArrayList<StaticObject>();
		logger.setLevel(Constants.getLogLevel());
		//populateMutableObjects();
		//populateImmutableObjects();
	}
	
	
	public void initRegionParams(){	
		
		try {
			//populateMutableObjects();
			//populateImmutableObjects();
			
			

		}catch(Exception e){
			logger.severe(e.getMessage());
		}
		
	}
	
	/**
	 * The method render renders the bomb if placed, immutable objects by calling method {@link #renderImmuatableObjects(Graphics)}
	 * and mutable objects by calling method {@link #renderMutableObjects(Graphics)}
	 * @param g
	 */
	
	public void render(Graphics g){
		
		
		
		try {

			if (isBombActive) {
				renderBomb(g);
			}

			renderImmuatableObjects(g);
			renderMutableObjects(g);

		}catch(Exception e){
			logger.log(Level.SEVERE, e.getMessage());
		}
				
	}
	
	/**
	 * The method renderBomb renders the bomb blasting scene in up,down,right and bottom direction while destroying the mutable objects
	 * @param g
	 */
	
	public void renderBomb(Graphics g){
		
		
		testCounter++;
		
		MutableObject bomb = arrBomb.get(0);
		
		int row1 = bomb.getCol() * Constants.getImagesize();
		int col1 = bomb.getRow() * Constants.getImagesize();
		
		if(testCounter>100){
			isBombActive = false;
			testCounter=0;
			
			arrBomb.remove(0);
			
			//logger.info("===Row = " + bomb.getRow() + "    Col=" + bomb.getCol());
			regionMap[bomb.getRow()][bomb.getCol()]=0;
			
			return;
			
		}
		
				
		
		//logger.log(Level.INFO, "TestCounter = " + testCounter +  "  Row =" + row1 + "  Col=" + col1);
		int scale1 = Constants.getImagesize() * Constants.getScale();			
		
		g.drawImage(im.getFlame(), row1 + Constants.getImagesize(), col1, scale1, scale1, null);
		g.drawImage(im.getFlame(), row1 - Constants.getImagesize(), col1, scale1, scale1, null);
		g.drawImage(im.getFlame(), row1, col1+Constants.getImagesize(), scale1, scale1, null);
		g.drawImage(im.getFlame(), row1, col1-Constants.getImagesize(), scale1, scale1, null);	
		
	}
	
	/**
	 * Displays the Immutable static objects like brick
	 * @param g
	 */
	public void renderImmuatableObjects(Graphics g){
		
		int scale1 = Constants.getImagesize()*Constants.getScale();
		
		/*for(int i=0;i<arrBrick.size();i++){
			StaticObject mo =  arrBrick.get(i);
			g.drawImage(im.getBrickTile(), mo.getRowX1(), mo.getColY1(), scale1, scale1, null);
		}
		

		for(int i=0;i<arrGrass.size();i++){
			StaticObject mo =  arrGrass.get(i);
			g.drawImage(im.getGrassTile(), mo.getRowX1(), mo.getColY1(), scale1, scale1, null);
		}*/
	}
	
	/**
	 * Displays/renders the immutable objects. these objects may change during the course of game e.g walls are broken
	 * @param g
	 */
	
	public void renderMutableObjects(Graphics g){
		
		int scale1 = Constants.getImagesize()*Constants.getScale();
				
		/*for(int i=0;i<arrWall.size();i++){
			MutableObject mo = arrWall.get(i);
			g.drawImage(im.getWallTile(), mo.getRowX1(), mo.getColY1(), scale1, scale1, null);
			logger.info("Row X = " + mo.getRowX1() + "    Col Y=" + mo.getColY1());
		}*/
		
		
		int rowPix;
		int colPix;
		
		for(int i=0;i<noOfRowsInRegion;i++){						
			for(int j=0;j<noOfColsInRegion;j++){				
				if(regionMap[i][j]==wallObjectID){
					
					//if(testFlag==true) logger.info("In render --- Wall at  i = " + i + "  j= " + j);
					rowPix = Constants.getImagesize() * i;
					colPix = Constants.getImagesize() * j;
							
					g.drawImage(im.getWallTile(), colPix,rowPix,scale1, scale1, null);
				}
				
				else if(regionMap[i][j]==brickObjectID){
					rowPix = Constants.getImagesize() * i;
					colPix = Constants.getImagesize() * j;
							
					g.drawImage(im.getBrickTile(), colPix,rowPix,scale1, scale1, null);
				}
				
				else if(regionMap[i][j]==bombObjectID){
					
					rowPix = Constants.getImagesize() * i;
					colPix = Constants.getImagesize() * j;							
					
					g.drawImage(im.getBomb(), colPix,rowPix,scale1, scale1, null);
				}
			}
			
			
		}
		
		
		/*for(int i=0;i<arrBomb.size();i++){
			
			//logger.log(Level.INFO,"Region Id =" + regionID +"  Bomb arraysize=" + arrBomb.size());
			MutableObject bomb = arrBomb.get(i);
			g.drawImage(im.getBomb(), bomb.getRowX1(), bomb.getColY1(), scale1, scale1, null);
		}*/
	}
	
	
	
	/**
	 * The method setBrickCoordinatesForRegion sets the coordinates for bricks after fetching from properties file
	 * @param brickPos
	 */
	
	public void setBrickCoordinatesForRegion(int brickPos[][]){
		
		this.brickPos = brickPos;
	}
	
  
	
	/**
	 * The method setBrickCoordinatesForRegion sets the coordinates for bricks after fetching from properties file
	 * @param wallPos
	 */
	
	public void setWallCoordinatesForRegion(int wallPos[][]){
		this.wallPos = wallPos;
	}
	
	
	
	public void createRegionMap(int rows,int cols){
		
		noOfRowsInRegion = rows;
		noOfColsInRegion = cols;
						
		regionMap = new int[rows][cols];
		
		if(brickPos!=null)
			for(int i=0;i<brickPos.length;i++){
				int row11 = brickPos[i][0];
				int col11 = brickPos[i][1];			
				
				regionMap[row11][col11] = brickObjectID;	
			}
		
		if(wallPos!=null)
		for(int i=0;i<wallPos.length;i++){
			int row11 = wallPos[i][0];
			int col11 = wallPos[i][1];
			
			//logger.info("Setting wall position=== Row11 = " + row11 + "   Col=" + col11);
			regionMap[row11][col11] = wallObjectID;
		}		
					
	}
	
	
	public void setRegionMap(int regionMap[][]){		
		
		this.regionMap = regionMap;
	}
	
	
	/**
	 * @return the regionMap
	 */
	public int[][] getRegionMap() {
		return regionMap;
	}


	private void populateObjects(int[][] objectPos , ArrayList<MutableObject> staticObject,ArrayList<MutableObject> mutable){
		/*
		for(int i=0;i<objectPos.length;i++){
			if (Util.isNull(staticObject)) {
				MutableObject mo = new MutableObject();
				mo.setRow(objectPos[i][0]);
				mo.setCol(objectPos[i][1]); 
				mo.setColY1( mo.getRow() * Constants.getImagesize());
				mo.setColY2(mo.getRow() * Constants.getImagesize() + Constants.getImagesize());
				mo.setRowX1(mo.getCol() * Constants.getImagesize());
				mo.setRowX2( mo.getCol() * Constants.getImagesize() + Constants.getImagesize());
				mutable.add(mo);
				
			}else{
				StaticObject so = new StaticObject();
				so.setRow(objectPos[i][0]);
				so.setCol(objectPos[i][1]); 
				so.setColY1( so.getRow() * Constants.getImagesize());
				so.setColY2(so.getRow() * Constants.getImagesize() + Constants.getImagesize());
				so.setRowX1(so.getCol() * Constants.getImagesize());
				so.setRowX2( so.getCol() * Constants.getImagesize() + Constants.getImagesize());
				staticObject.add(so);
			}
			
		}*/
		
	}
	
	/**
	 * Checks the existence of any object specified by the pixel position. Used in collison detection
	 * @param xPixel
	 * @param yPixel
	 * @return
	 */
	
	public boolean isAnyObject(int xPixel,int yPixel){
				
		if(xPixel<0 || yPixel<0)
			return false;
		
		//logger.info("y pixel = "+ yPixel + " YPos = " + yPixel/Constants.getImagesize() + " NoOfRows = " + noOfRowsInRegion + " X Pixel =" + xPixel + " XPos=" + xPixel/Constants.getImagesize() + "  NoOfCols=" + noOfColsInRegion);
		if(yPixel/Constants.getImagesize()>=noOfRowsInRegion || xPixel/Constants.getImagesize()>=noOfColsInRegion)			
			return false;
			
		
		int objectID = regionMap[yPixel/Constants.getImagesize()][xPixel/Constants.getImagesize()];
		//logger.info("object Id =" + objectID + " i =" + yPixel/Constants.getImagesize() + " j=" + xPixel/Constants.getImagesize());
		
		if(objectID==0 || objectID==bombObjectID)
			return false;
		else
			return true;
	}
	
	
	 /**
	  * The method renderBomb places the bomb at the position where player pressed the spacebar key while adjusting appropriately to
	 * the row and column
	  * @param x --- represents left to right in screen
	  * @param y --- represents top to bottom in screen
	  * @param playerDirection
	  */
	
	public void placeBomb(int x,int y){
		
		//logger.info("placeBomb called ........");
		MutableObject bomb =  new MutableObject();
		int col1 = (int)x/Constants.getImagesize();
		int row1 = (int)y/Constants.getImagesize();
				
		y = row1 * Constants.getImagesize();
		x = col1 * Constants.getImagesize();
		
		
		bomb.setRow(row1);
		bomb.setRowX1(x); 
		bomb.setRowX2(x + Constants.getImagesize()); 
		
		bomb.setCol(col1); 		
		bomb.setColY1(y);
		bomb.setColY2( y + Constants.getImagesize());
		
		//logger.info("row1 = " + row1 + "   col1= " + col1 + "   x=" + x + "   y="+y);
		
		arrBomb.add(bomb);
		
		//logger.info("Bomb at Row = " + row1 + "    Col1=" + col1);
		regionMap[row1][col1] = bombObjectID;
		
		int delay = Constants.getBombTime(); //milliseconds
		
		Timer t = new Timer(delay, this);
		t.setRepeats(false);
		t.start();
	}
	
	/**
	 * Fires the bomb event
	 */
	public void actionPerformed(ActionEvent evt) {
		//logger.info("Bomb event fired " );
        isBombActive = true;
        
        blastMutableObjects();
    }
	
	
	/**
	 * removes the player from game space
	 * @param playerID
	 */
	
	public void removePlayer(int playerID){
		
		if(mapPlayer.get(playerID)!=null)
			mapPlayer.remove(playerID);
	}
	
	/**
	 * Blasts the mutable objects in the range of bomb
	 */
	
	public void blastMutableObjects(){
		
		MutableObject bomb = arrBomb.get(0);
		
		int row =  bomb.getRow();
		int col = bomb.getCol();
		
					
		int i = 0;
		
		Player player = game.getPlayer();
		
		
		int playerCurRow = getPlayerCell(player.getPlayerCurYPos(),Constants.getImagesize());		
		int playerCurCol = getPlayerCell(player.getPlayerCurXPos(),Constants.getImagesize());
			
		
		player.isPlayerActive = checkObjectInbombRange(playerCurRow,playerCurCol,row,col);
		
		for(Integer playerID:mapPlayer.keySet()){
			
			Player peer = mapPlayer.get(playerID);
			
			playerCurRow = getPlayerCell(peer.getPlayerCurYPos(),Constants.getImagesize());			
			playerCurCol = getPlayerCell(peer.getPlayerCurXPos(),Constants.getImagesize());
			
			peer.isPlayerActive = checkObjectInbombRange(playerCurRow,playerCurCol,row,col);
			
			if(peer.isPlayerActive==false){
				mapPlayer.remove(playerID);
			}
			
		}
		
		//logger.info("+++Row = "+ row + "  Col=" + col + "   Region =" + regionMap[row+1][col]);
		if(col>0 && regionMap[row][col-1]<=10 && regionMap[row][col-1]>=0)
			regionMap[row][col-1] = 0;
		
		if(regionMap[row][col+1]<=10 && regionMap[row][col+1]>=0)
			regionMap[row][col+1] = 0;
		
		if(row>0 && regionMap[row-1][col]<=10 && regionMap[row-1][col]>=0)
			regionMap[row-1][col] = 0;
		
		if(regionMap[row+1][col]<=10 && regionMap[row+1][col]>=0)
			regionMap[row+1][col] = 0;	
				
	}
	
	/**
	 * Checks whether player is in the range of current bomb object
	 * @param playerCurRow
	 * @param playerCurCol
	 * @param bombRow
	 * @param bombCol
	 * @return
	 */
	private boolean checkObjectInbombRange(int playerCurRow,int playerCurCol,int bombRow,int bombCol){
		
		if(bombRow+1== playerCurRow || bombRow== playerCurRow || bombRow-1 == playerCurRow ){			
			if(bombCol+1==playerCurCol || bombCol== playerCurCol || bombCol-1 == playerCurCol)
				return false;
		}
		
		return true;
	}
		
	/**
	 * Returns the player cell number according to the x or y position. The game space according to screen width and height
	 * is divided into rows and columns. These are obtained by dividing width with image size to give number of columns
	 * and height with image size to give number of rows
	 * @param pos
	 * @param imageSize
	 * @return
	 */
	private int getPlayerCell(int pos,int imageSize){
		
		return ((int) Math.ceil(pos/ imageSize));
	}
	
	/**
	 * Draws the player over GUI screen
	 * @param g
	 */
	
	public void renderPeer(Graphics g){
		
		for(Integer playerID : mapPlayer.keySet()){
			Player player = mapPlayer.get(playerID);
			g.drawImage(im.getPeer(), player.getPlayerCurXPos(), player.getPlayerCurYPos(), Constants.getPlayerImageSize(), Constants.getPlayerImageSize(), null);
		}		
		
	}
	
	/**
	 * Updates the position of player in map
	 * @param playerID
	 * @param x
	 * @param y
	 * @param imageManager
	 */
	
	public void insertUpdatePeers(int playerID,int x,int y,ImageManager imageManager){
		
		Player player1 = mapPlayer.get(playerID);
		
		//First time player is appearing. Insert in playerlist
		if(player1==null){
			player1 = new Player(x,y,imageManager,this.game,this.game.getPlayer().getController());			
			mapPlayer.put(playerID, player1);			
		}
		
		player1.setPlayerCurXPos(x);
		player1.setPlayerCurYPos(y);
	}
	
	
	
	public void setRegionBoundary(int x,int y){
		rX2 = x;
		rY2 = y;
	}


	/**
	 * @return the regionID
	 */
	public int getRegionID() {
		return regionID;
	}


	/**
	 * @param regionID the regionID to set
	 */
	public void setRegionID(int regionID) {
		this.regionID = regionID;
	}
	
	public int getRowsInRegion(){
		return noOfRowsInRegion;
	}
	
	public int getColsInRegion(){
		return noOfColsInRegion;
	}
	
}
