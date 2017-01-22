package EightPuzzle;
import Core.Heuristics;
import Core.Tile;

// Admissible
public class MisplacedRowAndColumn implements Heuristics<Tile []>{
	
	// Admissible heuristic which counts the number of misplaced tile in each row and column	
	
	public int determineScore(char[][] puzzle, Tile[] tiles){
		
		int numOfTilesMisplacedInRow = 0;
		int numOfTilesMisplacedInColumn = 0;
		
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
				
				if(tiles[temp].row != i){
					// tile not in the correct row
					numOfTilesMisplacedInRow++;
				}
				
				if(tiles[temp].column != j){
					// tile not in the correct column
					numOfTilesMisplacedInColumn++;
				}		
			}
		}
		
		return numOfTilesMisplacedInRow + numOfTilesMisplacedInColumn; // add both values together
	}
}
