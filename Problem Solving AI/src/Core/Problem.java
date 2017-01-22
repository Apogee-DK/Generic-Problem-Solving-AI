package Core;
/*
 * Problem interface which can be used to create other classes which can be used by the State space object
 * in order to use the search methods and heuristics 
 */

public interface Problem{

	// Return the current state object
	public Object getStartState();	
	
	// Return the parent/previous Problem
	public Problem getPreceedingProblem();	
	
	// Compare current state with the goal state
	public boolean compareGoalState();
	
	// Set the heuristic for solving the problem
	public void setHeuristic(int hMode);	
	
	// Return a list of new problems after modification
	public Problem[] modifyState(int hMode);
	
	// Determine the heuristic value of the current state of the problem
	public void setHeuristicValue();
	
	// Return the heuristic value of the current state of the problem
	public int getHeuristicValue();	
	
	// Add a value to the heuristic value of the current state of the problem
	public void addToHeuristicValue(int i);
	
	// Return the cost to reach the current state
	public int getCostValue();
	
	// Display the puzzle in a readable format
	public void printPuzzle();
	
}