package lab.game.gui;

import java.awt.Graphics;

import lab.game.utility.Constants;

public class BrickTile extends Tile{

	public BrickTile(ImageManager im){
		
		super(im);
		
	}

	
	public void tick() {
				
	}

	
	public void render(Graphics g,int x,int y) {
		g.drawImage(im.getBrickTile(), x, y, Constants.getImagesize()* Constants.getScale(), Constants.getImagesize()* Constants.getScale(),null);
		
	}
}
