package Othello;
import Core.Heuristics;

// Heuristics which uses a different way of calculating scores once the board reaches a certain state
public class EarlyGameVSLateGameHeuristic implements Heuristics<Othello>{
	
	public int determineScore(char[][] puzzle, Othello game){
		
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
		char opponentsColor = 'W';
		if (color == 'W')
			opponentsColor = 'B';
		
		int tileScore = 0;
		int myTiles = 0;
		int oppTiles = 0;
		int totalTiles = 0;
		for(int i = 0; i < puzzle.length; ++i){
			for(int j = 0; j < puzzle[i].length; ++j){
				if (puzzle[i][j] == color){ // if the color of the tile is the current player's, add it to their score
					tileScore += ranking_scores[i][j];
					totalTiles++;
					myTiles++;
				} // else if the color of the tile is the opponent player's, subtract it from their score
				else if (puzzle[i][j] == opponentsColor){
					tileScore -= ranking_scores[i][j];
					totalTiles++;
					oppTiles++;
				}
			}		
		}
		
		int countScore;
		if (totalTiles < 35) { // early game stage
			countScore = oppTiles - myTiles;
		}
		else { // end game stage
			countScore = myTiles - oppTiles;
		}
		
		return 60 * countScore +  40 * tileScore;
	}
}
