package EightPuzzle;
import Core.Heuristics;
import Core.Tile;

// Admissible
public class ManhattanDistance implements Heuristics<Tile []>{
	
	// Sum of distances of a tile from its goal state position
	
	public int determineScore(char[][] puzzle, Tile[] tiles){
		int distance = 0;		
		
		for(int i = 0; i < puzzle.length; i++){
			for(int j = 0; j < puzzle[i].length; j++)
			{
				int temp = -1;
				if(puzzle[i][j] == 'B'){
					temp = 0;
				}
				else{
					temp = Character.getNumericValue(puzzle[i][j]);
				}		
				
				// The tile array has the position of all the tiles in the correct position
				if(tiles[temp].row != i || tiles[temp].column != j){
					// subtract the row and column index to get the distance
					distance += Math.abs(tiles[temp].row - i) + Math.abs(tiles[temp].column - j);
				}
			}			
		}	
		
		return distance;
	}
}
