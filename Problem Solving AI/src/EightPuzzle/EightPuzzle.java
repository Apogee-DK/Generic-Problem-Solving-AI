package EightPuzzle;
import java.lang.Math;

import Core.Heuristics;
import Core.Problem;
import Core.Tile;

/*
 * EightPuzzle class holds uses the Problem interface to provide details about its problem
 * It holds a current and goal state which the program can use to determine whether it reached the goal state
 * It also features functions which associates a heuristic value to its state or expands to other states.
 * In this case, it expands by moving the blank space up, down, left, or right * 
 */


public class EightPuzzle implements Problem{
	private char [][] _puzzle;
	private EightPuzzle _parent;
	private Tile [] _goal;
	private Heuristics<Tile []> _heuristic;	
	
	private int heuristicValue;	
	private int costValue;
	
	private int rowBlank;
	private int columnBlank;
	private int prevRowBlank = -1;
	private int prevColumnBlank = -1;


	public EightPuzzle(char [] initial, char[] goal, int numOfValues){

		int size = (int)Math.sqrt(numOfValues);

		_puzzle = new char[size][size];
		
		// an array of tiles which has a row, column index and the character it holds
		// Allows an easier comparison when we use heuristics
		_goal = new Tile[numOfValues];

		for(int i = 0; i < _puzzle.length; i++){
			for(int j = 0; j < _puzzle[i].length; j++)
			{
				_puzzle[i][j] = initial[i*size + j];
				if(_puzzle[i][j] == 'B'){
					rowBlank = i;
					columnBlank = j;
					continue;
				}
			}			
		}

		// initializing the Tile array
		// Tile array will hold all the tiles with its numerical value and its position (goal state) on the board
		for(int i = 0; i < goal.length; i++){
			int temp = -1;

			if(goal[i] == 'B'){
				temp = 0;
			}
			else{
				temp = Character.getNumericValue(goal[i]);
			}			
			
			_goal[temp] = new Tile(goal[i], i / size, i % size);	
		}		
		
		// distance value
		costValue = 0;
	}

	// initialize a EightPuzzle with the the following attributes
	public EightPuzzle(char [][] initial, Tile[] goal, Heuristics<Tile []> h, int rowBlank, int columnBlank, int prevRowBlank, int prevColumnBlank){		
		_puzzle = initial;		
		_goal = goal;
		_heuristic = h;

		this.rowBlank = rowBlank;
		this.columnBlank = columnBlank;
		this.prevRowBlank = prevRowBlank;
		this.prevColumnBlank = prevColumnBlank;
	}
	
	// returns the 2D array which holds the state of the puzzle
	public char[][] getStartState(){
		return _puzzle;		
	}
	
	// returns the parent puzzle
	public EightPuzzle getPreceedingProblem(){
		return _parent;
	}

	// checks the current state to determine whether it reached the goal state
	public boolean compareGoalState(){
		for(int i = 0; i < _puzzle.length; i++){
			for(int j = 0; j < _puzzle[i].length; j++)
			{
				int temp = -1;
				if(_puzzle[i][j] == 'B'){
					temp = 0;
				}
				else{
					// Convert the character to a numerical value
					temp = Character.getNumericValue(_puzzle[i][j]);
				}		

				// The tile array has the position of all the tiles in the correct position
				if(_goal[temp].row != i || _goal[temp].column != j){
					return false;
				}
			}			
		}		
		return true;
	}

	public void setHeuristic(int hMode){		
		// use the user's input to determine the heuristic mode
		switch(hMode){
		case 0:
			_heuristic = new ManhattanDistance();
			break;
		case 1:
			_heuristic = new MisplaceTiles();
			break;
		case 2:
			_heuristic = new MinManhattanMisplaced();
			break;
		// Admissible heuristic	
		case 3:
			_heuristic = new MisplacedRowAndColumn();
			break;			
		// Inadmissible heuristic	
		case 4:
			_heuristic = new HashingCode();
			break;

		default:
			_heuristic = null;
			break;			
		}		
	}

	// uses heuristic function to assign a value to the state
	public void setHeuristicValue(){
		if(_heuristic != null){
			heuristicValue = _heuristic.determineScore(_puzzle, _goal);
		}						
	}
	
	// print the current state's puzzle
	public void printPuzzle(){
		for(int i = 0; i < _puzzle.length; i++){
			for(int j = 0; j < _puzzle[i].length; j++)
			{
				System.out.print(_puzzle[i][j] + " ");
			}			
			System.out.println();
		}
	}
	
	// Return a list of puzzle states
	public EightPuzzle[] modifyState(int heuristicMode){

		EightPuzzle[] listOfPuzzles = new EightPuzzle[4];

		// Loop through all the possible movements of the blank space - UP, DOWN, LEFT, RIGHT		
		for(int i = 0; i < listOfPuzzles.length; i++){
			
			// Instantiate a new 2D array and deep Copy the contents of the initial puzzle state into the newly created array
			char [][] _newPuzzleState = new char[_puzzle.length][_puzzle.length];
			
			for(int j = 0; j < _newPuzzleState.length; j++){
				for(int k = 0; k < _newPuzzleState[j].length; k++)
				{
					_newPuzzleState[j][k] = _puzzle[j][k];
				}			
			}			

			// set the current blank row and column to a negative number
			int _currentRowBlank = -1;
			int _currentColumnBlank = -1;

						
			// Move the blank space 
			// 1) Ensure it is within the bounds
			// 2) Ensure that the move does not result in the initial puzzle state
			
			// Move the blank space up			
			if(i == 0 && rowBlank > 0 && rowBlank - 1 != prevRowBlank){
				
				char temp = _newPuzzleState[rowBlank-1][columnBlank];
				_newPuzzleState[rowBlank-1][columnBlank] = 'B';
				_newPuzzleState[rowBlank][columnBlank] = temp;

				_currentRowBlank = rowBlank - 1;
				_currentColumnBlank = columnBlank;
			}

			// Move the blank space down
			else if(i == 1 && rowBlank < _puzzle.length - 1 && rowBlank + 1 != prevRowBlank){
				char temp = _newPuzzleState[rowBlank+1][columnBlank];
				_newPuzzleState[rowBlank+1][columnBlank] = 'B';
				_newPuzzleState[rowBlank][columnBlank] = temp;

				_currentRowBlank = rowBlank + 1;
				_currentColumnBlank = columnBlank;		
			}

			// Move the blank space to the left
			else if(i == 2 && columnBlank > 0 && columnBlank - 1 != prevColumnBlank){
				char temp = _newPuzzleState[rowBlank][columnBlank - 1];
				_newPuzzleState[rowBlank][columnBlank - 1] = 'B';
				_newPuzzleState[rowBlank][columnBlank] = temp;

				_currentRowBlank = rowBlank;
				_currentColumnBlank = columnBlank - 1;				
			}

			// Move the blank space to the right
			else if(i == 3 && columnBlank < _puzzle.length - 1 && columnBlank + 1 != prevColumnBlank){
				char temp = _newPuzzleState[rowBlank][columnBlank + 1];
				_newPuzzleState[rowBlank][columnBlank + 1] = 'B';
				_newPuzzleState[rowBlank][columnBlank] = temp;

				_currentRowBlank = rowBlank;
				_currentColumnBlank = columnBlank + 1;			
			}	

			// check if the new blank row and column is not -1
			if(_currentRowBlank != -1 && _currentColumnBlank != -1){				
				// Create the new problem and set its values
				EightPuzzle newPuzzle = new EightPuzzle(_newPuzzleState, _goal, _heuristic, _currentRowBlank, _currentColumnBlank, rowBlank, columnBlank);
				newPuzzle.setHeuristic(heuristicMode);
				newPuzzle.setHeuristicValue();
				newPuzzle._parent = this;
				newPuzzle.costValue = this.costValue + 1;

				// add new puzzle state to list
				listOfPuzzles[i] = newPuzzle;			
			}
			else{
				// the move was not possible
				listOfPuzzles[i] = null;				
			}
		}

		return listOfPuzzles;
	}
	
	public int getHeuristicValue(){
		return heuristicValue;		
	}
	
	public void addToHeuristicValue(int i){
		heuristicValue += i;		
	}
	
	// return the cost to reach the current state, in this case it is the depth level
	public int getCostValue(){
		return costValue;
	}

	// Determine whether two arrays are equal by comparing both arrays - similar to deep equals
	@Override
	public boolean equals(Object obj){
		if(this == obj)
			return true;
		if((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		
		EightPuzzle puzzle = (EightPuzzle) obj;
		
		for(int i = 0; i < puzzle._puzzle.length; i++){
			for(int j = 0; j < puzzle._puzzle[i].length; j++){
				if(puzzle._puzzle[i][j] != this._puzzle[i][j]){
					return false;
				}				
			}		
		}
		
		return true;		
	}
	
	// Return the hash of the array for use in our state space's hash set (closedList)
	@Override
	public int hashCode() {
	    int hash = 0;
	    for (int i = 0; i < _puzzle.length; i++){
	        for (int j = 0; j < _puzzle[i].length; j++){
	            hash = (hash) ^ (int)((Math.pow(173,i) + Math.pow(307,j)) * _puzzle[i][j]);
	        }
	    }
	    return hash;
	}
}
