package lab.game.gui;

public class ConsoleMode extends Game {

	public ConsoleMode(){		
		super();			
	}
	
	public  void insertUpdatePeers(int regionID,int playerID,int xPosition,int yPosition,boolean isStateUpdate,int regionMap[][]){
		
		super.insertUpdatePeers(regionID, playerID, xPosition, yPosition, isStateUpdate, regionMap);
		
		//logger.info("In ConsoleMode.java-----Region ID =" + regionID + "  x=" + xPosition + " y=" + yPosition);
		if(!isStateUpdate)
			region[regionID-1].insertUpdatePeers(playerID,xPosition,yPosition,null);
	}

	
	public ToolBar getToolBar() {
		// TODO Auto-generated method stub
		return null;
	}

}

