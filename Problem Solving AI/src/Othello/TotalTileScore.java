package Othello;
import Core.Heuristics;

/*
 * Each tiles has a value, the score is determined by summing up the value of each black chip
 */
public class TotalTileScore implements Heuristics<Othello>{
	// Calculate total tile score of the current player
	public int determineScore(char[][] puzzle, Othello game){
		
		int[][] tileScores =  {{25, -5, 15, 8, 8, 15, -5, 25}, 
								{-5, -10, -4, 1, 1, -4, -10, -5},
								{15, -4, 2, 3, 3, 2, -4, 15},
								{8, 1, 3, -1, -1, 3, 1, 8},
								{8, 1, 3, -1, -1, 3, 1, 8},
								{15, -4, 2, 3, 3, 2, -4, 15},
								{-5, -10, -4, 1, 1, -4, -10, -5},
								{25, -5, 15, 8, 8, 15, -5, 25}};
		
		char currentPlayersColor = game.currentPlayer.getColor();
		char opponentPlayersColor = 'B';
		if (currentPlayersColor == 'B')
			opponentPlayersColor = 'W';
		
		int score = 0;
		
		// sum up the scores of the current player and subtract it with their opponents value
		for(int i = 0; i < puzzle.length; ++i){
			for(int j = 0; j < puzzle[i].length; ++j){
				if (puzzle[i][j] == currentPlayersColor){
					score += tileScores[i][j];
				}
				else if (puzzle[i][j] == opponentPlayersColor){
					score -= tileScores[i][j];
				}
			}		
		}
		
		return score;
	}
}
