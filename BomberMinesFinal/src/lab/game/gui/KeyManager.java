package lab.game.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * KeyManager class catering for key pressed events (arrow keys and space bar) 
 */

public class KeyManager implements KeyListener   {
	private Game game;
	
	public void setGame(Game game){
				
		this.game = game;
	}
	
	
	public void keyPressed(KeyEvent e) {
			
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:			
			game.getPlayer().setUp(true);			
			break;
		case KeyEvent.VK_DOWN:
			game.getPlayer().setDown(true); 
			break;
		case KeyEvent.VK_LEFT:
			game.getPlayer().setLeft(true); 
			break;
		case KeyEvent.VK_RIGHT:
			game.getPlayer().setRight(true); 
			break;
		case KeyEvent.VK_SPACE:
			game.getPlayer().setSpace(true); 
			break;
		default:
			break;
		}
		
	}

	
	public void keyReleased(KeyEvent e) {
		
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			game.getPlayer().setUp(false); 
			break;
		case KeyEvent.VK_DOWN:
			game.getPlayer().setDown(false); 
			break;
		case KeyEvent.VK_LEFT:
			game.getPlayer().setLeft(false); 
			break;
		case KeyEvent.VK_RIGHT:
			game.getPlayer().setRight(false); 
			break;
		case KeyEvent.VK_SPACE:
			game.getPlayer().setSpace(false); 
			break;
		default:
			break;
		}
		
	}

	
	public void keyTyped(KeyEvent arg0) {
		
		
	}
}
