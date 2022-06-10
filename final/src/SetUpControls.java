import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
public class SetUpControls extends JFrame implements KeyListener{

	static FindingTimo fT = new FindingTimo();
	static JFrame frame = new JFrame("Controls Window (Keep this tab on top to use controls)");
	//making jframe a global so I can access it from another method and ultimately another class
	
	public static void setControls(){
		Menu m = new Menu();
		//set the frame
		
		frame.setBounds(300, 300, 600, 300);
		frame.setVisible(true);
		frame.getContentPane().setLayout(null);
		frame.isAlwaysOnTop();
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
				System.out.println("You lose!");	
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
				System.out.println("Hint!");
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
				m.endingScreen();
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
		
		int yTurtle = FindingTimo.timo.yCord;
		int xTurtle = FindingTimo.timo.xCord;
		int zTurtle = FindingTimo.timo.zCord;
		
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
			if(xPlayer<fT.mWidth-1) {
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
				fT.map[yPlayer][xPlayer][zPlayer] = '.';
				FindingTimo.player.zCord++;
				fT.update();
			} else {
				System.out.println("Already at the top floor");
			}
			break;
			
		//checks if user presses down
		case KeyEvent.VK_DOWN:
			if(zPlayer>0) {
				fT.map[yPlayer][xPlayer][zPlayer] = '.';
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


