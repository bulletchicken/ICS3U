import java.io.*;

public class FindingTimo {

	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	public static void main(String[]args) {
		FindingTimo fT = new FindingTimo();
		final int mlength = 15; //vertical
		final int mwidth = 15; //horizontal
		final int height = 3; //up down

		String [][][] map = new String[mlength][mwidth][height];
		fT.run(map);
		System.out.println("Make sure not in windowed mode");
		

	}

	public void displayGrid(String [][][] map, int floor) {
		
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[0].length; j++) {
				System.out.print(map[i][j][floor] + " ");
			}
			System.out.println();
		}
	}
	
	public void update(Character timo, String[][][] map) {
		map[timo.xCord][timo.yCord][timo.zCord] = "t";
	}

	public void run(String[][][]map) {
		
		Character timo = new Character();	
		//starting cords
		timo.xCord = (int)(Math.random()*15);
		timo.yCord = (int)(Math.random()*15);
		timo.zCord = (int)(Math.random()*2);
		
		System.out.println(timo.xCord);
		
		
		SetUpControls.setControls();
		displayIntro();
		
		
		Character player = new Character();
	//starting cords in the middleQ  qbv
		player.xCord = 8;
		player.yCord = 8;
		player.zCord = 0;
		
		//setup the map and resets it on every run
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[0].length; j++) {
				for(int k = 0; k < map[0][0].length; k++) {
					map[i][j][k] = "-";
				}
			}
		}
		
		int floor = player.zCord;
		//use cues like you hear footsteps above or below you
		update(timo, map);
		displayGrid(map, floor);
	}

	public void displayIntro() {
		System.out.println("Game : type 1 to begin");
	}

	public String nameInput()throws IOException {
		return br.readLine();
	}
}