import java.awt.Color;

public class FindingTimo {

	static class Character {
		int xCord;
		int yCord;
		int zCord;
		int numOfMoves;
		boolean found;		
	}
	//update

	static Character timo = new Character();
	static Character player = new Character();
	static Character hunter = new Character();
	static Character finish = new Character();

	final static int mLength = 15; //vertical
	final static int mWidth = 15; //horizontal
	final static int height = 3; //up down

	static char [][][] map = new char[mLength][mWidth][height];
	static byte [][][] ghostMap = new byte[mLength][mWidth][height];

	int previousDistance = distanceBetween();
	
	public void run() {

		SetUpControls.setControls();
		//starting cords
		
		finish.xCord = (int)(Math.random()*mWidth);
		finish.yCord = (int)(Math.random()*mLength);
		finish.zCord = (int)(Math.random()*height);
		//
		
		timo = new Character();
		timo.xCord = (int)(Math.random()*mWidth);
		timo.yCord = (int)(Math.random()*mLength);
		timo.zCord = (int)(Math.random()*height);
		
		//make the hunter start at timo
		
		hunter.xCord = timo.xCord;
		hunter.yCord = timo.yCord;
		hunter.zCord = 0;
		
		
		//starting cords in the middle
		
		player = new Character();
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
	
	public void update() { 
		String[]alphabet = "abcdefghijklmnopqrstuvwxyz".split("");
		final char VISIONCONES = '.';
		Menu m = new Menu();
		
		for(int i = 0; i < 20; i++) System.out.println(); //to clear screen

		player.numOfMoves++;
		vision(VISIONCONES);
		
		if(timo.found||checkOnTop(timo)) {
			if(atBedroom()){
				System.out.println("You win!");
				m.endingScreen(player.numOfMoves);
				return; //breaks the method
			}
			System.out.println("Timo has been found! Return the turtle to coordinates (" + alphabet[finish.xCord] + ", " + (finish.yCord+1) + ", " + (finish.zCord+1) + ")");
		} else {
			System.out.println("Timo has not been found...");
			turtleMoveTracker(VISIONCONES);
		}
		
		if(checkOnTop(hunter)){ 
			System.out.println("You were caught!");
			m.loseScreen();
			return;
		}
		if(player.numOfMoves%3==0) { //make it run two every three 
			for(int i = 0; i < 2; i++) {
				hunterMovement(VISIONCONES);
				if(checkOnTop(hunter)){
					System.out.println("You were caught!");
					m.loseScreen();
					return;
				}
			}
			
		}
		if(canBeSeen(hunter, VISIONCONES)) {
			map[hunter.yCord][hunter.xCord][hunter.zCord]='h';
		}
		
		map[player.yCord][player.xCord][player.zCord] = 'x';

		displayGrid(player.zCord, alphabet);
	}
	
	private void displayGrid(int floor, String[]alphabet) {

		System.out.println("Move #" + player.numOfMoves);
		System.out.println("Current Location: " + "(" + alphabet[player.xCord] + ", "+(player.yCord+1) + ", " + (player.zCord+1) + ")");
		
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
	
	private boolean atBedroom() {

		//make a random point the player has to reach on another floor
		if(player.yCord == finish.yCord && player.xCord == finish.xCord && player.zCord == finish.zCord){
			return true;
		}
		return false;
	}

	private boolean checkOnTop(Character onTop) {
		if(onTop.xCord==player.xCord&&onTop.yCord==player.yCord&&onTop.zCord==player.zCord) {
			onTop.found = true;

			return true;
		}
		return false;
	}
	
	
	//turtle methods
	
	private void turtleMove(char VISIONCONES){
		
		byte direction;

		byte [] turtleDirection = moveGenerator(timo); 
		
		//direction 0 = up or down
		//direction 1 = forward
		//direction 2 = backwards
		//direction 3 = left
		//direction 4 = right
		
		timo.numOfMoves++;

		if(turtleDirection[4] == 0) { //meaning all directions were skipped and none added to options list
			direction = 0;
		}
		else {
			direction = turtleDirection[(byte)(Math.random()*turtleDirection[4])];
		}
		if(map[timo.yCord][timo.xCord][timo.zCord]=='!') { //true even if on itself because it means it was visable previously
			map[timo.yCord][timo.xCord][timo.zCord] = VISIONCONES;
		} else if (!canBeSeen(timo, VISIONCONES)){
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

	private byte[] moveGenerator(Character movement) {
		
		
		final byte maxNumOfOptions = 5; //4 slots for moves, 1 slot at end for number of available moves
		byte []options = new byte[maxNumOfOptions];

		//checks which sides are open. adds them into a draw. //if nothing in draw, goes up and down

		//adds the directions into a raffel!

		byte numOfOption = 0;

		if(movement.yCord>0&&ghostMap[movement.yCord-1][movement.xCord][movement.zCord]==0) {
			options[numOfOption] = 1;
			numOfOption++;
		}

		if(movement.yCord<mLength-1&&ghostMap[movement.yCord+1][movement.xCord][movement.zCord]==0) {
			options[numOfOption] = 2;
			numOfOption++;
		}

		if(movement.xCord>0&&ghostMap[movement.yCord][movement.xCord-1][movement.zCord]==0) {
			options[numOfOption] = 3;
			numOfOption++;
		}
		if(movement.xCord<mWidth-1&&ghostMap[movement.yCord][movement.xCord+1][movement.zCord]==0) {
			options[numOfOption] = 4;
			numOfOption++;
		}
		
		options[4] = numOfOption;

		return options;
	}


	//have to compare ghostMap everytime vision is called.
	private boolean canBeSeen(Character inQuestion, char VISIONCONES) {
		if(map[inQuestion.yCord][inQuestion.xCord][inQuestion.zCord]==VISIONCONES) {
			return true;
		}
		return false;
	}

	private void vision(char VISIONCONES) {
		
		//update the try catch to remove unncessary catch
		if(player.yCord<mLength-1) {
			map[player.yCord+1][player.xCord][player.zCord] = VISIONCONES;
		}
		if(player.yCord<mLength-1&&player.xCord<mWidth-1) {
			map[player.yCord+1][player.xCord+1][player.zCord] = VISIONCONES;
		}
		if(player.yCord<mLength-1&&player.xCord>0) {
			map[player.yCord+1][player.xCord-1][player.zCord] = VISIONCONES;
		}
		
		
		if(player.yCord>0) {
			map[player.yCord-1][player.xCord][player.zCord] = VISIONCONES;
		}
		if(player.yCord>0&&player.xCord<mWidth-1) {
			map[player.yCord-1][player.xCord+1][player.zCord] = VISIONCONES;
		}
		if(player.yCord>0&&player.xCord>0) {
			map[player.yCord-1][player.xCord-1][player.zCord] = VISIONCONES;
		}
		
		
		if(player.xCord<mWidth-1) {
			map[player.yCord][player.xCord+1][player.zCord] = VISIONCONES;
		}
		if(player.xCord>0) {
			map[player.yCord][player.xCord-1][player.zCord] = VISIONCONES;
		}

	}

	private int distanceBetween(){ //this method must be called after the changes like distanceBetween(smth.xCord-1) | can be called before
		int distance = Math.abs(timo.xCord-player.xCord) + Math.abs(timo.yCord-player.yCord);
		return distance;
	}

	public void isHot(int previousDistance, int currentDistance) {
		if(currentDistance<=previousDistance) {
			if(currentDistance<=5) { //if within 5 moves, very hot
				SetUpControls.frame.getContentPane().setBackground(Color.red);
			} else {
				SetUpControls.frame.getContentPane().setBackground(Color.red.darker().darker().darker());
			}
			
		} else {
			SetUpControls.frame.getContentPane().setBackground(Color.blue.darker().darker().darker());
		}
	}
	
	public void turtleMoveTracker(char VISIONCONES) {
		byte random = (byte)(Math.random()*3); //can move 0-2 squares every three moves
		if(player.numOfMoves%2==0){
			for(int i = 0; i < random; i++){
				turtleMove(VISIONCONES);	
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
		
		
		
		if(canBeSeen(timo, VISIONCONES)) { //not within the other if to be ontop of vision updates
			map[timo.yCord][timo.xCord][timo.zCord]='!';
		}
	}
	
	
	
	
	private void hunterMovement(char VISIONCONES){
		
		int deltaX = player.xCord-hunter.xCord;
		int deltaY = player.yCord-hunter.yCord;
		
		int distanceX = Math.abs(deltaX);
		int distanceY = Math.abs(deltaY);
		
		if(map[hunter.yCord][hunter.xCord][hunter.zCord]=='h') {
			map[hunter.yCord][hunter.xCord][hunter.zCord] = VISIONCONES;
		}
		
		if(distanceX>=distanceY) {
			//left right
			
			if(deltaX>0&&hunter.xCord+1<mWidth) {
				System.out.println("going right");
				hunter.xCord++;
			}
			if(deltaX<0&&hunter.xCord-1>=0) {
				System.out.println("going left");
				hunter.xCord--;
			}
		}
		
		if(distanceX<distanceY) {
			//up down
			
			if(deltaY>0&&hunter.yCord+1<mWidth) {
				System.out.println("go down");
				hunter.yCord++;
			}
			if(deltaY<0&&hunter.yCord-1>=0) {
				System.out.println("going up");
				hunter.yCord--;
			}
			
		}
		
	}
	
	public void revealMap() {
		for(int i = 0; i < mWidth; i++) {
			for(int j = 0; j < mLength; j++) {
				for(int x = 0; x < height; x++) {
					map[j][i][x] = '.';
				}
			}
		}
		
		update();
	}

}