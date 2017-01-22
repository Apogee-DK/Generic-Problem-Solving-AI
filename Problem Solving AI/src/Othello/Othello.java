package Othello;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import Core.Heuristics;
import Core.Problem;

public class Othello implements Problem {
	private char [][] _puzzle; // The state of the board
	private Othello _parent; // The previous state of the game
	
	private Heuristics<Othello> _heuristic; // Heuristic being used
	
	private char color;	
	private int costValue;
	private boolean minimax;
	
	int heuristicValue;	
	Vector2 solution; // Vector which holds the position of the chip to be placed
	Player [] players; // Holds all the players
	Player currentPlayer; // Player used to calculate heuristic
	HashMap<Integer, Vector2> adjacentTilesToChips; // All empty tiles surround the chips
	ArrayList<Vector2> whitePotentialMoves; // Possible moves of the white player
	ArrayList<Vector2> blackPotentialMoves; // Possible moves of the black player

	// Similar to the above, set a temporary list used for the calculation of modified states
	HashMap<Integer, Vector2> adjacentTilesToChipsAI; 
	ArrayList<Vector2> whitePotentialMovesAI;
	ArrayList<Vector2> blackPotentialMovesAI;
	
	public Othello(){}
	
	// Create a game state with the players and set if minimax should be used
	public Othello(Player one, Player two, boolean minimax){
		this.minimax = minimax;		
		
		_puzzle = new char[8][8];
		players = new Player[2];
		
		players[0] = one;
		players[1] = two;
		
		// Create an empty board
		for(int i = 0; i < _puzzle.length; ++i){
			for(int j = 0; j < _puzzle[i].length; ++j){
				_puzzle[i][j] = '-';
			}			
		}
		
		// Create empty lists
		whitePotentialMoves = new ArrayList<Vector2>();
		blackPotentialMoves = new ArrayList<Vector2>();
		adjacentTilesToChips = new HashMap<Integer, Vector2>();	
		
		whitePotentialMovesAI = new ArrayList<Vector2>();
		blackPotentialMovesAI = new ArrayList<Vector2>();
		adjacentTilesToChipsAI = new HashMap<Integer, Vector2>();	
	}
	
	// Making a copy of a modified state
	private Othello(Player p, Player [] players, char[][] state, ArrayList<Vector2> whitePotentialMoves, ArrayList<Vector2> blackPotentialMoves, HashMap<Integer, Vector2> adjacentTilesToChips, boolean minimax){
		this.minimax = minimax;
		
		_puzzle = state;	
		this.players = new Player[2];
		this.currentPlayer = p;
		
		// clone the player
		this.players[0] = players[0].clone();
		this.players[1] = players[1].clone();
		
		this.whitePotentialMoves = new ArrayList<Vector2>();
		this.blackPotentialMoves = new ArrayList<Vector2>();
		this.adjacentTilesToChips = new HashMap<Integer, Vector2>();	
		
		// Deep copy list
		for(Vector2 vector : whitePotentialMoves){
			this.whitePotentialMoves.add(vector.clone());
		}
		
		for(Vector2 vector : blackPotentialMoves){
			this.blackPotentialMoves.add(vector.clone());
		}
		
		for(Vector2 vector : adjacentTilesToChips.values()){
			this.adjacentTilesToChips.put(vector.hashCode(), vector.clone());
		}		
			
		this.whitePotentialMovesAI = new ArrayList<Vector2>();
		this.blackPotentialMovesAI = new ArrayList<Vector2>();
		this.adjacentTilesToChipsAI = new HashMap<Integer, Vector2>();	
	}
	
	public void setPlayer(Player p){
		currentPlayer = p;
	}
	
	// Return the current state object
	public char [][] getStartState(){
		return _puzzle;		
	}

	// Return the parent/previous Problem
	public Othello getPreceedingProblem(){
		return _parent;
	}

	// There is no goal state in Othello
	public boolean compareGoalState(){
		return false;
	}

	// Set the heuristic for solving the problem
	public void setHeuristic(int hMode){
		switch(hMode){
		case 0:
			_heuristic = new MaximumReversals();
			break;
		case 1:
			_heuristic = new MaximumMobility();
			break;
		case 2:
			_heuristic = new TotalTileScore();
			break;
		case 3:
			_heuristic = new EarlyGameVSLateGameHeuristic();
			break;
		case 4:
			_heuristic = new CornersAreKey();
			break;
		default:
			_heuristic = null;
			break;			
		}
	}
	
	// set the color of the chip to be placed
	public void setColor(char color){
		this.color = color;
	}
	
	// function to reverse the color of the chip
	public void setToOpposingColor(){		
		if(color == 'W'){
			color = 'B';
		}
		else if(color == 'B'){
			color = 'W';
		}
	}
	
	public char getColor(){
		return color;
	}
	
	// Return a list of new problems after modification
	public Othello[] modifyState(int hMode){
		ArrayList<Vector2> listOfMoves;
		
		// Get the list of moves of the current player
		if(color == 'W'){
			listOfMoves = whitePotentialMoves;
		}
		else{
			listOfMoves = blackPotentialMoves;
		}
		
		// array which will hold all the possible moves
		Othello [] listOfProblemStates = new Othello [listOfMoves.size()];		
		
		// for each move, find the next possible state
		for(int i = 0; i < listOfMoves.size(); ++i){
			char [][] newBoardState = new char[_puzzle.length][_puzzle.length];
			
			// Deep copy
			for(int j = 0; j < newBoardState.length; j++){
				for(int k = 0; k < newBoardState[j].length; k++)
				{
					newBoardState[j][k] = _puzzle[j][k];
				}			
			}		
						
			// get the vector position
			int x = listOfMoves.get(i).x;
			int y = listOfMoves.get(i).y;
			
			// change the state of the copied board
			newBoardState[x][y] = color;
			
			// clear previous moves that were calculateed
			whitePotentialMovesAI.clear();
			blackPotentialMovesAI.clear();
			adjacentTilesToChipsAI.clear();
			
			// Deep copy list
			for(Vector2 vector : whitePotentialMoves){
				whitePotentialMovesAI.add(vector.clone());
			}
			
			for(Vector2 vector : blackPotentialMoves){
				blackPotentialMovesAI.add(vector.clone());
			}
			
			for(Vector2 vector : adjacentTilesToChips.values()){
				adjacentTilesToChipsAI.put(vector.hashCode(), vector.clone());
			}
			
			// flip the chips of the copied board
			flipChipsOnBoard(x, y, color, newBoardState);
			
			// update the adjacent tile list
			updateAdjacentTileList(x, y, newBoardState, adjacentTilesToChipsAI);
			
			// find the acceptable moves for each player
			findAcceptableMovesForPlayers(newBoardState, whitePotentialMovesAI, blackPotentialMovesAI, adjacentTilesToChipsAI);
			
			// call the copy constructor and instantiate a new Othello object
			Othello newPuzzle = new Othello(currentPlayer, players, newBoardState, whitePotentialMovesAI,  blackPotentialMovesAI, adjacentTilesToChipsAI, minimax);
			
			// Settings
			newPuzzle.setColor(color);
			newPuzzle.solution = new Vector2 (x, y);			
			newPuzzle.setHeuristic(hMode);
			// if it is not minimax, find the heuristic value right away
			if(minimax){
				newPuzzle.setHeuristicValue();
			}
			newPuzzle._parent = this;

			// Add the new game state to the array			
			listOfProblemStates[i] = newPuzzle;
		}
		
		// return the array of game states
		return listOfProblemStates;
	}

	// Determine the heuristic value of the current state of the problem
	public void setHeuristicValue(){
		if(_heuristic != null){
			heuristicValue = _heuristic.determineScore(_puzzle, this);
		}
	}

	// Return the heuristic value of the current state of the problem
	public int getHeuristicValue(){
		return heuristicValue;
	}

	// Add a value to the heuristic value of the current state of the problem
	public void addToHeuristicValue(int i){
		heuristicValue += i;
	}

	// Return the cost to reach the current state
	public int getCostValue(){
		return costValue;
	}

	// Display the puzzle in a readable format
	public void printPuzzle(){
		System.out.print("  ");
		
		for(int i = 0; i < 8; ++i){
			System.out.print(i + " ");
		}
		
		System.out.println();
		
		for(int i = 0; i < _puzzle.length; i++){
			System.out.print(i + " ");
			for(int j = 0; j < _puzzle[i].length; j++)
			{
				System.out.print(_puzzle[i][j] + " ");
			}			
			System.out.println();	
		}
	}
	
	// check if the othello state is a winning state
	public boolean isWinner(Othello state){
		int blackChips = 0;
		int whiteChips = 0;
		
		for(int l = 0; l < state._puzzle.length; ++l){
			for(int j = 0; j < state._puzzle.length; ++j){
				if(state._puzzle[l][j] == 'B'){
					blackChips++;
				}
				
				if(state._puzzle[l][j] == 'W'){
					whiteChips++;
				}
			}		
		}
		
		if(state.getColor() == 'W'){
			if((blackChips == 0 || blackPotentialMoves.size() == 0) && blackChips > whiteChips){
				if(players[0].getColor() == 'W'){
					players[0].setWinner();
				}
				else{
					players[1].setWinner();
				}
				return true;
									
			}				
		}
		else if(state.getColor() == 'B'){
			if((whiteChips == 0 || whitePotentialMoves.size() == 0) && whiteChips > blackChips){
				if(players[0].getColor() == 'B'){
					players[0].setWinner();
				}
				else{
					players[1].setWinner();
				}
				return true;
			}		
		}
		
		return false;
	}
	
	// update the puzzle with the placement of the new chip
	void updatePuzzle(int x, int y, char t){
		_puzzle[x][y] = t;
	}
	
	// update the empty tile list which surrounds the player chips
	void updateAdjacentTileList(int x, int y, char [][] _puzzle, HashMap<Integer, Vector2> adjacentTilesToChips){
		int [] increment = {-1, 0, 1};	

		// go through the 8 tiles surrounding the chip
		for(int i = 0; i < 3; ++i){
			if(x + increment[i] < 0 || x + increment[i] == _puzzle.length){
				continue;
			}

			for(int j = 0; j < 3; ++j){				
				if(i == 1 && j== 1 || y + increment[j] < 0 || y + increment[j] == _puzzle[i].length){
					continue;
				}				

				if(_puzzle[x + increment[i]][y + increment[j]] == '-'){
					// the tile is empty, create a vector with the new coordinates
					Vector2 temp = new Vector2(x + increment[i], y + increment[j]);

					// find existing vector in adjacent tile list
					Vector2 exist = adjacentTilesToChips.get(temp.hashCode());

					if(exist == null){
						// vector does not exist, add chip position to empty tile's vector
						temp.addAdjacentVector(new Vector2(x, y));

						//store the new vector in the adjacent list
						adjacentTilesToChips.put(temp.hashCode(), temp);	
					}
					else{
						// vector exists in the list, add the chip's position to the existing vector
						exist.addAdjacentVector(new Vector2(x, y));
						adjacentTilesToChips.put(exist.hashCode(), exist);
					}					
				}

			}			
		}

		// remove the placed tile position from the empty tile list
		adjacentTilesToChips.remove(new Vector2(x, y).hashCode());	
	}

	// update the list of moves for each player
	void findAcceptableMovesForPlayers(char[][] _puzzle, ArrayList<Vector2> whitePotentialMoves, ArrayList<Vector2> blackPotentialMoves, HashMap<Integer, Vector2> adjacentTilesToChips){

		whitePotentialMoves.clear();
		blackPotentialMoves.clear();	

		// Find all possible moves for each player
		for(Vector2 vector : adjacentTilesToChips.values()){
			assignPotentialMoveToPlayer(_puzzle, vector, whitePotentialMoves, blackPotentialMoves);
		}	
	}

	// assign a possible move to each player
	void assignPotentialMoveToPlayer(char[][] _puzzle, Vector2 vector, ArrayList<Vector2> whitePotentialMoves, ArrayList<Vector2> blackPotentialMoves){
		int deltaX = 0, deltaY = 0, nextX = 0, nextY = 0;

		// for each adjacent vector, check if the position of the chip flanking the starting chip is the same color
		for(Vector2 adjVector: vector.adjacentVectors.values()){			
			// vector.adjacentVectors is a list of vectors positions on the board which contains chips
			
			deltaX = adjVector.x - vector.x;
			deltaY = adjVector.y - vector.y;

			nextX = vector.x + deltaX;
			nextY = vector.y + deltaY; 

			char colorEdgeChip = _puzzle[nextX][nextY]; // find the color of the chip at the current position
			nextX += deltaX;
			nextY += deltaY;		

			// Ensure that it is within the boundaries
			while(nextX >= 0 && nextX < _puzzle.length && nextY >= 0 && nextY < _puzzle[nextX].length){

				if(_puzzle[nextX][nextY] == '-'){
					// Chip cannot be placed there
					break;
				}
				else if(_puzzle[nextX][nextY] != colorEdgeChip){
					// chip is different than the chip we started with...
					
					// Add to the potential moves of the correct player
					if(colorEdgeChip == 'W'){					
						blackPotentialMoves.add(vector);						
					}
					else{
						whitePotentialMoves.add(vector);
					}
					break;
				}

				nextX += deltaX;
				nextY += deltaY;
			}			
		}		
	}

	// function to flip the chips
	void flipChipsOnBoard(int x, int y, char t, char [][] _puzzle){
		Stack<Vector2> chipsToBeFlipped = new Stack<Vector2>();
		int [] increment = {-1, 0, 1};	

		// go through the 8 tiles surrounding the chip
		for(int i = 0; i < 3; ++i){
			if(x + increment[i] < 0 || x + increment[i] == _puzzle.length){
				continue;
			}

			for(int j = 0; j < 3; ++j){				
				if(i == 1 && j== 1 || y + increment[j] < 0 || y + increment[j] == _puzzle[i].length){
					continue;
				}

				if(_puzzle[x + increment[i]][y + increment[j]] == '-'){
					continue;
				}

				flipLine(x, y, increment[i], increment[j], t, chipsToBeFlipped, _puzzle);
			}			
		}	

		// change color of all chips in the stack
		for(Vector2 vector : chipsToBeFlipped){
			_puzzle[vector.x][vector.y] = t;
		}
	}

	// flip the line of chips
	void flipLine(int x, int y, int deltaX, int deltaY, char chipColor, Stack<Vector2> tilesToFlip, char[][] _puzzle){		

		int count = 0; // keep track of how many tiles were added to the stack
		int nextX = x + deltaX;
		int nextY = y + deltaY; 

		// Ensure that it is within the boundaries
		while(nextX >= 0 && nextX < _puzzle.length && nextY >= 0 && nextY < _puzzle[nextX].length){
			if(_puzzle[nextX][nextY] == '-'){
				break;
			}
			else if(_puzzle[nextX][nextY] != chipColor){
				// add the previous tile to the stack
				tilesToFlip.push(new Vector2(nextX, nextY));
				++count;
			}
			else if(_puzzle[nextX][nextY] == chipColor){
				count = 0;
				break;				
			}

			nextX += deltaX;
			nextY += deltaY;
		}

		while(count != 0){
			tilesToFlip.pop();
			--count;			
		}
	}

	// get the number of tiles owned by each player
	void getTilesOwned(Player p1, Player p2){
		char col1 = p1.getColor();
		char col2 = p2.getColor();
		
		p1.resetPlayerStatus();
		p2.resetPlayerStatus();
		
		for(int i = 0; i < _puzzle.length; ++i){
			for(int j = 0; j < _puzzle.length; ++j){
				if(_puzzle[i][j] == col1){
					p1.increaseTiles();
				}
				else if(_puzzle[i][j] == col2){
					p2.increaseTiles();
				}
			}			
		}		
		
	}
	
	void emptyBoard(){
		for(int i = 0; i < _puzzle.length; ++i){
			for(int j = 0; j < _puzzle.length; ++j){
				_puzzle[i][j] = '-';			
			}			
		}		
	}
}
