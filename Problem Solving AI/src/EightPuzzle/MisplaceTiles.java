package EightPuzzle;
import Core.Heuristics;
import Core.Tile;

// Admissible
public class MisplaceTiles implements Heuristics<Tile []>{
	
	// Total number of misplaced tiles 
	
	public int determineScore(char[][] puzzle, Tile[] tiles){
		int count = 0;		
		
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
					// add to the count
					count++;
				}
			}			
		}	
		
		return count;
	}
}
