import java.io.*;

public class FindingTimo {
	//testgit
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	static Character timo = new Character();
	static Character player = new Character();
	static Character finish = new Character();

	final char visionCones = '.';

	final static int mLength = 15; //vertical
	final static int mWidth = 15; //horizontal
	final static int height = 3; //up down
	
	int turtleSpeed = 0;

	static char [][][] map = new char[mLength][mWidth][height];


	public static void main(String[]args) {
		new FindingTimo();
		
		FindingTimo fT = new FindingTimo();

		fT.run();

	}
	

	public void turtleMove(){
		final char poop = 'o';
		byte direction = (byte)(1+Math.random()*4);
		switch(direction) {
		case 1:
			if(timo.yCord>0&&map[timo.yCord-1][timo.xCord][timo.zCord]!=poop) {
				map[timo.yCord][timo.xCord][timo.zCord] = poop;
				timo.yCord--;
				
			} else {
				System.out.println("blocked");
				turtleMove();
			}
			break;
		case 2:
			if(timo.yCord<mLength-1&&map[timo.yCord+1][timo.xCord][timo.zCord]!=poop) {
				map[timo.yCord][timo.xCord][timo.zCord] = poop;
				timo.yCord++;
				
			} else {
				System.out.println("blocked");
				turtleMove();
			}
			break;
		case 3:
			if(timo.xCord>0&&map[timo.yCord][timo.xCord-1][timo.zCord]!=poop) {
				map[timo.yCord][timo.xCord][timo.zCord] = poop;
				timo.xCord--;
				
			}else {
				System.out.println("blocked");
				turtleMove();
			}
			break;
		case 4:
			if(timo.xCord<mWidth-1&&map[timo.yCord][timo.xCord+1][timo.zCord]!=poop) {
				map[timo.yCord][timo.xCord][timo.zCord] = poop;
				timo.xCord++;
				
			}else {
				System.out.println("blocked");
				turtleMove();
			}
			break;
			
		}
		
		
	}
	public boolean canBeSeen(Character inQuestion) {
		if(map[inQuestion.yCord][inQuestion.xCord][inQuestion.zCord]==visionCones) {
			return true;
		}
		return false;
	}

	public void vision() {
		try{
			map[player.yCord+1][player.xCord][player.zCord] = visionCones;
			
			//corner piece
			map[player.yCord+1][player.xCord+1][player.zCord] = visionCones;
			map[player.yCord+1][player.xCord-1][player.zCord] = visionCones;
		}catch(Exception e){

		}
		try{
			map[player.yCord-1][player.xCord][player.zCord] = visionCones;
			
			//corner piece
			map[player.yCord-1][player.xCord+1][player.zCord] = visionCones;
			map[player.yCord-1][player.xCord-1][player.zCord] = visionCones;
		}catch(Exception e){

		}

		try{
			map[player.yCord][player.xCord+1][player.zCord] = visionCones;
		}catch(Exception e){

		}

		try{
			map[player.yCord][player.xCord-1][player.zCord] = visionCones;
		}catch(Exception e){

		}
	}

	public void displayGrid(int floor, String[]alphabet) {

		System.out.println("(" + alphabet[player.xCord] + ", "+(player.yCord+1) + ", " + (player.zCord+1) + ")");

		for(int i = 0; i < map[0].length; i++) {
			System.out.print(alphabet[i] + " ");
		}
		System.out.println();

		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[0].length; j++) {
				System.out.print(map[i][j][floor] + " ");
			}
			System.out.println(i+1);                                                                                                                                                                                                                                                                          
		}
	}

	public void update() {
		String[]alphabet = "abcdefghijklmnopqrstuvwxyz".split("");
		for(int i = 0; i < 20; i++) System.out.println(); //to clear screen
		turtleMove();
		vision();
		map[timo.yCord][timo.xCord][timo.zCord] = '!';
		map[player.yCord][player.xCord][player.zCord] = 'x';
		if(player.found||onTurtle()) {
			System.out.println("Return the turtle to coordinates (" + alphabet[finish.xCord] + ", " + (finish.yCord+1) + ", " + (finish.zCord+1) + ")");
			if(bedroom()){
				endingScreen();
				return; //breaks the method
			}
		}

		System.out.print(player.found);
		
		displayGrid(player.zCord, alphabet);
	}

	public boolean bedroom() {

		//make a random point the player has to reach on another floor
		if(player.yCord == finish.yCord && player.xCord == finish.xCord && player.zCord == finish.zCord){
			return true;
		}
		return false;
	}

	public boolean onTurtle() {
		if(timo.xCord==player.xCord&&timo.yCord==player.yCord&&timo.zCord==player.zCord) {
			player.found = true;
			System.out.println("Caputred the turtle!");
			
			finish.xCord = (int)(Math.random()*15);
			finish.yCord = (int)(Math.random()*15);
			finish.zCord = (int)(Math.random()*height);
			
			
			return true;
		}
		return false;
	}

	public void run() {
		SetUpControls.setControls();
		//starting cords
		timo.xCord = (int)(Math.random()*15);
		timo.yCord = (int)(Math.random()*15);
		timo.zCord = (int)(Math.random()*height);
		//starting cords in the middle
		player.xCord = 7;
		player.yCord = 7;
		player.zCord = 0;

		//setup the map and resets it on every run
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[0].length; j++) {
				for(int k = 0; k < map[0][0].length; k++) {
					map[i][j][k] = ' ';
				}
			}
		}

		//use cues like you hear footsteps above or below you
		update();
	}
	public void endingScreen() {
		SetUpControls.closeWindow();
		System.out.println("You win");
	}

	public void displayIntro() {
		System.out.println("Game : type 1 to begin");
	}

	public String nameInput()throws IOException {
		return br.readLine();
	}
}