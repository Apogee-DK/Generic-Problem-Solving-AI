package Othello;
import Core.Heuristics;

// Similar to the total score heuristic, we associate values to each tile
// Corner are more important
public class CornersAreKey implements Heuristics<Othello>{
	
	public int determineScore(char[][] puzzle, Othello game){
		int  myTiles, oppTiles, totalTiles;
		
		int[][] ranking_scores =  {{100, -25, 50, 30, 30, 50, -25, 100},
									{-25, -50, -30, 5, 5, -30, -50, -25},
									{50, -30, 10, 10, 10, 10, -30, 50},
									{30, 5, 10, -5, -5, 10, 5, 30},
									{30, 5, 10, -5, -5, 10, 5, 30},
									{50, -30, 10, 10, 10, 10, -30, 50},
									{-25, -50, -30, 5, 5, -30, -50, -25},
									{100, -25, 50, 30, 30, 50, -25, 100}};
		
		char color = game.currentPlayer.getColor() ;
		
		// get the opponents color
		char opponents_color = 'W';
		if (color == 'W')
			opponents_color = 'B';
		
		int tileScore = 0;
		for(int i = 0; i < puzzle.length; ++i){
			for(int j = 0; j < puzzle[i].length; ++j){
				if (puzzle[i][j] == color){ // if the color of the tile is the current player's, add it to their score
					tileScore += ranking_scores[i][j];
				} // else if the color of the tile is the opponent player's, subtract it from their score
				else if (puzzle[i][j] == opponents_color){
					tileScore -= ranking_scores[i][j];
				}
			}		
		}
			
		myTiles = 0;
		oppTiles = 0;
		if(puzzle[0][0] == color) 
			myTiles++;
		else if(puzzle[0][0] == opponents_color) 
			oppTiles++;
		if(puzzle[0][7] == color) 
			myTiles++;
		else if(puzzle[0][7] == opponents_color) 
			oppTiles++;
		if(puzzle[7][0] == color) 
			myTiles++;
		else if(puzzle[7][0] == opponents_color) 
			oppTiles++;
		if(puzzle[7][7] == color) 
			myTiles++;
		else if(puzzle[7][7] == opponents_color) 
			oppTiles++;
		
		int cornerScore = 50 * (myTiles - oppTiles);
		
		myTiles = 0;
		oppTiles = 0;
		if(puzzle[0][0] == '-'){
			if(puzzle[0][1] == color) 
				myTiles++;
			else if(puzzle[0][1] == opponents_color) 
				oppTiles++;
			if(puzzle[1][1] == color) 
				myTiles++;
			else if(puzzle[1][1] == opponents_color) 
				oppTiles++;
			if(puzzle[1][0] == color) 
				myTiles++;
			else if(puzzle[1][0] == opponents_color) 
				oppTiles++;
		}
		if(puzzle[0][7] == '-'){
			if(puzzle[0][6] == color) 
				myTiles++;
			else if(puzzle[0][6] == opponents_color) 
				oppTiles++;
			if(puzzle[1][6] == color) 
				myTiles++;
			else if(puzzle[1][6] == opponents_color) 
				oppTiles++;
			if(puzzle[1][7] == color) 
				myTiles++;
			else if(puzzle[1][7] == opponents_color) 
				oppTiles++;
		}
		if(puzzle[7][0] == '-'){
			if(puzzle[7][1] == color) 
				myTiles++;
			else if(puzzle[7][1] == opponents_color) 
				oppTiles++;
			if(puzzle[6][1] == color) 
				myTiles++;
			else if(puzzle[6][1] == opponents_color) 
				oppTiles++;
			if(puzzle[6][0] == color) 
				myTiles++;
			else if(puzzle[6][0] == opponents_color) 
				oppTiles++;
		}
		if(puzzle[7][7] == '-'){
			if(puzzle[6][6] == color) 
				myTiles++;
			else if(puzzle[6][7] == opponents_color) 
				oppTiles++;
			if(puzzle[6][6] == color) 
				myTiles++;
			else if(puzzle[6][6] == opponents_color) 
				oppTiles++;
			if(puzzle[7][6] == color) 
				myTiles++;
			else if(puzzle[7][6] == opponents_color) 
				oppTiles++;
		}
		int cornerClosenessScore = -25 * (myTiles - oppTiles);
		
		return 20 * tileScore + 50 * cornerScore + 30 * cornerClosenessScore;
	}
}
