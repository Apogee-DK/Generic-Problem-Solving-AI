package Core;
import java.util.Scanner;
import EightPuzzle.EightPuzzleGame;
import Othello.OthelloGame;

/*
 * The Driver class instantiates a problem and a state space. 
 * Uses the user's input too determine the search mode and heuristics.
 * The program times itself in order to check the duration of the program execution.
 * 
 * For further information, please consult the Tests pdf file for results on each search mode and heuristic
 * 
 */

public class Driver {	
	public static void main(String [] args){
		System.out.println("WELCOME TO DEXTER AND DANIIL'S GAME ZONE");
		Scanner user_input = new Scanner(System.in);
		int userInput;
		boolean quit = false;		

		while (!quit){
			System.out.println("--------------------------------------");
			System.out.println("Menu Options:");
			System.out.println("1 - Eight Puzzle Game");
			System.out.println("2 - Othello");
			System.out.println("0 - Quit");

			userInput = user_input.nextInt();
			user_input.nextLine();
			
			System.out.println("--------------------------------------");

			if(userInput == 1){
				new EightPuzzleGame().play(user_input);				
			}
			else if(userInput == 2){				
				new OthelloGame().play(user_input);
			}
			else if(userInput == 0){
				quit = true;				
			}

		}

		user_input.close();		
	}	
}
