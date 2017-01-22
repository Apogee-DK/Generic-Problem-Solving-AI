package EightPuzzle;
import Core.Heuristics;
import Core.Tile;

// Admissible
public class MinManhattanMisplaced implements Heuristics<Tile []>{
	// Minimum number between Misplaced Tiles and Manhattan Distance
	
	public int determineScore(char[][] puzzle, Tile[] tiles){		
		int countManhattan = 0;
		int countMisplaced = 0;
		
		for(int i = 0; i < puzzle.length; i++){
			for(int j = 0; j < puzzle[i].length; j++)
			{
				int temp = -1;
				if(puzzle[i][j] == 'B'){
					continue;
				}
				else{
					temp = Character.getNumericValue(puzzle[i][j]);
				}		
				
				// The tile array has the position of all the tiles in the correct position
				if(tiles[temp].row != i || tiles[temp].column != j){
					countManhattan += Math.abs(tiles[temp].row - i) + Math.abs(tiles[temp].column - j);
					countMisplaced ++;
				}
			}			
		}	
		
		return Math.min(countManhattan, countMisplaced); // return the minimum between these two values --> inconsistent heuristic		
	}	
}
