package lab.game.utility;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import lab.game.pojo.HostInfo;

public class Util {
	private static final Logger logger = Constants.getLogger(Util.class.getName());
	
	/**
	 * This method extract row and column position. row and column position are separated by space whereas different
	 * values are separated by comma separated list e.g 1 2,5 4 are two entries. First is the position of block
	 * at row position 1 and column position 2, second is row position 5 and column position 4
	 * @param str
	 * @param totalRowsInRegion : for checking the index does not exceed the possible rows in region
	 * @param totalColsInRegion : for checking the index does not exceed the possible cols in region
	 * @return
	 */
	
	public int[][] extractRegionCoordinates(String str,int totalRowsInRegion,int totalColsInRegion){		
		
		String strEntries[] = str.trim().split(",");				
				
		int validEntryCount = 0;
		
		for(int i=0;i<strEntries.length;i++){
			String str1 = strEntries[i];
			
			
			String str2[] = str1.trim().split(" ");
			
			int rowVal = Integer.parseInt(str2[0]);
			int colVal = Integer.parseInt(str2[1]);
			
			if(rowVal>=totalRowsInRegion || colVal>=totalColsInRegion)	//index exceeded the region boundary
				continue;
			
			validEntryCount++;			
		}
		
		
		int arrRegion[][] = new int[validEntryCount][2];	//two dimensional array containing row and col values
		
		int index = 0;
			
		for(int i=0;i<strEntries.length;i++){
			String str1 = strEntries[i];
			
			
			String str2[] = str1.trim().split(" ");
			
			int rowVal = Integer.parseInt(str2[0]);
			int colVal = Integer.parseInt(str2[1]);
					
			
			if(rowVal>=totalRowsInRegion || colVal>=totalColsInRegion)	//index exceeded the region boundary
				continue;
			
			
			arrRegion[index][0] = rowVal;
			arrRegion[index][1] = colVal;
			
			index++;			
			
		}	
		
		return arrRegion;
	}
	
	
	public static void println(String msg){
		logger.info(msg );
	}

	public static boolean isNull(Object obj){
		if (null == obj) {
			return true;
		}
		return false;
	}
	
	public static boolean isNullOrEmpty(Object obj){
		String o = (String)obj;
		if (null == obj) {
			return true;
		}else if ("".equals(o.toString())) {
			return true;
		}
		return false;
	}
	
	public static BufferedImage loadImage(String path){
		
		try {
			return ImageIO.read(Util.class.getResource(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	


	
	public static int generatePlayerId(int length) {
	    Random random = new Random();
	    char[] digits = new char[length];
	    digits[0] = (char) (random.nextInt(9) + '1');
	    for (int i = 1; i < length; i++) {
	        digits[i] = (char) (random.nextInt(10) + '0');
	    }
	    
	   
	    return Integer.parseInt(new String(digits));
	}
	
	public static void traverseSuperNeighbors(Map<Integer,HostInfo> superNeighbors){
		
		logger.info("Size of  superNeighbors :  "+superNeighbors.size());
		Iterator<Entry<Integer, HostInfo>> it = superNeighbors.entrySet().iterator();
	    
	     while (it.hasNext()) {
	         Entry<Integer, HostInfo> pairs = it.next();
	         
	         HostInfo info =(HostInfo) pairs.getValue();
	         logger.info("Region ID : "+pairs.getKey() +" -- "+"IP ADDRESS : "+info.getSuperIp() +":"+info.getPort());
	        
	         
	     }
	     logger.info("---------------------------------------------------");
	}	
	
	
}
