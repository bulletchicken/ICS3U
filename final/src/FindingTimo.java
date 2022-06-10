import java.awt.Color;

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

	static Character timo = new Character();
	static Character player = new Character();
	Character finish = new Character();

	

	final static int mLength = 15; //vertical
	final static int mWidth = 15; //horizontal
	final static int height = 3; //up down

	int move = 0; //make a universal move counter to display number of moves and use to do turtle speed if(move%4==0) or smth

	static char [][][] map = new char[mLength][mWidth][height];
	static byte [][][] ghostMap = new byte[mLength][mWidth][height];

	int previousDistance = distanceBetween();

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


	public void turtleMove(char visionCones){

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
		} else if (!canBeSeen(timo, visionCones)){
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



	//have to compare ghostMap everytime vision is called.
	public boolean canBeSeen(Character inQuestion, char visionCones) {
		if(map[inQuestion.yCord][inQuestion.xCord][inQuestion.zCord]==visionCones) {
			return true;
		}
		return false;
	}

	public void vision(char visionCones) {
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

	public int distanceBetween(){ //this method must be called after the changes like distanceBetween(smth.xCord-1) | can be called before
		int distance = Math.abs(timo.xCord-player.xCord) + Math.abs(timo.yCord-player.yCord);

		//save the print for after the method is called so i can return an int
		return distance;
	}

	public void isHot(int previousDistance, int currentDistance) {
		if(currentDistance<=previousDistance) {
			if(currentDistance<5) { //if within 5 moves, very hot
				SetUpControls.frame.getContentPane().setBackground(Color.red);
			} else {
				SetUpControls.frame.getContentPane().setBackground(Color.red.darker().darker().darker());
			}
			
		} else {
			SetUpControls.frame.getContentPane().setBackground(Color.blue.darker().darker().darker());
		}
	}
	
	public void turtleMoveTracker(char visionCones) {
		byte random = (byte)(Math.random()*3); //can move 0-2 squares every three moves
		if(move%2==0){
			for(int i = 0; i < random; i++){
				turtleMove(visionCones);	
			}
		}
		
		int currentDistance = distanceBetween();
		if(timo.zCord<player.zCord) {
			System.out.println("You hear footsteps below you");
		}
		else if(timo.zCord>player.zCord) {
			System.out.println("You hear footsteps above you");
		} else {
			System.out.println("Timo is "+currentDistance+" steps away");
		}
		
		isHot(previousDistance, currentDistance); //changes the color of controls
		
		previousDistance = currentDistance;
		
		if(canBeSeen(timo, visionCones)) { //not within the other if to be ontop of vision updates
			map[timo.yCord][timo.xCord][timo.zCord]='!';
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
		final char visionCones = '.';
		Menu m = new Menu();

		
		for(int i = 0; i < 20; i++) System.out.println(); //to clear screen

		move++;
		vision(visionCones);

		if(timo.found||onTurtle()) {
			if(atBedroom()){
				m.endingScreen();
				return; //breaks the method
			}
			System.out.println("Return the turtle to coordinates (" + alphabet[finish.xCord] + ", " + (finish.yCord+1) + ", " + (finish.zCord+1) + ")");
		} else {
			turtleMoveTracker(visionCones);
			System.out.println("timo's current location (he's still on the run!) : " + alphabet[timo.xCord] +" "+ (timo.yCord+1) +" "+ (timo.zCord+1)); //save this for the hint
		}

		map[player.yCord][player.xCord][player.zCord] = 'x';

		displayGrid(player.zCord, alphabet);
	}

	public boolean atBedroom() {

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

			finish.xCord = (int)(Math.random()*mWidth);
			finish.yCord = (int)(Math.random()*mLength);
			finish.zCord = (int)(Math.random()*height);

			return true;
		}
		return false;
	}

	public void run() {
		SetUpControls.setControls();
		//starting cords
		timo.xCord = (int)(Math.random()*mWidth);
		timo.yCord = (int)(Math.random()*mLength);
		timo.zCord = (int)(Math.random()*height);
		//starting cords in the middle
		player.xCord = mWidth/2;
		player.yCord = mLength/2;
		player.zCord = 0;

		//setup the map and resets it on every run
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[0].length; j++) {
				for(int k = 0; k < map[0][0].length; k++) {
					map[i][j][k] = ' ';
				}
			}
		}
		update();
	}


}
