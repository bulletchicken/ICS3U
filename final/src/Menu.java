import java.io.*;
public class Menu {
	public void endingScreen() {
		SetUpControls.closeWindow();
		System.out.println("You win");
	}

	public void displayIntro() {
		System.out.println("Game : type 1 to begin");
	}
}
