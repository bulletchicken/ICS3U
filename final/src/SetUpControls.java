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
			public void actionPerformed(ActionEvent arg0) {
				m.endingScreen(FindingTimo.player.numOfMoves);
			}
		});
		
		//rules button
		JButton rulesButton = new JButton("rules");
		frame.add(rulesButton);
		rulesButton.setBounds(200, 200, 100, 50);
		rulesButton.setFocusable(false);
		rulesButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Menu menu = new Menu();
				menu.rules();
			}
		});
		
		//controls button
		JButton controlsButton = new JButton("controls");
		frame.add(controlsButton);
		controlsButton.setBounds(300, 200, 100, 50);
		controlsButton.setFocusable(false);
		controlsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Menu menu = new Menu();
				menu.controlHelp();
			}
		});
		
		//attaching keylistener to the frame
		SetUpControls listen = new SetUpControls();
		frame.addKeyListener(listen);
		//will remain passively active 
		
	}
	
	
	@Override
	public void keyPressed(KeyEvent e) {
		
		int yPlayer = FindingTimo.player.yCord;
		int xPlayer = FindingTimo.player.xCord;
		int zPlayer = FindingTimo.player.zCord;
		
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
				fT.update();
			} else {
				System.out.println("Already at the bottom floor");
			} 
			break;
		}
		
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		//empty. Must be implemeneted because of keylistener
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}
	
	public static void closeWindow() {
		frame.dispose();
	}
	
}