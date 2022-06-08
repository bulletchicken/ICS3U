import java.io.*;

class Character {
	int xCord;
	int yCord;
	int zCord;
	boolean found;
}

class Moves{
	byte[]options;
	byte randomDirection;
	byte numOfOptions;
}
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
	int move = 0; //make a universal move counter to display number of moves and use to do turtle speed if(move%4==0) or smth

	static char [][][] map = new char[mLength][mWidth][height];


	public static void main(String[]args) {
		new FindingTimo();
		
		FindingTimo fT = new FindingTimo();

		fT.run();

	}

	public Moves moveGenerator(Character movement, char poop) {
		
		final byte maxNumOfOptions = 5;
		Moves availableMoves = new Moves();
		availableMoves.options = new byte[maxNumOfOptions];
		

		
		//checks which sides are open. adds them into a draw. //if nothing in draw, goes up and down
		
		//adds the directions into a raffel!
		
		/* 1 = forward
		 * 2 = back
		 * 3 = left
		 * 4 = right
		 */
		byte indexOfOption = 0;
		if(movement.yCord>0&&map[timo.yCord-1][timo.xCord][timo.zCord]!=poop) {
			System.out.println("can move forward");
			availableMoves.options[indexOfOption] = 1;
			indexOfOption++;
		}
		if(movement.yCord<mLength-1&&map[timo.yCord+1][timo.xCord][timo.zCord]!=poop) {
			System.out.println("can move backwards");
			availableMoves.options[indexOfOption] = 2;
			indexOfOption++;
		}
		if(movement.xCord>0&&map[timo.yCord][timo.xCord-1][timo.zCord]!=poop) {
			System.out.println("can move left");
			availableMoves.options[indexOfOption] = 3;
			indexOfOption++;
		}
		if(movement.xCord<mWidth-1&&map[timo.yCord][timo.xCord+1][timo.zCord]!=poop) {
			System.out.println("can move right");
			availableMoves.options[indexOfOption] = 4;
			indexOfOption++;
		}
		
		for(int i = 0; i < 5; i++) {
			System.out.println(availableMoves.options[i]);
		}
		
		availableMoves.numOfOptions = indexOfOption;
		
		availableMoves.randomDirection = (byte)(Math.random()*indexOfOption); //200 iq numOfOptions strat
		System.out.println("dfsdf" + (byte)(Math.random()*indexOfOption));
		return availableMoves;
		
	}
	public void turtleMove(){
		
		byte direction;
		final char poop = 'o';
		Moves turtleDirection = moveGenerator(timo, poop);
		
		if(turtleDirection.numOfOptions == 0) { //meaning all directions were skipped and none added to options list
			direction = 0;
		}
		else {
			direction = turtleDirection.options[turtleDirection.randomDirection];
		}
		System.out.println(direction);
		switch(direction) {
		
		case 0: //up down in case the turtle is trapped in its own poop
			if(timo.zCord<height-1) { //zcords should be measured and compared invididually
				map[timo.yCord][timo.xCord][timo.zCord] = poop;
				timo.zCord++;
			} 
			else if(timo.zCord>0) {
				map[timo.yCord][timo.xCord][timo.zCord] = poop;
				timo.zCord--;
			}
			break;
		case 1: //forward
			map[timo.yCord][timo.xCord][timo.zCord] = poop;
			timo.yCord--;
			break;
		case 2: //backward
			map[timo.yCord][timo.xCord][timo.zCord] = poop;
			timo.yCord++;
			break;
		case 3: //left
			map[timo.yCord][timo.xCord][timo.zCord] = poop;
			timo.xCord--;
			break;
		case 4: //right
			map[timo.yCord][timo.xCord][timo.zCord] = poop;
			timo.xCord++;
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
		vision();
		turtleMove();
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


