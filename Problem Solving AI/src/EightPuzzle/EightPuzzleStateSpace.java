package EightPuzzle;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Stack;

import Core.MinToMaxProblemComparator;
import Core.Problem;

import java.util.Set;
import java.util.HashSet;
import java.util.Comparator;

/*
 * State Space class holds all the search functions required to expand a state to get its children
 * It features a generic search mode function which allows users to choose between search modes and heuristics
 * Search Mode includes:
 * - BFS
 * - DFS
 * - Best First Search
 * - A*
 * 
 * Each search has a different way to manipulate the fringe list (open list)
 * Included the use of a closed list to prevent duplicate states in order to increase efficiency
 * 
 */

public class EightPuzzleStateSpace{
	private Set<Problem> closedList; // will use a hash set in order to ease duplicate checking
	private Stack<Problem> answer;
	private Problem[] states;
	private Comparator<Problem> comparator;		
	
	public EightPuzzleStateSpace(){
		comparator = new MinToMaxProblemComparator(); // comparator needed to insert a problem into the priority queue
		closedList = new HashSet<Problem>(); // ensures that there are no duplicate states 
		answer = new Stack<Problem>(); // use of stack to print out the path
	}

	// allow the user to choose a problem, a search method and a heuristic to use
	public void searchMode(Problem p, int i, int heuristicMode){
		reset();
		
		switch(i){
		case 0:
			breadthFirstSearch(p, new LinkedList<Problem>());
			break;
		case 1:
			depthFirstSearch(p, new Stack<Problem>());
			break;
		case 2:
			bestFirstSearch(p, heuristicMode, new PriorityQueue<Problem>(10, comparator));
			break;
		case 3: 
			aStar(p, heuristicMode, new PriorityQueue<Problem>(10, comparator));
			break;

		default:
			break;
		}		
	}

	// display the states that are currently in the answer stack
	public void showSolution(){
		int steps = 0;
		while(!answer.isEmpty()){
			System.out.println("---------------");
			if(steps == 0){
				System.out.println("Initial State");
			}
			else if(answer.size() == 1){
				System.out.println("Step " + steps + " - Goal State" );
			}
			else{
				System.out.println("Step " + steps);
			}
			System.out.println("---------------");			
			answer.pop().printPuzzle();		
			System.out.println();
			steps++;
		}
		
		System.out.println("Visited: " + closedList.size());
		answer.clear();
	}
	
	private void reset(){
		closedList.clear();		
	}


	// breadth first search - uses a Queue for its fringe list
	private void breadthFirstSearch(Problem puzzle, Queue<Problem> list){		
		list.add(puzzle);

		while(!list.isEmpty()){

			Problem currentProblem = list.poll();
			closedList.add(currentProblem); // add the state to the list of states visited

			if(currentProblem.compareGoalState()){
				System.out.println("SOLUTION - Breadth First Search");				

				while(currentProblem != null){
					answer.push(currentProblem);
					currentProblem = currentProblem.getPreceedingProblem();
				}
				
				list.clear();
				break;
			}			
			else{
				states = currentProblem.modifyState(-1); // return the list of children states depending on move
				for(Problem problem : states){
					// for each state, check if it is null
					if(problem != null){
						if(!closedList.contains(problem)){
							// add to the back of the fringe list and keep the children from before
							// this will make sure that each depth level is visited first before checking the children
							list.add(problem);
						}
					}					
				}				
			}
		}
	}

	// depth first search - uses a stack for its fringe list
	private void depthFirstSearch(Problem puzzle, Stack<Problem> list){
		if(puzzle.compareGoalState()){
			System.out.println("SOLUTION - Depth First Search");

			puzzle.printPuzzle();
			return;
		}		

		list.add(puzzle); // add current state to the stack		
		
		while(!list.isEmpty()){
			Problem currentProblem = list.pop();
			closedList.add(currentProblem);
	
				
			states = currentProblem.modifyState(-1);
			for(Problem problem : states){
				if(problem != null){
					if(!closedList.contains(problem)){
						list.push(problem);	

						if(problem.compareGoalState()){
							System.out.println("SOLUTION - Depth First Search");					

							while(problem != null){
								answer.push(problem);
								problem = problem.getPreceedingProblem();
							}
							
							list.clear();
							break;
						}
					}
				}					
			}
		}	
	}

	// best first search
	private void bestFirstSearch(Problem puzzle, int heuristicMode, PriorityQueue<Problem> queue){		
		queue.add(puzzle);
		while(!queue.isEmpty()){

			// the polling will always choose the lowest heuristic value in the queue
			Problem currentProblem = queue.poll();
			closedList.add(currentProblem);

			if(currentProblem.compareGoalState()){
				System.out.println("SOLUTION - Best First Search");				

				while(currentProblem != null){
					answer.push(currentProblem);
					currentProblem = currentProblem.getPreceedingProblem();
				}
				
				queue.clear();
				break;
			}			
			else{
				states = currentProblem.modifyState(heuristicMode);
				for(Problem problem : states){
					if(problem != null){
						if(!closedList.contains(problem)){
							queue.add(problem);												
						}		
					}
				}
			}			
		}
	}

	// A*
	// considers the cost and heuristic
	private void aStar(Problem puzzle, int heuristicMode, PriorityQueue<Problem> queue){
		queue.add(puzzle);

		while(!queue.isEmpty()){

			// the polling will always choose the lowest heuristic value in the 
			Problem currentProblem = queue.poll();
			closedList.add(currentProblem);

			if(currentProblem.compareGoalState()){
				System.out.println("SOLUTION - A*");				

				while(currentProblem != null){
					answer.push(currentProblem);
					currentProblem = currentProblem.getPreceedingProblem();
				}
				
				queue.clear();
				break;
			}			
			else{
				// get the list of possible states with their correct h value
				states = currentProblem.modifyState(heuristicMode);
				for(Problem problem : states){
					if(problem != null){
						if(!closedList.contains(problem)){
							problem.addToHeuristicValue(problem.getCostValue()); // cost
							queue.add(problem);
						}
					}					
				}
			}			
		}
	}
}


