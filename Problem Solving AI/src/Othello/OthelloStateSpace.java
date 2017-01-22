package Othello;

import java.util.PriorityQueue;

import Core.MaxToMinProblemComparator;
import Core.MinToMaxProblemComparator;
import Core.Problem;

import java.util.Set;
import java.util.Stack;
import java.util.HashSet;
import java.util.Comparator;

/*
 * State Space class holds all the search functions required to expand a state to get its children
 * It features a generic search mode function which allows users to choose between search modes and heuristics
 * Search Mode includes:
 * - Greedy Search
 * - Minimax
 * 
 */


public class OthelloStateSpace{
	private Problem[] solutionList; // will use a hash set in order to ease duplicate checking
	private Comparator<Problem> minToMaxComparator;
	private Comparator<Problem> maxToMinComparator;
	private Othello answer;
	private static Othello value;
	private static Othello alpha = new Othello();
	private static Othello beta = new Othello();

	public OthelloStateSpace(){
		minToMaxComparator = new MinToMaxProblemComparator(); // priority queue will be sorted from min to max
		maxToMinComparator = new MaxToMinProblemComparator(); // priority queue will be sorted from max to min
	}

	// allow the user to choose a problem, a search method and a heuristic to use
	public void searchMode(Problem p, int i, int heuristicMode){
		reset();

		switch(i){
		case 1:
			greedySearch(p, heuristicMode, new PriorityQueue<Problem>(10, maxToMinComparator));
			break;
		case 2: 
			minimaxSearch(p, heuristicMode, true);
			break;
		case 3: 
			minimaxSearch(p, heuristicMode, false);
			break;

		default:
			break;
		}		
	}

	// return the solution vector
	public Vector2 getSolution(){

		if(answer == null){
			System.out.println("No moves!");
		}

		return answer.solution;
	}

	// reset the values needed for the range
	private void reset(){
		alpha.heuristicValue = Integer.MIN_VALUE;
		beta.heuristicValue = Integer.MAX_VALUE;
	}

	// best first search
	private void greedySearch(Problem puzzle, int heuristicMode, PriorityQueue<Problem> queue){		

		Problem [] states = puzzle.modifyState(heuristicMode);
		for(Problem problem : states){
			queue.add(problem);	
		}

		answer = (Othello)queue.poll();
	}

	// Function which will be called by the AI on their turn
	private void minimaxSearch(Problem puzzle, int heuristicMode, boolean prune){
		// setting the depth
		int depth = 3;
		
		// Temporary variable which will be used to hold the state of the problem
		Othello parent;
		
		// User can decide if they want the AI to prune or not
		if(prune){			
			parent = miniMax(puzzle, heuristicMode, depth, alpha, beta, true);			
		}
		else{
			parent = miniMaxNoPrune(puzzle, heuristicMode, depth, true);			
		}
		
		// Create a new temporary variable
		Othello child = new Othello();

		// Go up the tree until the parent's parent is null and return its child		
		while(parent.getPreceedingProblem() != null){
			child = parent;
			parent = parent.getPreceedingProblem();			
		}

		answer = child;
	}


	private Othello miniMax(Problem puzzle, int heuristicMode, int depth, Othello alpha, Othello beta, boolean isMax){

		// Find all the possible moves
		Problem [] states = puzzle.modifyState(heuristicMode);		

		if(depth == 0 || states.length == 0)
		{
			// Reached the depth, find the heuristic value and return the problem
			puzzle.setHeuristicValue();
			return (Othello)puzzle;
		}

		Othello tempValue = new Othello();

		// Check if it is a maximizing node		
		if(isMax){
			// Assign alpha (maximum lower bound) to value
			value = alpha;
			
			// for each problem, set it to the opposing color so the right chip will be placed and call the minimax function again
			// however, this time we are minimizing that move
			for(Problem problem : states){
				((Othello)problem).setToOpposingColor();
				tempValue = miniMax(problem, heuristicMode, depth - 1, value, beta, false);
				
				// check if the value we got is bigger than the value we had under the same parent state
				if(tempValue.getHeuristicValue() > value.getHeuristicValue()){
					// it is bigger, make value equal to it
					value = tempValue;
				}

				// check if the value is bigger than the minimum upper bound
				if(value.getHeuristicValue() > beta.getHeuristicValue()){
					// it passed the limit, break and return the result
					return beta;
				}
			}
		}
		else{
			// Assign beta (minimum upper bound) to value
			value = beta;
			
			// for each problem, set the color and  call the maximizing minimax function
			for(Problem problem : states){	
				((Othello)problem).setToOpposingColor();
				tempValue = miniMax(problem, heuristicMode, depth - 1, alpha, value, true);
				
				// Check if the value we got is smaller than the value we had under the same parent state
				if(tempValue.getHeuristicValue() < value.getHeuristicValue()){
					// value is smaller, set the value to the value we determined from minimax
					value = tempValue;
				}
				// check if the value is smaller than the maximum lower bound
				if(value.getHeuristicValue() < alpha.getHeuristicValue()){
					return alpha;
				}
			}			
		}	

		// return the final state which was taken
		return value;
	}

	// function which uses a minimax tree with no pruning
	private Othello miniMaxNoPrune(Problem puzzle, int heuristicMode, int depth, boolean isMax){
		
		Problem [] states = puzzle.modifyState(heuristicMode);		
		
		if(depth == 0 || states.length == 0)
		{
			puzzle.setHeuristicValue();
			return (Othello)puzzle;
		}

		Othello tempValue = new Othello();
		
		if(isMax){
			// No range used
			for(Problem problem : states){

				((Othello)problem).setToOpposingColor();
				tempValue = miniMaxNoPrune(problem, heuristicMode, depth - 1, false);

				// compare with the value determined previously to see if it should be modified				
				if(tempValue.getHeuristicValue() > value.getHeuristicValue()){
					value = tempValue;
				}
			}
		}
		else{			
			// No range used
			for(Problem problem : states){	
				((Othello)problem).setToOpposingColor();
				tempValue = miniMaxNoPrune(problem, heuristicMode, depth - 1, true);

				// compare with the value determined previously to see if it should be modified
				if(tempValue.getHeuristicValue() < value.getHeuristicValue()){
					value = tempValue;
				}
			}			
		}
		
		return value;
	}
}
