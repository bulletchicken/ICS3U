/*=====================================
 * SetUpControls
 * Jeremy Su
 * June 17, 2022
 * Java 8
 * ====================================
 * - Problem Definition: Required keyboard and mouse input and interface to provide 
 * user with game controls
 * - Input: keypress "w", "a", "s", "d", UP and DOWN arrowkeys as well as Mouse clicks on buttons
 * - Output: Converted user input and warnings for out of bound movements. Also opens up a control
 * panel where buttons are stored and keypresses are read, as well as change colours indicating hot and cold
 * - Processing: converts user input into game movements and calls methods in the FindingTimo class
 * ====================================
 * List of Variables
 * let fT represent an object to access non-static methods within the FindingTimo class (type FindingTimo)
 * let frame represent an object created by the JFrame class that houses all the buttons (type JFrame)
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
@SuppressWarnings("serial")

public class SetUpControls extends JFrame implements KeyListener{

	static FindingTimo fT = new FindingTimo();
	static JFrame frame;
	//making jframe a global so I can access it from another method and ultimately another class
	
	
	/*setControls method:
	 * this procedural method is used to create and give properties to the frame and the buttons 
	 * 
	 * List of local variables
	 * let m represent an object to access non-static methods within the Menu class (type Menu)
	 * let alphabet represent all the letters in the alphabet used to convert x-coordinates to letter coordinates (type String[])
	 * let giveUp represent the give up button (type JButton)
	 * let huntButton represent the hint button (type JButton)
	 * let winButton represent the win button (type JButton)
	 * let rulesButton represent the rules button (type JButton)
	 * let controlsButton represent the controls button (type JButton)
	 * let reveal represent the reveal button (type JButton)
	 * let listen represent an object to access non-static methods within the SetUpControls class (type SetUpControls)
	 * @param none
	 * @return void
	 */
	public static void setControls(){
		frame = new JFrame("Controls Window (Keep this tab on top to use controls)");
		Menu m = new Menu();
		String[]alphabet = "abcdefghijklmnopqrstuvwxyz".split(""); //to display coordinates
		//set the frame
		
		frame.setBounds(1000, 500, 600, 300);
		frame.setVisible(true);
		frame.getContentPane().setLayout(null);
		frame.setAlwaysOnTop(true);
		frame.requestFocus();
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(Color.blue.darker().darker().darker());
		
		//give up button
		JButton giveUp = new JButton("Give up");
		frame.add(giveUp);
		giveUp.setBounds(350, 75, 100, 100);
		giveUp.setFocusable(false);
		giveUp.addActionListener(new ActionListener(){
			@Override
			/* actionPerformed method:
			 * this procedural method will call the loseScreen method in the Menu class
			 * @param arg0 (type ActionEvent)
			 * @return void
			 */
			public void actionPerformed(ActionEvent arg0) {
				m.loseScreen();
			}
		});
		
		//hint button
		JButton hintButton = new JButton("Hint");
		frame.add(hintButton);
		hintButton.setBounds(150, 75, 100, 100);
		hintButton.setFocusable(false);
		hintButton.addActionListener(new ActionListener(){
			@Override
			/* actionPerformed method:
			 * this procedural method will reveal timo's location but add 5 moves onto the player's score
			 * @param arg0 (type ActionEvent)
			 * @return void
			 */
			public void actionPerformed(ActionEvent arg0){
				if(!FindingTimo.timo.found){
					System.out.println("timo's current location (he's still on the run!) : " + alphabet[FindingTimo.timo.xCord] +" "+ (FindingTimo.timo.yCord+1) +" "+ (FindingTimo.timo.zCord+1)); //save this for the hint
					FindingTimo.player.numOfMoves +=5;
				}
				else{
					System.out.println("timo has already been found!");
				}
			}
		});
		
		//win button
		JButton winButton = new JButton("Win");
		frame.add(winButton);
		winButton.setBounds(250, 75, 100, 100);
		winButton.setFocusable(false);
		winButton.addActionListener(new ActionListener() {
			@Override
			/* actionPerformed method:
			 * this procedural method will call the endingScreen method in the Menu class
			 * @param arg0 (type ActionEvent)
			 * @return void
			 */
			public void actionPerformed(ActionEvent arg0) {
				m.endingScreen(FindingTimo.player.numOfMoves);
			}
		});
		
		//rules button
		JButton rulesButton = new JButton("rules");
		frame.add(rulesButton);
		rulesButton.setBounds(150, 200, 100, 50);
		rulesButton.setFocusable(false);
		rulesButton.addActionListener(new ActionListener() {
			@Override
			/* actionPerformed method:
			 * this procedural method will call the rules method in the Menu class
			 * @param arg0 (type ActionEvent)
			 * @return void
			 */
			public void actionPerformed(ActionEvent arg0) {
				m.rules();
			}
		});
		
		//controls button
		JButton controlsButton = new JButton("controls");
		frame.add(controlsButton);
		controlsButton.setBounds(250, 200, 100, 50);
		controlsButton.setFocusable(false);
		controlsButton.addActionListener(new ActionListener() {
			@Override
			/* actionPerformed method:
			 * this procedural method will call the controlHelp method in the Menu class
			 * @param arg0 (type ActionEvent)
			 * @return void
			 */
			public void actionPerformed(ActionEvent arg0) {
				m.controlHelp();
			}
		});
		
		//reveal button
		JButton reveal = new JButton("reveal map");
		frame.add(reveal);
		reveal.setBounds(350, 200, 100, 50);
		reveal.setFocusable(false);
		reveal.addActionListener(new ActionListener() {
			@Override
			/* actionPerformed method:
			 * this procedural method will call the revealMap method in the Menu class
			 * @param arg0 (type ActionEvent)
			 * @return void
			 */
			public void actionPerformed(ActionEvent arg0) {
				fT.revealMap();
			}
		});
		
		//attaching keylistener to the frame
		SetUpControls listen = new SetUpControls();
		frame.addKeyListener(listen);
		//will remain passively active 
		
	}//end setControls
	
	/* keyPressed method:
	 * this procedural method will run anytime the frame captures keyboard input, converting the input
	 * into game values and movements
	 * 
	 * List of Local Variables
	 * let yPlayer represent the y coordinates of player (type int)
	 * let xPlayer represent the x coordinates of player (type int)
	 * let zPlayer represent the z coordinates of player (type int)
	 * let yHunter represent the y coordinates of hunter (type int)
	 * let xHunter represent the x coordinates of hunter (type int)
	 * let zHunter represent the z coordinates of hunter (type int)
	 * let move represent the letter from keyboard (type char)
	 * 
	 * @param
	 * e - what ever key that was pressed and received by the frame (type KeyEvent)
	 * @return void
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		
		int yPlayer = FindingTimo.player.yCord;
		int xPlayer = FindingTimo.player.xCord;
		int zPlayer = FindingTimo.player.zCord;
		
		int yHunter = FindingTimo.hunter.yCord;
		int xHunter = FindingTimo.hunter.xCord;
		int zHunter = FindingTimo.hunter.zCord;
		
		char move = e.getKeyChar();
		
		switch(move) {
		
		//create two methods for the two cases | hotter, colder | and fit in two conditions
		
		//if user presses w
		case 'w':
			if(yPlayer>0) {
				FindingTimo.player.yCord--;
				fT.update();
			} else {
				System.out.println("Wall!");
			}
			break;
			
		//if user presses s
		case 's':
			if(yPlayer<FindingTimo.mLength-1) {
				FindingTimo.player.yCord++;
				fT.update();
			} else {
				System.out.println("Wall!");
			}
			break;
			
		//checks if user presses a
		case 'a':
			if(xPlayer>0) {
				FindingTimo.player.xCord--;
				fT.update();
			} else {
				System.out.println("Wall!");
			}
			break;
			
		//checks if user presses d
		case 'd':
			if(xPlayer<FindingTimo.mWidth-1) {
				FindingTimo.player.xCord++;
				fT.update();
			} else {
				System.out.println("Wall!");
			}
			break;
		}
		
		switch(e.getKeyCode()) {
		
		//checks if user presses up
		case KeyEvent.VK_UP:
			if(zPlayer<2) {
				FindingTimo.map[yPlayer][xPlayer][zPlayer] = '.';
				FindingTimo.player.zCord++;
				
				FindingTimo.map[yHunter][xHunter][zHunter] = ' ';
				FindingTimo.hunter.zCord++;
				fT.update();
			} else {
				System.out.println("Already at the top floor");
			}
			break;
			
		//checks if user presses down
		case KeyEvent.VK_DOWN:
			if(zPlayer>0) {
				FindingTimo.map[yPlayer][xPlayer][zPlayer] = '.';
				FindingTimo.player.zCord--;
				
				FindingTimo.map[yHunter][xHunter][zHunter] = ' ';
				FindingTimo.hunter.zCord--;
				fT.update();
			} else {
				System.out.println("Already at the bottom floor");
			} 
			break;
		}
		
	}//end keyPressed method
	
	/*closeWindow method:
	 * this procedural closes the controls frame
	 * @param none
	 * @return void
	 */
	public static void closeWindow() {
		frame.dispose();
	}
	
	
	
	@Override
	public void keyReleased(KeyEvent e) {
		//empty. Must be implemented because of keylistener
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		//empty. Must be implemented because of keylistener
	}

	
}