import java.io.*;
public class Menu {
	
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	public static void main(String[]args) {
		FindingTimo fT = new FindingTimo();
		Menu m = new Menu();
		m.displayIntro();
		fT.run();
	}
	
	public void endingScreen() {
		SetUpControls.closeWindow();
		System.out.println("You win");
	}

	public void displayIntro() {
		System.out.println("Game : type 1 to begin");
	}
	public int intInput() {
		try { 
			
			int input = Integer.parseInt(br.readLine());
			if(input>10&&input<20) {
				return input;
			} else {
				
				return intInput();
			}
		}catch(Exception e) {
			
			return intInput();
		}
	}
}

