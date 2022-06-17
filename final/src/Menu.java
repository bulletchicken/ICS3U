/*=====================================
 * Menu
 * Jeremy Su
 * June 17, 2022
 * Java 8
 * ====================================
 * - Problem Definition: Required communication with user before and after the game
 * - Input: yes or no if the player wants to play (again), what is their username
 * (between 3 and 12 characters), a score between 1-10 what they would rate the game
 * - Output: Title screen, intro questions, loss screen, win screen, leaderboard
 * of top 10 scores of all time from a .txt file
 * - Processing: extracting scores and names from .txt file and sorting scores
 * ====================================
 * List of Variables
 * - let br represent the object to collect user input (type BufferedReader)
 * - let playerName represent the username of the current player for the current run (type String)
 */

import java.io.*;
import java.util.Scanner;

public class Menu {

	/* playerScore class:
	 * This class is used to makes objects to store the individual scores from the .txt file
	 * 
	 * List of Local Variables
	 * let name represent the name of the run
	 * let score represent the score of the run
	 */
	class playerScore{
		String name;
		int score;
	}//end playerScore class

	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	static String playerName = "";


	/* main method:
	 * This procedural method is first to be called and is used to start up the game by calling other methods.
	 * It is also used to display the title screen and user advice
	 * 
	 * List of Local Variables
	 * let m represent an object to access non-static methods within the Menu class (type Menu)
	 * @param args (type String[])
	 * @throws IO Exception
	 * @return void
	 */
	public static void main(String[]args) throws IOException {

		Menu m = new Menu();
		//m.rules();
		//m.controlHelp();
		System.out.println("\n-----Finding Timo-----");
		System.out.println("      by Jeremy\n");
		System.out.println("This is timo...");
		System.out.println("\n      oo/><><>\\\n     ( -)><><><>\n      L|_|L|_|`) ");
		System.out.println("\n            ...he is lost.\n");
		m.displayIntro(); //includes : method1, method2
		System.out.println("!!!Make sure to have the window open ontop to utilize keyboard controls!!!");
		System.out.println("click on the 'controls' or 'rules' button on the window for help");

	}//end main method


	/* displayIntro method:
	 * This procedural method is used to ask questions to the user to continue playing
	 * 
	 * List of local variables
	 * let fT represent an object to access non-static methods within the FindingTimo class (type FindingTimo)
	 * 
	 * @param none
	 * @throws IOException
	 * @return void
	 */
	private void displayIntro() throws IOException {
		FindingTimo fT = new FindingTimo();

		System.out.print("Can you help me find him?... (Yes|No): ");
		if(continuePlaying()) { //if yes
			playerName = nameInput();
			System.out.println("What an excellent name " + playerName + "!");
			fT.run();

		} else {
			goodbye();
		}
	}//end displayIntro method


	/* nameInput method:
	 * This functional method is used to retrieve the user's desired name between 3 and 12 characters
	 * 
	 * List of Local Variables
	 * let name represent the user's desired name for the run (type String)
	 * let nameLen represent the length of the name (type int)
	 * 
	 * @param none
	 * @throws IOException
	 * @return desired name (type String) or returns itself to repeat
	 */
	private String nameInput() throws IOException {

		System.out.print("What is your name? : ");
		String name = br.readLine();
		int nameLen = name.length();
		if(nameLen>=3 && nameLen <= 12){
			return name;
		}
		else{
			System.out.println("Please enter a name between 3-12 chararcters");
			return nameInput();
		}
	}//end nameInput method
	
	
	/* continuePlaying method:
	 * This functional method is used to retrieve the answer whether the user wants to continue playing
	 * 
	 * List of Local Variables
	 * let CONTINUE represent the required answer to continue playing (type String)
	 * let END represent the required answer to stop playing (type String)
	 * 
	 * @param none
	 * @throws IOException
	 * @return whether the user wants to continue playing (type boolean)
	 */
	private boolean continuePlaying()throws IOException{
		final String CONTINUE = "yes";
		final String END = "no";

		String answer = br.readLine().toLowerCase();

		if(answer.equals(CONTINUE)){
			System.out.println("Great!");
			return true;
		}
		else if (answer.equals(END)){
			System.out.println("it looks like i will have to do it myself...");
			return false;
		}
		else{
			System.out.println("not a valid option. please try again");
			return continuePlaying();
		}
	}//end continuePlaying method
	
	/* loseScreen method:
	 * This procedural method is used to display the message for losing and asks
	 * if the user wants to play again by calling the displayIntro method to reset
	 * 
	 * @param none
	 * @return void
	 */
	public void loseScreen(){
		SetUpControls.closeWindow();
		displayBoard();
		System.out.println("You got caught? timo still needs saving!");
		try{
			displayIntro();
		} catch(IOException e){
			e.printStackTrace();
		}

	}//end loseScreen method

	
	/* endingScreen method:
	 * This procedural method is used to display the message for winning, adds their score to the file,
	 * and asks if the user wants to play again by calling the displayIntro method to reset
	 * 
	 * @param
	 * score - the number of moves it took for the player to win the run (type int)
	 * @return void
	 */
	public void endingScreen(int score){
		SetUpControls.closeWindow();
		addScore(score);
		displayBoard();
		System.out.println("\nwait what?!? timo is lost again!");
		try {
			displayIntro();
		}catch(IOException e) {
			e.printStackTrace(); 
		}
	}//end endingscreen method


	/* displayBoard method:
	 * This procedural method is used to display the top 10 scores from the leaderboard extracted from the leaderboard file
	 * 
	 * @param none
	 * @return void
	 */
	private void displayBoard(){
		playerScore[] gameLeaderBoard;
		try {
			gameLeaderBoard = extractLeaderBoard();

			System.out.println("\n\n\n   Player | #of moves");
			System.out.println("   -----------------");

			int index = 0;
			while(index<10&&gameLeaderBoard[index]!=null) {
				System.out.println("#" + (index+1) + " : " + gameLeaderBoard[index].name + " | " + gameLeaderBoard[index++].score);
			}
			System.out.println();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

	}//end displayBoard method
	
	
	/* extractLeaderBoard method:
	 * This functional method is used to extract all the scores from the leaderboard file and return it in an array
	 * 
	 * List of Local Variables
	 * let board represent an object used to find the path of the leaderboard.txt file (type File)
	 * let count represent an object used to count the number of lines in the leaderboard.txt file (type Scanner)
	 * let fileReader represent an object used to read the text in the leaderboard.txt file (type Scanner)
	 * 
	 * @param none
	 * @throws FileNotFoundException
	 * @returns an object array filled with names and scores (type playerScore)
	 */
	private playerScore[] extractLeaderBoard() throws FileNotFoundException{

		File board = new File("src/leaderboard.txt");
		Scanner count = new Scanner(board);	

		int numOfSubmissions = 0;

		try {
			while(count.hasNext()) {
				count.nextLine();
				numOfSubmissions++;
			}
			count.close();
		}catch(Exception e) {
			e.getStackTrace();
		}
		//reset
		playerScore [] extracted = new playerScore[numOfSubmissions];
		try {
			Scanner fileReader = new Scanner(board);

			while(numOfSubmissions-->0&&fileReader.hasNext()) {
				extracted[numOfSubmissions] = new playerScore();
				extracted[numOfSubmissions].name = fileReader.next();
				extracted[numOfSubmissions].score = fileReader.nextInt();
			}
			fileReader.close();
		}catch(Exception e) {
			e.getStackTrace();
		}

		return insertionSort(extracted, extracted.length);
	}//end extractLeaderBoard method
	
	
	/*insertionSort method:
	 * this functional method is used to sort the scores of an array from smallest to greatest
	 * 
	 * List of Local Variables
	 * let considered represent the score going to be sorted in the current loop (type playerScore)
	 * 
	 * @param
	 * unSorted - the object array that is going to be sorted (type playerScore)
	 * size - the length of the array (type int)
	 * @return the sorted array (type playerScore[])
	 */
	private playerScore[] insertionSort(playerScore[]unSorted, int size) {

		for(int i = 1; i < size; i++) {

			playerScore considered = unSorted[i];
			int sizeOfSorted = i-1;//always behind considered

			while(sizeOfSorted>=0&&unSorted[sizeOfSorted].score>considered.score) {
				unSorted[sizeOfSorted+1] =unSorted[sizeOfSorted];
				sizeOfSorted--;
			}
			unSorted[sizeOfSorted+1] = considered;			
		}


		/*
		 * bubble sort : 
		 * 
		for(int i = 0; i < size; i++) {
			for(int j = i; j < size; j++) {
				if(unSorted[i].score>unSorted[j].score) {
					playerScore temp = unSorted[i];
					unSorted[i] = unSorted[j];
					unSorted[j] = temp;
				}
			}
		}
		 */

		return unSorted;
	}//end insertionSort method

	
	/* addScore method:
	 * This procedural method writes the score of the current run into the text file
	 * 
	 * List of Local Variables
	 * let writer represent the object that will append a value into the target file (type FileWriter)
	 * @param
	 * score - the number of moves it took for the player to win the run (type int)
	 * @return void
	 */
	private void addScore(int score) {

		try {
			FileWriter writer = new FileWriter("src/leaderboard.txt", true);
			writer.append("\n" + playerName + " " + String.valueOf(score));

			writer.close();
		} catch(IOException e) {

		}

	}//end addScore method

	/* rules method:
	 * This procedural method displays the rules of the game
	 * @param none
	 * @return void
	 */
	public void rules(){
		System.out.println("\n-----Rules-----\n");
		System.out.println("Timo is lost and needs your help finding\nhim. In order to find him, you will have\nto traverse a 3d grid.\n");
		System.out.println("There are three floors to the 15x15 grid.\nAt first it is blank, but as you move,\nthe area around you becomes 'explored'.");
		System.out.println("While by you won't be able to see\nanything, explored areas will reveal\nwhenever timo or the hunter walk on it.\n");
		System.out.println("When you eventually find timo, you will\nbe given a random destinationwhere you\nmust return him and win the game.");
		System.out.println("Unfortunately for you, a hunter will be\nchasing you as you move\n");
		System.out.println("\n---------------\n");
	}//end rules method
	
	
	
	/* controlHelp method:
	 * This procedural method displays the help for the controls of the game
	 * @param none
	 * @return void
	 */
	public void controlHelp(){
		System.out.println("\n-----Controls-----\n");
		System.out.println("!!!This game utilizes keyboard input so make sure to have the controls window open on top when the game starts!!!\n");

		System.out.println("- press 'w' to move foward\n- press 'a' to move left\n- press 's' to move backwards\n- press 'd' to move right\n- press arrowup to go up a floor\n- press arrowdown to go down a floor\n");

		System.out.println("You have a few tools:");
		System.out.println("- a tracker above the grid which shows how far you are from timo\n- a control screen which changes colours depending on how hot or cold you are to him");
		System.out.println("- a gps which shows your current coordinates\n- a hint button which allows you to find his location at the cost of adding 5 moves to your score");
		System.out.println("- a button to display the rules\n- a button to display the controls");
		System.out.println("\nThere are a few admin tools:");
		System.out.println("- a win button which allows you to instantly win the game\n- a give up button which allows you to instantly lose the game");
		System.out.println("- a reveal button which shows the entire map and all locations\n");
		System.out.println("\n---------------\n");
	}//end controlHelp method
	
	
	/* goodbye method:
	 * this procedural method asks the user to rate the game and responds before ending the program
	 * 
	 * List of local variables
	 * let rating represent the number of starts out of 10 based on user (type int)
	 * @param none
	 * @return void
	 */
	public void goodbye() {
		System.out.print("out of 10 stars, how would you rate my game? : ");
		int rating = intInput();
		if(rating==10) {
			System.out.println("WOW! Glad you liked it");
		} else if(rating<5) {
			System.out.println("Oh... sorry :/");
		} else if(rating<9) {
			System.out.println("stay in touch for more games!");
		}

		System.out.println("\nthanks for playing. goodbye...");
	}

	
	/*intInput method:
	 * this functional method retrieves the user input between 0 and 10
	 * 
	 * List of Local Variables 
	 * let input represent the score the user inputs between 0 and 10 (type int)
	 * @param none
	 * @return an int value between 0 and 10 (type int)
	 */
	private int intInput() {
		try { 

			int input = Integer.parseInt(br.readLine());
			if(input>0&&input<=10) {
				return input;
			} else {
				System.out.println("invalid answer. Try again");
				return intInput();
			}
		}catch(Exception e) {
			System.out.println("invalid answer. Try again");
			return intInput();
		}
	}
}