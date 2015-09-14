/**
 * 
 */
package lab.game.utility;


import java.io.FileReader;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;




/**
 * 
 * The InitGame initializes the parameters after fetching from config file
 *
 */

public class InitGame {
	
	
	public InitGame() {
		
		super();
		//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();		
		init();		
		//screenSize.width = 800;
		//screenSize.height = 800;
		Constants.setScreenWidth(Constants.getScreenWidth());
		Constants.setScreenHeight(Constants.getScreenHeight());
		
		
		//Constants.setScreenWidth(600);
		//Constants.setScreenWidth(600);
	}
	
	private void init(){
		try {
			
			FileReader propReader = new FileReader("config.properties");			
			Properties prop = new Properties();
			prop.load(propReader);			
			
			if(prop.getProperty("regions")!=null)
				Constants.setRegions(Integer.parseInt(prop.getProperty("regions").trim()));
			
			if(prop.getProperty("imageSize")!=null)
				Constants.setImagesize(Integer.parseInt(prop.getProperty("imageSize").trim()));
			
			if(prop.getProperty("playerImageSize")!=null)
				Constants.setPlayerImageSize(Integer.parseInt(prop.getProperty("playerImageSize").trim()));
			
			if(prop.getProperty("scale")!=null)
				Constants.setScale(Integer.parseInt(prop.getProperty("scale").trim()));
			
			if(prop.getProperty("debugMode")!=null)
				Constants.setDebugMode(Boolean.parseBoolean(prop.getProperty("debugMode").trim()));			
			
			if(prop.getProperty("logLevel")!=null)
					Constants.setLogLevel(Level.parse(prop.getProperty("logLevel").trim()));			
			
			if(prop.getProperty("isSuperNeighbor")!=null)
				Constants.setSuperNeighbor(Boolean.parseBoolean(prop.getProperty("isSuperNeighbor").trim()));			
			
			Constants.setPort(Integer.parseInt(prop.getProperty("portNo").trim()));			
			
			if(prop.getProperty("activeRegion")!=null)
				Constants.setActiveRegion(Integer.parseInt(prop.getProperty("activeRegion").trim()));
						
			Constants.setBricksCoordinate(new HashMap<String, String>());
			Constants.setBlocksCoordinate(new HashMap<String, String>());
			//Constants.setGrassCoordinate(new HashMap<String, String>());
			
			for (int i = 0; i < Constants.getRegions(); i++) {				
				
				if(prop.getProperty("rbricks"+i)!=null)
					Constants.getBricksCoordinate().put("rbricks"+i, prop.getProperty("rbricks"+i).trim());
				if(prop.getProperty("rblocks"+i)!=null)
					Constants.getBlocksCoordinate().put("rblocks"+i, prop.getProperty("rblocks"+i).trim());				
			}
			
			if(prop.getProperty("bombTime")!=null)
				Constants.setBombTime(Integer.parseInt(prop.getProperty("bombTime").trim()));		
			
						
			if(Constants.getSuperNeighbor()==false){	//ordinary peer in startup
				if(prop.getProperty("superPeerAddress")!=null){
					Constants.setSuperPeerAddress(prop.getProperty("superPeerAddress").trim());					
				}
				if(prop.getProperty("superPeerPortNo")!=null)
					Constants.setSuperPeerPort(Integer.parseInt(prop.getProperty("superPeerPortNo").trim()));
				
				if(prop.getProperty("noOfPeers")!=null)
					Constants.setNoOfPeers(Integer.parseInt(prop.getProperty("noOfPeers").trim()));
			}
			
			
			if(prop.getProperty("gameScreenWidth")!=null)
				Constants.setScreenWidth(Integer.parseInt(prop.getProperty("gameScreenWidth").trim()));
			
			if(prop.getProperty("gameScreenHeight")!=null)
				Constants.setScreenHeight(Integer.parseInt(prop.getProperty("gameScreenHeight").trim()));

			if(prop.getProperty("peersToConnect")!=null)
				Constants.setNoOfPeers(Integer.parseInt(prop.getProperty("peersToConnect").trim()));
			
			if(prop.getProperty("consoleMode")!=null){
				if(prop.getProperty("consoleMode").trim().equals("false"))
					Constants.setConsoleMode(false);
				else
					Constants.setConsoleMode(true);
			}
			
			if(prop.getProperty("wayPoint")!=null)
				Constants.setWayPoint(prop.getProperty("wayPoint").trim());
			
			if(prop.getProperty("srcDesPoints")!=null)
				Constants.setSrcDestPoints(prop.getProperty("srcDesPoints").trim());
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
