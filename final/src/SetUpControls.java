import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
public class SetUpControls extends JFrame implements KeyListener{

	
	public static void setControls(){
		
		//set the frame
		JFrame frame = new JFrame("Controls Window (Keep this tab on top to use controls)");
		frame.setBounds(300, 300, 600, 300);
		frame.setVisible(true);
		frame.getContentPane().setLayout(null);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
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
				System.out.println("Win!");
			}
		});
		
		//attaching keylistener to the frame
		SetUpControls listen = new SetUpControls();
		frame.addKeyListener(listen);
		//will remain passively active 
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		char move = e.getKeyChar();
		switch(move) {
		case 'w':
			System.out.println("Up");
			break;
		case 's':
			System.out.println("Down");
			break;
		case 'a':
			System.out.println("Left");
			break;
		case 'd':
			System.out.println("Right");
			break;
		}
		
		switch(e.getKeyCode()) {
		case KeyEvent.VK_UP:
			System.out.println("Jump");
			break;
		case KeyEvent.VK_DOWN:
			System.out.println("Fall");
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
	
}


