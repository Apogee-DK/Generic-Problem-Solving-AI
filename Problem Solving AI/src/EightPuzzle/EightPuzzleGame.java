package EightPuzzle;

import java.util.Scanner;

import Core.Problem;
import Core.Game;

public class EightPuzzleGame implements Game{

	public void play(Scanner UI){
		
		System.out.println("Starting the Eight puzzle game!");
		
		int mode = 0;
		int hMode = 0;
		boolean quit = false;
		
		// the following variables are used for timing the results
		long timeStart;
		long timeEnd;
		float timeResult;
		
		while(!quit){	
			timeStart = 0;
			timeEnd = 0;
			timeResult = 0.0f;
			
			
			System.out.println("Please enter the goal state: ");
			String goalStateString = UI.nextLine();

			System.out.println("Please enter the initial state: ");
			String initialStateString = UI.nextLine();		

			goalStateString = goalStateString.replaceAll(" ", "");
			char [] goalState = goalStateString.toCharArray();

			initialStateString = initialStateString.replaceAll(" ", "");		
			char [] initialState = initialStateString.toCharArray();

			Problem puzzle = new EightPuzzle(initialState, goalState, goalState.length);

			EightPuzzleStateSpace stateSpace = new EightPuzzleStateSpace();

			while(mode <= 4){			
				System.out.println("-------------------------");
				System.out.println("\nChoose your mode: ");
				System.out.println("0: BFS");
				System.out.println("1: DFS");
				System.out.println("2: Best First Search");
				System.out.println("3: A*");
				System.out.println("-------------------------");
				System.out.println("4: Change tile pattern");	
				System.out.println("5: To Quit");
				System.out.println("-------------------------");

				mode = UI.nextInt();
				UI.nextLine();
				
				if(mode < 0 || mode > 5){
					System.out.println("Please select an option listed below...");
					continue;
				}
				
				if(mode == 2 || mode == 3){
					System.out.println("Choose your heuristic: ");
					System.out.println("0: Manhattan Distance");
					System.out.println("1: Misplaced Tiles");
					System.out.println("2: Min (Manhattan, Misplaced)");
					System.out.println("3: My Admissable Heuristic - Misplaced Tiles in Row & Column");
					System.out.println("4: My Non-Admissable Heuristic - Hashing Code");
					
					hMode = UI.nextInt();	
					UI.nextLine();
				}				

				if(mode < 4){
					System.out.println();
					
					// determine time it took to solve the solution
					timeStart = System.nanoTime();
					stateSpace.searchMode(puzzle, mode, hMode);					
					timeEnd = System.nanoTime();
					timeResult = ((timeEnd - timeStart) / 10000) / 100.0f;
					
					// display the solution
					stateSpace.showSolution();
					
					// display the time it took
					System.out.println("Solution took " + timeResult + " ms to solve.");					
				}
				else if(mode == 4){
					System.out.println();
					break;
				}
			}
			
			if(mode == 5){
				quit = true;
			}
		} 
	}
	
}

