import java.awt.Color;
import java.io.*;

public class FindingTimo {
	
	static class Character {
		int xCord;
		int yCord;
		int zCord;
		int numOfMoves;
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
	Character finish = new Character();

	final char visionCones = '.';

	final static int mLength = 15; //vertical
	final static int mWidth = 15; //horizontal
	final static int height = 3; //up down
	
	int move = 0; //make a universal move counter to display number of moves and use to do turtle speed if(move%4==0) or smth

	static char [][][] map = new char[mLength][mWidth][height];
	static byte [][][] ghostMap = new byte[mLength][mWidth][height];
 
	public static void main(String[]args) {
		new FindingTimo();
		
		FindingTimo fT = new FindingTimo();

		fT.run();

	}

	public Moves moveGenerator(Character movement, char poop) {
		
		final byte maxNumOfOptions = 4;
		Moves availableMoves = new Moves();
		availableMoves.options = new byte[maxNumOfOptions];
		
		//checks which sides are open. adds them into a draw. //if nothing in draw, goes up and down
		
		//adds the directions into a raffel!
		
		byte indexOfOption = 0;
		if(movement.yCord>0&&ghostMap[timo.yCord-1][timo.xCord][timo.zCord]==0) {
			availableMoves.options[indexOfOption] = 1;
			indexOfOption++;
		}
		if(movement.yCord<mLength-1&&ghostMap[timo.yCord+1][timo.xCord][timo.zCord]==0) {
			availableMoves.options[indexOfOption] = 2;
			indexOfOption++;
		}
		if(movement.xCord>0&&ghostMap[timo.yCord][timo.xCord-1][timo.zCord]==0) {
			availableMoves.options[indexOfOption] = 3;
			indexOfOption++;
		}
		if(movement.xCord<mWidth-1&&ghostMap[timo.yCord][timo.xCord+1][timo.zCord]==0) {
			availableMoves.options[indexOfOption] = 4;
			indexOfOption++;
		}
		
		availableMoves.numOfOptions = indexOfOption;
		
		availableMoves.randomDirection = (byte)(Math.random()*indexOfOption); //200 iq numOfOptions strat
		return availableMoves;
		
	}
	
	
	public void turtleMove(){
		
		byte direction;
		final char poop = 'o';
		
		Moves turtleDirection = moveGenerator(timo, poop);
		timo.numOfMoves++;
		
		if(turtleDirection.numOfOptions == 0) { //meaning all directions were skipped and none added to options list
			direction = 0;
		}
		else {
			direction = turtleDirection.options[turtleDirection.randomDirection];
		}
		if(map[timo.yCord][timo.xCord][timo.zCord]=='!') { //true even if on itself because it means it was visable previously
			map[timo.yCord][timo.xCord][timo.zCord] = visionCones;
		} else if (!canBeSeen(timo)){
			map[timo.yCord][timo.xCord][timo.zCord] = ' ';
		}
		ghostMap[timo.yCord][timo.xCord][timo.zCord] = (byte)timo.numOfMoves;
		
		switch(direction) {
		
		case 0: //up down in case the turtle is trapped in its own poop
			if(timo.zCord<height-1) { //zcords should be measured and compared invididually
				timo.zCord++;
			} 
			else if(timo.zCord>0) {
				timo.zCord--;
			}
			break;
		case 1: //forward
			timo.yCord--;
			break;
		case 2: //backward
			timo.yCord++;
			break;
		case 3: //left
			timo.xCord--;
			break;
		case 4: //right
			timo.xCord++;
			break;
		}
		
	}
	
	public int distanceBetween(){ //this method must be called after the changes like distanceBetween(smth.xCord-1) | can be called before
		int distance = Math.abs(timo.xCord-player.xCord) + Math.abs(timo.yCord-player.yCord);
 //save the print for after the method is called so i can return an int
		return distance;
	}
	
	//have to compare ghostMap everytime vision is called.
	public boolean canBeSeen(Character inQuestion) {
		if(map[inQuestion.yCord][inQuestion.xCord][inQuestion.zCord]==visionCones) {
			return true;
		}
		return false;
	}

	public void vision() {
		try{
			
			map[player.yCord+1][player.xCord][player.zCord] = visionCones;
			
			map[player.yCord+1][player.xCord+1][player.zCord] = visionCones;
			map[player.yCord+1][player.xCord-1][player.zCord] = visionCones;
		}catch(Exception e){
			
		}
		try{
			map[player.yCord-1][player.xCord][player.zCord] = visionCones;
			
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
	public void isHot() {
		if(!timo.found) {
			if(Math.abs(player.yCord-timo.yCord)>Math.abs((player.yCord-1)-(timo.yCord))){
				//if within a certain range, print even hotter
				SetUpControls.frame.getContentPane().setBackground(Color.red.darker().darker().darker());
			} else {
				SetUpControls.frame.getContentPane().setBackground(Color.blue.darker().darker().darker());
			}
			else {
				
				//cold blue
			}
		}
		//should i just compare the distance... only one comparison compared to the four for each direction

	}
	public void isCold() {
		
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
		byte random = (byte)(Math.random()*3); //can move 0-2 squares every three moves
		for(int i = 0; i < 20; i++) System.out.println(); //to clear screen
		
		System.out.println("Timo is "+distanceBetween()+" steps away");
		
		move++;
		vision();
		if(!timo.found){
			if(move%3==0){
				for(int i = 0; i < random; i++){
					turtleMove();	
				}
				
			}
			if(canBeSeen(timo)) { //not within the other if to be ontop of vision updates
				map[timo.yCord][timo.xCord][timo.zCord]='!';
			}
		}
		
		//place the color change here using distance 
		
		
		System.out.println("timo location : " + alphabet[timo.xCord] +" "+ (timo.yCord+1) +" "+ (timo.zCord+1));
		map[player.yCord][player.xCord][player.zCord] = 'x';
		if(timo.found||onTurtle()) {
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
