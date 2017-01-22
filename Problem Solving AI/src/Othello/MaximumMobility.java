package Othello;

import Core.Heuristics;
// Heuristic which gets the rate of mobility at each turn vs the mobility of its opponent
public class MaximumMobility implements Heuristics<Othello>{	
	// score based on the maximum number of potential moves for each player
	public int determineScore(char[][] puzzle, Othello game){
		int sizeW = game.whitePotentialMoves.size();
		int sizeB = game.blackPotentialMoves.size();
		
		if(sizeW + sizeB == 0){
			return  0;
		}
		
		int mobilityRate = 100 * (sizeW - sizeB) / (sizeW + sizeB);
		
		if(game.currentPlayer.getColor() == 'B'){
			mobilityRate = 100 * (sizeB - sizeW) / (sizeW + sizeB);
		}

		return mobilityRate; 
	}
}
