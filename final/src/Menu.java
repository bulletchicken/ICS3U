import java.io.*;
import java.util.Scanner;

public class Menu {
	
	static String playerName = "";

	class playerScore{ //I will make a map array out of this
		String name;
		int score;
	}
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	boolean gameStart = false;
	
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
		
	}
	
	public void displayIntro() throws IOException {
		FindingTimo fT = new FindingTimo();

		System.out.print("Can you help me find him?... (Yes|No): ");
		gameStart = continuePlaying();
		if(gameStart) { //if yes
			playerName = nameInput();
			System.out.println("What an excellent name " + playerName + "!");
			fT.run();
			System.out.println("!!!Make sure to have the window open ontop to utilize keyboard controls!!!");
			System.out.println("Enter 'rules' or 'controls' to display help menus");
			
			//it asks for the loop to run once more before ending. How to remove... //ill just move this to the update
		} else { //if no
			goodbye();
		}
	}
	public void helpRequest() throws IOException {
		String option = br.readLine();
		if(option.equals("rules")) rules();
		else if(option.equals("controls")) controlHelp();
		else {
			System.out.println("invalid option. Enter 'rules' or 'controls' to display help");
		}
	}
	
	public boolean continuePlaying()throws IOException{
		final String CONTINUE = "yes";
		final String END = "no";

		String answer = br.readLine().toLowerCase();
		
		if(answer.equals(CONTINUE)){
			System.out.println("Great!");
			gameStart = true;
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
	}
	
	public void displayBoard(){
		playerScore[] gameLeaderBoard;
		try {
			gameLeaderBoard = extractLeaderBoard();
			
			System.out.println("Player | #of moves");
			System.out.println("-----------------");
			
			int index = 0;
			while(index<10&&gameLeaderBoard[index]!=null) {
				System.out.println(gameLeaderBoard[index].name + " | " + gameLeaderBoard[index++].score);
			}
		} catch (FileNotFoundException e) {
	
			e.printStackTrace();
		}

	}

	//edit for later: go back and fix the try catch to match errors
	public playerScore[] extractLeaderBoard() throws FileNotFoundException{
	
		File board = new File("src/leaderboard.txt");
		Scanner count = new Scanner(board);
		
		//go back to the previous state where i count the lines then i go add into an array
		
		
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
		
		//here remove the last value from the list
	}
	
	
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
	}
	
	
	public void endingScreen(int score){
		SetUpControls.closeWindow();
		addScore(score);
		displayBoard();
		gameStart = false;
		System.out.println("\nwait what?!? timo is lost again!");
		try {
			displayIntro();
		}catch(IOException e) {
			e.printStackTrace(); 
		}
	}


	public String nameInput() throws IOException {
		
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
	
	public void rules(){
		System.out.println("\n-----Rules-----\n");
		System.out.println("Timo is lost and needs your help finding\nhim. In order to find him, you will have\nto traverse a 3d grid.\n");
		System.out.println("There are three floors to the 15x15 grid.\nAt first it is blank, but as you move,\nthe area around you becomes 'explored'.");
		System.out.println("While by you won't be able to see\nanything, explored areas will reveal\nwhenever timo or other npcs walk on it.\n");
		System.out.println("When you eventually find timo, you will\nbe given a random destinationwhere you\nmust return him and win the game.");
		System.out.println("Unfortunately for you, npcs will be\nchasing you as you move\n");
	}
	
	public void controlHelp(){
		System.out.println("\n-----Controls-----\n");
		System.out.println("!!!This game utilizes keyboard input so make sure to have the controls window open on top when the game starts!!!\n");
		
		System.out.println("- press 'w' to move foward\n- press 'a' to move left\n- press 's' to move backwards\n- press 'd' to move right\n- press arrowup to go up a floor\n- press arrowdown to go down a floor\n");
		
		System.out.println("You have a few tools:");
		System.out.println("- a tracker above the grid which shows how far you are from timo\n- a control screen which changes colours depending on how hot or cold you are to him");
		System.out.println("- a gps which shows your current coordinates\n- a hint button which allows you to find his location at the cost of adding 5 moves to your score");
		System.out.println("\nThere are a few admin tools:");
		System.out.println("- a win button which allows you to instantly win the game\n- a give up button which allows you to instantly lose the game\n");
		System.out.println("\n---------------\n");
	}
	
	private void addScore(int score) {
		
		try {
			FileWriter writer = new FileWriter("src/leaderboard.txt", true);
			writer.append("\n" + playerName + " " + String.valueOf(score));
			
			writer.close();
		} catch(IOException e) {
			
		}
		
	}
	public void goodbye() {
		System.out.println("goodbye...");
	}
}