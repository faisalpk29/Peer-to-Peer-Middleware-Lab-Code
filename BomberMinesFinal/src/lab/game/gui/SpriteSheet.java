package lab.game.gui;

import java.awt.image.BufferedImage;

import lab.game.utility.Constants;

/**
 * Sprite sheet of all images used in game
 * 
 */
public class SpriteSheet {

	private BufferedImage sheet;

	
	public SpriteSheet(BufferedImage sheet){
		this.sheet = sheet;
	}
	
	
	
	public BufferedImage crop(int col, int row, int w, int h){
		return sheet.getSubimage(col *Constants.getImagesize(), row *Constants.getImagesize(), w, h);
	}
	
	
	
}
