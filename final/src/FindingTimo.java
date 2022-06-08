import java.io.*;

public class FindingTimo {
	
	static class Character {
		int xCord;
		int yCord;
		int zCord;
		boolean found;
	}

	static class Moves{
		byte[]options;
		byte randomDirection;
		byte numOfOptions;
	}

	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	static Character timo = new Character();
	static Character player = new Character();
	static Character finish = new Character();

	final char visionCones = '.';

	final static int mLength = 15; //vertical
	final static int mWidth = 15; //horizontal
	final static int height = 3; //up down
	
	int move = 0; //make a universal move counter to display number of moves and use to do turtle speed if(move%4==0) or smth

	static char [][][] map = new char[mLength][mWidth][height];
	static byte [][][] unseenMap = new byte[mLength][mWidth][height];
 
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
		if(movement.yCord>0&&unseenMap[timo.yCord-1][timo.xCord][timo.zCord]==0) {
			System.out.println("can move forward");
			availableMoves.options[indexOfOption] = 1;
			indexOfOption++;
		}
		if(movement.yCord<mLength-1&&unseenMap[timo.yCord+1][timo.xCord][timo.zCord]==0) {
			System.out.println("can move backwards");
			availableMoves.options[indexOfOption] = 2;
			indexOfOption++;
		}
		if(movement.xCord>0&&unseenMap[timo.yCord][timo.xCord-1][timo.zCord]==0) {
			System.out.println("can move left");
			availableMoves.options[indexOfOption] = 3;
			indexOfOption++;
		}
		if(movement.xCord<mWidth-1&&unseenMap[timo.yCord][timo.xCord+1][timo.zCord]==0) {
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
				unseenMap[timo.yCord][timo.xCord][timo.zCord] = (byte) move;
				timo.zCord++;
			} 
			else if(timo.zCord>0) {
				unseenMap[timo.yCord][timo.xCord][timo.zCord] = (byte) move;
				timo.zCord--;
			}
			break;
		case 1: //forward
			unseenMap[timo.yCord][timo.xCord][timo.zCord] = (byte) move;
			timo.yCord--;
			break;
		case 2: //backward
			unseenMap[timo.yCord][timo.xCord][timo.zCord] = (byte) move;
			timo.yCord++;
			break;
		case 3: //left
			unseenMap[timo.yCord][timo.xCord][timo.zCord] = (byte) move;
			timo.xCord--;
			break;
		case 4: //right
			unseenMap[timo.yCord][timo.xCord][timo.zCord] = (byte) move;
			timo.xCord++;
			break;
		}
		
	}
	
	public void poopPlacement(int playerYCord, int playerXCord, int playerZCord) {
		char poop = 'o';
		//if main map has vision cone on same place where other map has poop
		
		
		//statement to check if vision has discovered poop or not
		if(map[playerYCord][playerXCord][playerZCord]== visionCones && unseenMap[timo.yCord][timo.xCord][timo.zCord]!=0) {
			map[timo.yCord][timo.xCord][timo.zCord]=poop;
		} else {
			map[playerYCord][playerXCord][playerZCord]=visionCones;
		}
	}
	//have to compare unseenMap everytime vision is called.
	public boolean canBeSeen(Character inQuestion) {
		if(map[inQuestion.yCord][inQuestion.xCord][inQuestion.zCord]==visionCones) {
			return true;
		}
		return false;
	}

	public void vision() {
		try{
			poopPlacement(player.yCord+1, player.xCord, player.zCord);
			//corner piece
			poopPlacement(player.yCord+1, player.xCord+1, player.zCord);
			poopPlacement(player.yCord+1, player.xCord-1, player.zCord);
		}catch(Exception e){
			
		}
		try{
			poopPlacement(player.yCord-1, player.xCord, player.zCord);
			
			//corner piece
			poopPlacement(player.yCord-1, player.xCord+1, player.zCord);
			poopPlacement(player.yCord-1, player.xCord-1, player.zCord);
		}catch(Exception e){
			
		}

		try{
			poopPlacement(player.yCord, player.xCord+1, player.zCord);
		}catch(Exception e){

		}

		try{
			poopPlacement(player.yCord, player.xCord-1, player.zCord);
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
		move++;
		if(!timo.found){
			if(move%3==0){
				turtleMove();
			}
			if(canBeSeen(timo)){
				map[timo.yCord][timo.xCord][timo.zCord] = '!';
			}
		}
		System.out.println(alphabet[timo.yCord] +" "+ timo.xCord +" "+ (timo.zCord+1));
		System.out.println(move);
		map[player.yCord][player.xCord][player.zCord] = 'x';
		if(player.found||onTurtle()) {
			System.out.println("Return the turtle to coordinates (" + alphabet[finish.xCord] + ", " + (finish.yCord+1) + ", " + (finish.zCord+1) + ")");
			if(bedroom()){
				endingScreen();
				return; //breaks the method
			}
		}

		System.out.print(timo.found);
		
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
			timo.found = true;
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


