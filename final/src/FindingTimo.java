/*=====================================
 * FindingTimo 
 * Jeremy Su
 * June 17, 2022
 * Java 8
 * ====================================
 * - Problem Definition: Functions of the game including 
 * NPC movement, displaying location, displaying distance from the player
 * and the turtle, and updating the map (visuals of the game).
 * 
 * - Input: Both keyboard input including 'W', 'A', 'S', 'D', UP and DOWN 
 * arrow key and button presses from 'setUpControl.java' 
 * 
 * - Output text: the status of whether timo has been found, the distance between 
 * the player and timo, the move number, the player's current coordinates,
 * the finish point coordinates
 * - Output Map: a blank 15x15 grid (which expands 3 different floors) with
 * letter labelling for the x axis, and Integer values for the y axis
 * - Output character: an exclamation mark which represents Timo, an x which
 * represents the player, an h which represents the hunter, and '.' which
 * represents explored areas and where the player can see, changes the colour
 * of the controls panel based on whether the player is heading towards timo
 * 
 * 
 * - Gist of Processing: processes NPC and player movements and collisions
 * 
 * - Processing Map & Vision: reveals NPC's near the player and explored area, 
 * processes what can be seen by the player
 * - Processing Timo: sets a random starting point for timo, calculates which
 * direction he can move in and moves 0-2 spaces every three moves, removes
 * previous spaces timo has moved on within an invisible version of the map
 * so timo cannot walk on previous spaces
 * - Processing Hunter: sets the hunters position on timo, calculates the distance
 * between hunter and player so that it can chase the player, makes hunter move
 * twice every three moves 
 * - Processing Player: Checks which NPC he collides with, processes the distance between
 * player and timo, processes where player is allowed to move
 * ====================================
 * List of Variables
 * - let timo represent the location and properties of timo (type Character)
 * - let player represent the location and properties of player (type Character)
 * - let hunter represent the location and properties of hunter (type Character)
 * - let finish represent the location of the final checkpoint (type Character)
 * - let mLength represent the length of the grid (type int)
 * - let mWidth represent the width of the grid (type int)
 * - let height represent the height of the grid (type int)
 * - let map represent the grid of the game (type int[][][])
 * - let ghostMap represent the history of timos moves (type int[][][])
 * - let previousDistance be the distance between timo and player before any movement calculations (type int)
 */

import java.awt.Color;

public class FindingTimo {

	/* Character class:
	 * This class is used to create objects that define the player's, npc's, and the finish line's properties
	 * 
	 * List of Local Variables
	 * let xCord represent the x coordinates of the object (type int)
	 * let yCord represent the y coordinates of the object (type int)
	 * let zCord represent the z coordinates of the object (type int)
	 * let numOfMoves represent the number of times the position of an object changes (type int)
	 * let found represent whether or not they've been captured by the player (type boolean)
	 */
	static class Character {
		int xCord;
		int yCord;
		int zCord;
		int numOfMoves;
		boolean found;		
	} //end Character class
	//update

	static Character timo = new Character();
	static Character player = new Character();
	static Character hunter = new Character();
	static Character finish = new Character();

	final static int mLength = 15; 
	final static int mWidth = 15; 
	final static int height = 3;

	static char [][][] map = new char[mLength][mWidth][height];
	static byte [][][] ghostMap = new byte[mLength][mWidth][height];

	int previousDistance = distanceBetween();

	/* run method:
	 * This procedural method sets the values for all characters and the map for each run as well as bring up 
	 * the controls window. It is used to start up a new game
	 * 
	 * @param none
	 * @return void
	 */
	public void run() {

		SetUpControls.setControls();

		//makes the finish point anywhere random in the grid
		finish.xCord = (int)(Math.random()*mWidth);
		finish.yCord = (int)(Math.random()*mLength);
		finish.zCord = (int)(Math.random()*height);

		//makes timo start anywhere random in the grid
		timo = new Character();
		timo.xCord = (int)(Math.random()*mWidth);
		timo.yCord = (int)(Math.random()*mLength);
		timo.zCord = (int)(Math.random()*height);

		//makes the hunter start at timo's coordinates but always on the first floor
		hunter.xCord = timo.xCord;
		hunter.yCord = timo.yCord;
		hunter.zCord = 0;


		//starts the player at the middle on the first floor
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
	}//end run method

	/* update method:
	 * This procedural method works as the motherboard of the program. It centralizes the program
	 * by calling the methods to calculate player and NPC movement and vision to
	 * determine the state of the game. If game doesn't end, it calls the displayGrid method
	 * 
	 * List of Local Variables
	 * let alphabet represent all letters in the alphabet - used to convert x-coordinates to letters (type String[])
	 * let VISIONCONES represent explored and nearby area, granting the player the ability to see NPC's within (type char)
	 * let m represent an object to access non-static methods within the Menu class (type Menu)
	 * @param none
	 * @return void
	 */
	public void update() { 
		String[]alphabet = "abcdefghijklmnopqrstuvwxyz".split("");
		final char VISIONCONES = '.';
		Menu m = new Menu();

		for(int i = 0; i < 20; i++) System.out.println(); //to clear screen

		player.numOfMoves++;
		vision(VISIONCONES);

		if(timo.found||checkOnTop(timo)) {
			if(checkOnTop(finish)){
				System.out.println("You win!");
				System.out.println(Menu.playerName + ", your score was" + player.numOfMoves);
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
	}//end update method

	/* displayGrid method:
	 * This procedural method displays the number of total moves, player's current coordinates,
	 * and the map on the current floor in the current state of the game
	 * 
	 * @param 
	 * floor - the current floor the player is at (type int)
	 * alphabet - all the letters in the alphabet (type String[])
	 * @return void
	 */
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
	}//end displayGrid method

	/* checkOnTop method:
	 * This functional method returns whether or not an NPC is on top of the player
	 * 
	 * @param
	 * onTop - the NPC that is going to be checked (type Character)
	 * 
	 * @return if NPC is on top of player (type Boolean)
	 */
	private boolean checkOnTop(Character onTop) {
		if(onTop.xCord==player.xCord&&onTop.yCord==player.yCord&&onTop.zCord==player.zCord) {
			onTop.found = true;

			return true;
		}
		return false;
	}//end checkOnTop method

	/* hunterMOvement method:
	 * this procedural method removes its symbol from the grid in preperation for a move.
	 * Then it calculates which moves would bring it closer to the player and changes it coordinates
	 * 
	 * List of Local Variables 
	 * let deltaX be the horizontal difference between the player and the hunter (type int)
	 * let deltaY be the vertical difference between the player and the hunter (type int)
	 * let distanceX be the horizontal distance between the player and hunter (type int)
	 * let distanceY be the vertical distance between the player and the hunter (type int)
	 * 
	 * @param
	 * VISIONCONES - the character on the map used to represent explored or nearby areas (type char)
	 * @return void
	 */
	private void hunterMovement(char VISIONCONES){

		int deltaX = player.xCord-hunter.xCord;
		int deltaY = player.yCord-hunter.yCord;

		int distanceX = Math.abs(deltaX);
		int distanceY = Math.abs(deltaY);

		if(map[hunter.yCord][hunter.xCord][hunter.zCord]=='h') {
			map[hunter.yCord][hunter.xCord][hunter.zCord] = VISIONCONES;
		}

		if(distanceX>=distanceY) {

			if(deltaX>0&&hunter.xCord+1<mWidth) {
				hunter.xCord++; //move right
			}
			if(deltaX<0&&hunter.xCord-1>=0) {
				hunter.xCord--; //move left
			}
		}

		if(distanceX<distanceY) {

			if(deltaY>0&&hunter.yCord+1<mWidth) {
				hunter.yCord++; //move down
			}
			if(deltaY<0&&hunter.yCord-1>=0) {
				hunter.yCord--; //move up
			}
		}
	}//end hunterMovement method

	
	/* moveGenerator method:
	 * This functional method calculates possible moves for timo and returns them in an array
	 * 
	 * List of Local Variables
	 * let maxNumOfOptions represent the number of possible moves (type byte)
	 * let options represent the available directions timo can go in including the number of available moves(type byte[])
	 * 
	 * @param 
	 * movement - representing timo's properties (type Character)
	 * @return array containing available moves and number of moves (type byte[])
	 */

	private byte[] moveGenerator(Character movement) {

		final byte maxNumOfOptions = 5; //4 slots for moves, 1 slot at end of array for the number of available moves
		byte []options = new byte[maxNumOfOptions];

		//checks which sides are open. adds them into a draw. //if nothing in draw, goes up and down

		//adds the directions into a raffel!

		if(movement.yCord>0&&ghostMap[movement.yCord-1][movement.xCord][movement.zCord]==0) {
			options[options[4]] = 1;
			options[4]++;
		}

		if(movement.yCord<mLength-1&&ghostMap[movement.yCord+1][movement.xCord][movement.zCord]==0) {
			options[options[4]] = 2;
			options[4]++;
		}

		if(movement.xCord>0&&ghostMap[movement.yCord][movement.xCord-1][movement.zCord]==0) {
			options[options[4]] = 3;
			options[4]++;
		}
		if(movement.xCord<mWidth-1&&ghostMap[movement.yCord][movement.xCord+1][movement.zCord]==0) {
			options[options[4]] = 4;
			options[4]++;
		}

		return options;
	}//end moveGenerator method
	
	
	/* turlteMove method:
	 * This procedural method moves the turtle in a random direction
	 * 
	 * List of Local Variables
	 * let direction be the chosen direction of timo's move (type byte)
	 * let turtleDirection be the available directions (type byte[])
	 * 
	 * @param
	 * VISIONCONES - the character on the map used to represent explored or nearby areas (type char)
	 * 
	 * @return void
	 */
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
			if(timo.zCord<height-1) {
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

	}//end turtleMove method

	/* canBeSeen method:
	 * This functional method returns whether or not a NPC can be seen by the player
	 * 
	 * @param
	 * inQuestion - the NPC thats being checked (type Character)
	 * VISIONCONES - the character on the map used to represent explored or nearby areas (type char)
	 * 
	 * @return if the NPC can be seen by the player (type boolean)
	 */
	private boolean canBeSeen(Character inQuestion, char VISIONCONES) {
		if(map[inQuestion.yCord][inQuestion.xCord][inQuestion.zCord]==VISIONCONES) {
			return true;
		}
		return false;
	}//end canBeSeen method
	
	/* vision method:
	 * This procedural method reveals the players nearby squares and leaves an "explored" path
	 * 
	 * @param 
	 * VISIONCONES - the character on the map used to represent explored or nearby areas (type char)
	 * 
	 * @return void
	 */
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

	}//end vision method
	
	
	/* distanceBetween method:
	 * This functional method will return the overall distance between timo and the player
	 * 
	 * List of Local Variables
	 * let distance represent the distance bewteen timo and the player (type int)
	 * 
	 * @param none
	 * @return distance between timo and the player (type int)
	 */
	private int distanceBetween(){ //this method must be called after the changes like distanceBetween(smth.xCord-1) | can be called before
		int distance = Math.abs(timo.xCord-player.xCord) + Math.abs(timo.yCord-player.yCord);
		return distance;
	}//end distanceBetween method
	
	
	/* isHot method:
	 * This procedural method changes the colour of the controls panel depending on the direction and distance of the player and the turtle
	 * 
	 * @param 
	 * previousDistance - the distance between timo and player before they move (type int)
	 * currentDistance - the distance between timo and player after they move (type int)
	 * @return void
	 */
	private void isHot(int previousDistance, int currentDistance) {
		if(currentDistance<=previousDistance) {
			if(currentDistance<=5) { //if within 5 moves, very hot
				SetUpControls.frame.getContentPane().setBackground(Color.red);
			} else {
				SetUpControls.frame.getContentPane().setBackground(Color.red.darker().darker().darker());
			}

		} else {
			SetUpControls.frame.getContentPane().setBackground(Color.blue.darker().darker().darker());
		}
	}//end isHot method
	
	
	/* turtleMoveTracker method:
	 * This procedural method adds all the components of the turtle's move together and sets a range of how much the turtle can move
	 * 
	 * List of Local Variables
	 * let random represent the random number of moves the turtle will move (type byte)
	 * 
	 * @param
	 * VISIONCONES - the character on the map used to represent explored or nearby areas (type char)
	 * @return void
	 */
	private void turtleMoveTracker(char VISIONCONES) {
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
	}//end turtleMoveTracker method
	
	
	/* revealMap method:
	 * this procedural method reveals all npc's on the map and makes every square explored
	 * 
	 * @param none
	 * @return void
	 */
	public void revealMap() {
		for(int i = 0; i < mWidth; i++) {
			for(int j = 0; j < mLength; j++) {
				for(int x = 0; x < height; x++) {
					map[j][i][x] = '.';
				}
			}
		}
		update();
	}//end revealMap method
}