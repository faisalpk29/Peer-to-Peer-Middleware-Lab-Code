package lab.game.utility;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Constants {
	//private final static Logger logger= Constants.getLogger(Constants.class.getName());
	private static FileHandler fileHandler;
	public static Level logLevel = Level.INFO;
	private static boolean isDebugMode = true;
	private static int screenWidth=600;
	private static int screenHeight=600;
	private static int scale = 1; 
	private static int imagesize = 32;
	private static int playerImageSize = 24;
	private static int regions = 4;
	private static int port ;
	private static  int activeRegion =0;
	private static  boolean isSuperNeighbor = false;
	private static Map<String,String> bricksCoordinate;
	private static Map<String,String> blocksCoordinate;
	private static int bombTime = 3000;	
	public static String superPeerAddress;
	public static int superPeerPort;
	public static int noOfPeers=1;	//default no of peers	
	public static boolean isConsoleMode = true;
	public static String strWayPoint = "right";  //default movement of player is from left to right
	public static String strSrcDestPoints ="0 0,0 5";	//first is source and then after comma destination row col

	public static final String PPU ="PU";
	public static final String PB ="PB";
	public static final String PEER_LEAVE ="LV";
	public static final String PEER_JOIN ="JN";
	public static final String STATE_UPDATE = "SU";
	public static final String NEIGHBOR_UPDATE = "NU";
	public static final String REGION_CHANGE = "RC";
	
	public static String IP_ADDRESS ;
	
	
	static {
		try {

			IP_ADDRESS = InetAddress.getLocalHost().getHostAddress().toString();
		} catch (UnknownHostException e) {

			//logger.severe(e.getMessage());

		}
	}
	public static String getMyIpAddress() {
		
			return IP_ADDRESS + ":"+Constants.getPort();
	}
	/**
	 * @return the screenWidth
	 */
	public static int getScreenWidth() {
		return screenWidth;
	}
	/**
	 * @param screenWidth the screenWidth to set
	 */
	public static void setScreenWidth(int screenWidth) {
		Constants.screenWidth = screenWidth;
	}
	/**
	 * @return the screenHeight
	 */
	public static int getScreenHeight() {
		return screenHeight;
	}
	/**
	 * @param screenHeight the screenHeight to set
	 */
	public static void setScreenHeight(int screenHeight) {
		Constants.screenHeight = screenHeight;
	}
	/**
	 * @return the scale
	 */
	public static int getScale() {
		return scale;
	}
	/**
	 * @param scale the scale to set
	 */
	public static void setScale(int scale) {
		Constants.scale = scale;
	}
	/**
	 * @return the imagesize
	 */
	public static int getImagesize() {
		return imagesize;
	}
	/**
	 * @param imagesize the imagesize to set
	 */
	public static void setImagesize(int imagesize) {
		Constants.imagesize = imagesize;
	}
	/**
	 * @return the playerImageSize
	 */
	public static int getPlayerImageSize() {
		return playerImageSize;
	}
	/**
	 * @param playerImageSize the playerImageSize to set
	 */
	public static void setPlayerImageSize(int playerImageSize) {
		Constants.playerImageSize = playerImageSize;
	}
	/**
	 * @return the regions
	 */
	public static int getRegions() {
		return regions;
	}
	/**
	 * @param regions the regions to set
	 */
	public static void setRegions(int regions) {
		Constants.regions = regions;
	}
	/**
	 * @return the isdebugmode
	 */
	public static boolean isIsdebugmode() {
		return isDebugMode;
	}
	/**
	 * @return the bricksCoordinate
	 */
	public static Map<String, String> getBricksCoordinate() {
		return bricksCoordinate;
	}
	
//	public static Map<String, String> getGrassCoordinate() {
//		return grassCoordinate;
//	}
	/**
	 * @param bricksCoordinate the bricksCoordinate to set
	 */
	public static void setBricksCoordinate(Map<String, String> bricksCoordinate) {
		Constants.bricksCoordinate = bricksCoordinate;
	}
	
//	public static void setGrassCoordinate(Map<String, String> grassCoordinate) {
//		Constants.grassCoordinate = grassCoordinate;
//	}
	/**
	 * @return the blocksCoordinate
	 */
	public static Map<String, String> getBlocksCoordinate() {
		return blocksCoordinate;
	}
	/**
	 * @param blocksCoordinate the blocksCoordinate to set
	 */
	public static void setBlocksCoordinate(Map<String, String> blocksCoordinate) {
		Constants.blocksCoordinate = blocksCoordinate;
	}
	
	
	
	public static boolean isDebugMode() {
		return isDebugMode;
	}
	public static void setDebugMode(boolean isDebugMode) {
		Constants.isDebugMode = isDebugMode;
	}
	/**
	 * @return the port
	 */
	public static int getPort() {
		return port;
	}
	
	/**
	 * @return the logLevel
	 */
	public static Level getLogLevel() {
		return logLevel;
	}
	/**
	 * @param logLevel the logLevel to set
	 */
	public static void setLogLevel(Level logLevel) {
		Constants.logLevel = logLevel;
	}
	public static boolean isSuperNeighbor() {
		return isSuperNeighbor;
	}
	public static void setSuperNeighbor(boolean isSuperNeighbor) {
		Constants.isSuperNeighbor = isSuperNeighbor;
	}
	
	public static boolean getSuperNeighbor(){		
		return Constants.isSuperNeighbor;
	}
	public static void setPort(int port) {
		Constants.port = port;
	}
	public static int getActiveRegion() {
		return activeRegion;
	}
	public static void setActiveRegion(int activeRegion) {
		Constants.activeRegion = activeRegion;
	}
	public static FileHandler getFileHandler() {
		return fileHandler;
	}
	public static void setFileHandler(FileHandler fileHandler) {
		Constants.fileHandler = fileHandler;
	}
	
	
	public static void setBombTime(int bombTime1){
		bombTime = bombTime1;
	}
	
	public static int getBombTime(){
		return bombTime;
	}
	
	public static void setSuperPeerAddress(String superPeerAddress1){
		superPeerAddress = superPeerAddress1;
	}
	
	public static String getSuperPeerAddress(){
		return superPeerAddress;
	}
	
	public static void setSuperPeerPort(int superPeerPort1){
		superPeerPort = superPeerPort1;
	}
	
	public static int getSuperPeerPort(){
		return superPeerPort;
	}
	
	public static void setNoOfPeers(int noOfPeers1){
		noOfPeers = noOfPeers1;
	}
	
	public static int getNoOfPeers(){
		return noOfPeers;
	}
	
	  
	
	public static void setConsoleMode(boolean isConsoleMode1){
		isConsoleMode = isConsoleMode1;
	}
	
	public static boolean getConsoleMode(){
		return isConsoleMode;
	}
	
	public static void setWayPoint(String strWay){
		strWayPoint = strWay;
	}
	
	public static String getWayPoint(){
		return strWayPoint;
	}
	
	public static void setSrcDestPoints(String str1){
		strSrcDestPoints = str1;
	}
	
	public static String getSrcDestPoints(){
		return strSrcDestPoints;
	}
	
	public static Logger getLogger(String className){
		Logger logger= Logger.getLogger(className);
		
			 
	        logger.setUseParentHandlers(false);
			logger.addHandler(fileHandler);
			logger.setLevel(Constants.getLogLevel());
			
		return logger;
	}
	
}
