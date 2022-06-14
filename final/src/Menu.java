import java.io.*;
import java.util.Scanner;

public class Menu {
	
	static String playerName = "";

	class playerScore{ //I will make a map array out of this
		String name = "empty";
		int score = 0;
	}
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	public static void main(String[]args) throws IOException {
		System.out.println("\n-----Finding Timo-----\n");
		Menu m = new Menu();
		m.displayIntro();
	}
	
	public boolean continuePlaying()throws IOException{
		final String CONTINUE = "yes";
		final String END = "no";

		String answer = br.readLine().toLowerCase();

		if(answer.equals(CONTINUE)){
			return true;
		}
		else if (answer.equals(END)){
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
		Scanner fileReader = new Scanner(board);
		
		//go back to the previous state where i count the lines then i go add into an array
		
		
		int numOfSubmissions = 0;
		while(fileReader.hasNext()) {
			fileReader.next();
			numOfSubmissions++;
		}
		fileReader.close();
		
		//reset
		System.out.println(numOfSubmissions);
		fileReader = new Scanner(board);
		
		playerScore [] extracted = new playerScore[numOfSubmissions];

		for(int i = 0; i < numOfSubmissions; i++){
			System.out.println(i);
			extracted[i] = new playerScore();
			
			String nameFromFile = fileReader.next();
			int scoreFromFile = Integer.parseInt(fileReader.next());
			
			extracted[i].name = nameFromFile;
			extracted[i].score = scoreFromFile;
		}
		

		fileReader.close();
		extracted = insertionSort(extracted, numOfSubmissions);
		return extracted;
		
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
	
	
	public void endingScreen(int score) {
		SetUpControls.closeWindow();
		System.out.println("You win!");
		addScore(score);
		displayBoard();
	}

	public void displayIntro() throws IOException {
		FindingTimo fT = new FindingTimo();
		System.out.println("This is timo...");
		System.out.println("\n    oo/><><>\\\n   ( -)><><><>\n    L|_|L|_|`) ");
		System.out.println("\n        ...he is lost\n");
		
		System.out.print("Would you like to find him? (Yes|No): ");
		if(continuePlaying()) {
			playerName = nameInput();
			System.out.println("What an excellent name " + playerName);
			fT.run();
		} else {
			goodbye();
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