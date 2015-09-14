package lab.game.startup;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;

import lab.game.gui.GUIMode;
import lab.game.gui.Game;
import lab.game.gui.KeyManager;
import lab.game.gui.ToolBar;
import lab.game.utility.Constants;
import lab.game.utility.InitGame;

/**
 * Main class  for starting the game. The game is started in GUI mode or console mode depending upon the settings
 * in configuration file. If consoleMode parameter in configuration file is false then game is started in console mode
 * else if consoleMode is false then game is started in GUI mode
 * 
 * @author Kamran Yaqub
 *
 */

public class StartGame extends JFrame   {

	private static final long serialVersionUID = 1L;
	private Game game;
	
	public StartGame(String title) throws HeadlessException {
		super(title);
		FileHandler fileHandler;
		try {
			fileHandler = new FileHandler("./logs/BomberMines-"+Constants.getPort()+"-%u.txt");
			SimpleFormatter formatter = new SimpleFormatter();  
	        fileHandler.setFormatter(formatter);
	        Constants.setFileHandler(fileHandler);
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		 
	}
	
	
	/**
	 * Initializes and starts the game in GUI mode
	 */
	
	public void initilaizeAndStart(){
		//new InitGame();	
		
		game = new GUIMode();
		
			
		game.setPreferredSize(new Dimension(Constants.getScreenWidth() * Constants.getScale(), Constants.getScreenHeight() * Constants.getScale()));
		
		setSize(Constants.getScreenWidth() * Constants.getScale(), Constants.getScreenHeight() * Constants.getScale());
			//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			
		setResizable(false);
		getContentPane().add(game);
			
			//frame.add(game);
		
		addWindowListener(new WindowAdapter() {
				
				@Override
				public void windowClosing(WindowEvent e) {
				
				
				 System.exit(0);
				}
				
				  });
			
			ToolBar bar = new ToolBar();
			bar.setGame(game);
			bar.setPane(new JTextPane());
		    bar.getPane().setPreferredSize(new Dimension(250, 250));
		    bar.getPane().setBorder(new BevelBorder(BevelBorder.LOWERED));
		    bar.getToolBar().setMaximumSize(bar.getToolBar().getSize());
		   		   
		    setJMenuBar(bar.getMenuBar());
		    getContentPane().add(bar.getToolBar(), BorderLayout.NORTH);
		    //frame.getContentPane().add(example.pane, BorderLayout.CENTER);
		    pack();
		    setVisible(true);
		    ((GUIMode)game).setToolBar(bar);
		   
		    ((GUIMode)game).initGUI();
		    KeyManager keyManager = new KeyManager();
			keyManager.setGame(game);
			game.addKeyListener(keyManager);
		    ((GUIMode)game).start();
	}

	/*
	 * Main class that is to be run for game execution
	 */

	public static void main(String[] args) {		
		
		//Initializes the game parameters from configuration file
		new InitGame();
		
		if(Constants.getConsoleMode()==false){	//i.e GUI mode			
			StartGame gameFrame=new StartGame("Bomberman [Good Team]");	
			gameFrame.initilaizeAndStart();
		}
		else {	//console mode
			BomberMineConsole bc = new BomberMineConsole();
			bc.runBombermineConsole();
		}
	}
	
	
	 
	     
	    
}
