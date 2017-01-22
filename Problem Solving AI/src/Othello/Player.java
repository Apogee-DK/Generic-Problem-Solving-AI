package Othello;

import Core.Problem;

public class Player {
	
	OthelloStateSpace stateSpace; // each player has a state space, useful for AI only
	
	private String name;
	private char color;
	private int tilesOwned; // number of tiles controlled
	private boolean isAI; // checks if the player is an AI
	private boolean isWinner; // determines the winner
	private int aiMode; // specifies greedy search of minimax
	private int heuristicMode; // specifies the heuristic used
		
	Player(){
		tilesOwned = 0;
		isWinner = false;
	}
	
	// cloning method of a player
	public Player clone(){
		Player p = new Player();		
		
		p.name = this.name;
		p.color = this.color;
		p.tilesOwned = this.tilesOwned;
		p.isAI = this.isAI;
		p.isWinner = this.isWinner;
		p.aiMode = this.aiMode;
		p.heuristicMode = this.heuristicMode;
		
		return p; 	
	}
	
	// make the player an AI
	public void setAI(boolean b){
		isAI = b;
		stateSpace = new OthelloStateSpace();
	}
	
	// find the next states in the state space
	public void search(Problem othello){
		stateSpace.searchMode(othello, aiMode, heuristicMode);		
	}
	
	// returns the position of the chip
	public Vector2 results(){
		return stateSpace.getSolution();		
	}
	
	public boolean getAI(){
		return isAI;
	}
	
	public void setWinner(){
		isWinner = true;
	}
	
	public boolean getWinner(){
		return isWinner;		
	}
	
	// set the player's color
	public void setColor(char color){
		this.color = color;	
	}	
	
	public char getColor(){
		return color;		
	}
	
	// reset the status of the player for a rematch
	public void resetPlayerStatus(){	
		tilesOwned = 0;
		isWinner = false;
	}
	
	public void setName(String name){
		this.name = name;		
	}
	
	public String getName(){
		return name;		
	}
	
	public int getNumOfTilesOwned(){		
		return tilesOwned;
	}
	
	public void increaseTiles(){
		tilesOwned++;		
	}
	
	public void decreaseTiles(){
		tilesOwned++;		
	}
	
	public void setAIMode(int mode){
		aiMode = mode;
	}
	
	public int getAIMode(){
		return aiMode;
	}
	
	public void setHeuristicMode(int mode){
		heuristicMode = mode;
	}
	
	public int getHeuristicMode(){
		return heuristicMode;
	}
	
	// used to compare player to see if they are the same
	@Override
	public boolean equals(Object obj){
		if(this == obj)
			return true;
		if((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		
		Player player = (Player) obj;
		
		return (player.color == this.color)? true : false;		
	}
	
}
