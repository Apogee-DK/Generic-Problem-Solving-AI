package EightPuzzle;
import Core.Heuristics;
import Core.Tile;

// Inadmissible
public class HashingCode implements Heuristics<Tile []>{
	
	// An non-admissible heuristic which associates a hash code to every state
	
	public int determineScore(char[][] puzzle, Tile[] tiles){
	    int _hashState = 0;
	    int _hashGoal = 0;
	    
	    // use hash function to determine the hash of each state
	    
	    for (int i = 0; i < puzzle.length; i++){
	        for (int j = 0; j < puzzle[i].length; j++){
	            _hashState = (_hashState) ^ (int)((Math.pow(173,i) + Math.pow(307,j)) * puzzle[i][j]); // hash function, each state has its own hash value
	        }
	    }
	    
	    for (int i = 0; i < tiles.length; i++){
	        _hashGoal = (_hashGoal) ^ (int)((Math.pow(173,tiles[i].row) + Math.pow(307, tiles[i].column)) * tiles[i].tile);
	    }	    
	    	    
	    return Math.abs(_hashGoal - _hashState);
	}
}
