package Othello;
import Core.Heuristics;

public class MaximumReversals implements Heuristics<Othello>{	
	// getting number of chips from the opposing player
	public int determineScore(char[][] puzzle, Othello game){
		char currentColor = 'W';
		
		// Get the opposing color
		if(game.currentPlayer.getColor() == 'B'){
			currentColor = 'B';
		}
		
		int numOfChips = 0;	
		
		for(int i = 0; i < puzzle.length; ++i){
			for(int j = 0; j < puzzle[i].length; ++j){
				if(puzzle[i][j] == currentColor){
					numOfChips++;
				}
			}		
		}

		// return the number of the player's chip on the board
		// We want the max reversals, the move that has the biggest number of chips will be the best
		return numOfChips; 
	}
}
