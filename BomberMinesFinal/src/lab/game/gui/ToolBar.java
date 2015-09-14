package lab.game.gui;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import lab.game.network.SuperPeerHandler;
import lab.game.utility.Constants;
import lab.game.utility.Util;

/**
 * Toolbar contains GUI objects like textbox to enter super peer address. The current player connects to the address
 * written in text box. Leave button for graceful leave of player from the game. Also current address and region number
 * of the player are also mentioned
 * 
 */

public class ToolBar extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private  JTextPane pane;
	  private JMenuBar menuBar;
	  private JToolBar toolBar;
	  private JTextField txtAddress;
	  private Game game;
	 
	  JLabel labelRegion;
	  JButton buttonConnect = new JButton("Connect");
	  JButton btnLeave = new JButton("Gracefull Leave");
	  JMenu isSuper ;
	      
	  public ToolBar()  {
	    menuBar = new JMenuBar();


	    JMenu formatMenu = new JMenu("My IP :"+Constants.getMyIpAddress());
	    
	    isSuper = new JMenu("Super Peer : NO");
	    menuBar.add(formatMenu);
	   
	    
	    toolBar = new JToolBar("Formatting");
	  
	    menuBar.add(isSuper);
	    toolBar.addSeparator();
	    JLabel label = new JLabel("IP Address:");
	    toolBar.add(label);
	    toolBar.addSeparator();
	    txtAddress = new JTextField();
	    toolBar.add(txtAddress);	    
	    
	     buttonConnect = new JButton("Connect");
	     btnLeave = new JButton(" Leave ");
	     btnLeave.setEnabled(false);
	    toolBar.addSeparator();
	    toolBar.add(buttonConnect);
	    toolBar.add(btnLeave);
	    toolBar.addSeparator();
	    labelRegion = new JLabel("Region No");
	    toolBar.add(labelRegion);
	    toolBar.setFloatable(false);
	    buttonConnect.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        try {
	         
	         if (!Util.isNullOrEmpty(txtAddress.getText())) {
		        	String[] address =txtAddress.getText().split(":");
		        	game.getPlayer().getController().connectToNetwork(address[0], Integer.parseInt(address[1]));
		        	buttonConnect.setEnabled(false);
		        	btnLeave.setEnabled(true);
				}
	        } catch (Exception ex) {
	        	JOptionPane.showMessageDialog(null,
	        		    "Invalid IP addess given","Invalid IP",
	        		    JOptionPane.ERROR_MESSAGE);
	        		
	        }
	      }
	    });
	    
	    
	    btnLeave.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		        try {
		         
		        	if (game.getPlayer().isConnected() || SuperPeerHandler.getConnectedPeers() > 0) {
						game.getPlayer().getController().getNetwork().disconnect(false,-1,-1);
						//logger.info("Successfully disconnected ...");
						btnLeave.setEnabled(false);
						buttonConnect.setEnabled(true);
						
					}
		        } catch (Exception ex) {
		        	
		        		//logger.info(ex.getMessage());
		        }
		      }
		    });
	    
	        
	  }
	  
	  public void setRegionLabel(int regionNo){
	    	labelRegion.setText("Region: " + regionNo);
	    }

	  

	  class ActionHandler extends AbstractAction {

	    public ActionHandler(String text, Icon icon, String description,
	        char accelerator) {
	      super(text, icon);
	      putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(accelerator,
	          Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	      putValue(SHORT_DESCRIPTION, description);
	    }

	    public void actionPerformed(ActionEvent e) {
	      try {
	        pane.getStyledDocument().insertString(0,
	            "Action [" + getValue(NAME) + "] performed!\n", null);	        
	      } catch (Exception ex) {
	        ex.printStackTrace();
	      }
	    }
	  }

	  /**
		 * @return the pane
		 */
		public JTextPane getPane() {
			return pane;
		}

		/**
		 * @param pane the pane to set
		 */
		public void setPane(JTextPane pane) {
			this.pane = pane;
		}

		/**
		 * @return the menuBar
		 */
		public JMenuBar getMenuBar() {
			return menuBar;
		}

		/**
		 * @param menuBar the menuBar to set
		 */
		public void setMenuBar(JMenuBar menuBar) {
			this.menuBar = menuBar;
		}

		/**
		 * @return the toolBar
		 */
		public JToolBar getToolBar() {
			return toolBar;
		}

		/**
		 * @param toolBar the toolBar to set
		 */
		public void setToolBar(JToolBar toolBar) {
			this.toolBar = toolBar;
		}

		/**
		 * @return the isSuper
		 */
		public JMenu getIsSuper() {
			return isSuper;
		}

		/**
		 * @param isSuper the isSuper to set
		 */
		public void setIsSuper(JMenu isSuper) {
			this.isSuper = isSuper;
		}
		
		public void setGame(Game game){
			this.game = game;
		}
}
