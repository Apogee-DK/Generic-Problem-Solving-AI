package Othello;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import Core.Problem;
import Core.Game;
import Othello.Player;
import Othello.Othello;

/*
 * Class which allows players to interact with the AI
 */
public class OthelloGame implements Game{
	private Othello othello;	
	Player [] players;

	boolean blackChipTurn;
	int numOfEmptyTiles;
	int numOfPlayers;

	boolean gameStarted;
	boolean minimax;

	// set up the players of the game
	public OthelloGame(){
		players = new Player[2];

		players[0] = new Player();
		players[1] = new Player();

		othello = new Othello(players[0], players[1], minimax);		

		// initial values of the players and empty tiles
		numOfPlayers = -1;
		numOfEmptyTiles = 64;
	}

	// function which allows the game to be started
	public void play(Scanner UI){	

		boolean quit = false;
		boolean rematch = false;
		Player currentPlayer;

		int posX;
		int posY;

		// display message which states that the game started
		System.out.println("Starting the Othello game!");	

		// initialize starting state of the game
		initializePlayers(UI);

		randomizeStarter();

		initializeOthelloGame();	

		while(!quit){

			// rematch setup
			if(rematch){
				replay();
				rematch = false;
				gameStarted = true;
			}

			if(!gameStarted){		

				System.out.print("Would you like to quit? (Y/N) ");
				String option = UI.nextLine();

				while(!option.equals("Y") && !option.equals("N")){
					System.out.print("Please enter 'Y' or 'N': ");
					option = UI.nextLine();
				}

				if(option.equals("Y")){
					quit = true;
					continue;
				}

				System.out.println("Restarting game and initializing game settings...");

				resetGame(UI);				
			}

			while(gameStarted){							
				// Check if the game has ended
				if(endGame(numOfEmptyTiles)){
					// Ask players if they want to rematch?
					System.out.print("Rematch? (Y/N) ");
					String option = UI.nextLine();

					while(!option.equals("Y") && !option.equals("N")){
						System.out.print("Please enter 'Y' or 'N' for a rematch: ");
						option = UI.nextLine();
					}

					if(option.equals("Y")){
						rematch = true;					
					}
					else{
						rematch = false;
						gameStarted = false;
					}

					break;
				}

				// check if turn should be skipped
				if(blackChipTurn){
					currentPlayer = (players[0].getColor() == 'B')? players[0] : players[1];
					if(othello.blackPotentialMoves.size() == 0){
						System.out.println(currentPlayer.getName() + " has no possible moves, skipping turn.");
						blackChipTurn = !blackChipTurn;
						continue;
					}
				}
				else{
					currentPlayer = (players[0].getColor() == 'W')? players[0] : players[1];
					if(othello.whitePotentialMoves.size() == 0){
						System.out.println(currentPlayer.getName() + " has no possible moves, skipping turn.");
						blackChipTurn = !blackChipTurn;
						continue;
					}
				}

				// Display the current state of the board
				othello.printPuzzle();

				System.out.println("It is your move " + currentPlayer.getName() + ", place your " + currentPlayer.getColor() + " chip");

				// Ask for user input
				if(!currentPlayer.getAI()){
					System.out.print("Row: ");
					posX = UI.nextInt();
					UI.nextLine();

					System.out.print("Column: ");
					posY = UI.nextInt();
					UI.nextLine();


					// Place the chip			
					boolean validMove = placeChip(posX, posY, currentPlayer.getColor(), false);

					// check if the move is valid
					while(!validMove){
						System.out.print("Row: ");
						posX = UI.nextInt();
						UI.nextLine();

						System.out.print("Column: ");
						posY = UI.nextInt();
						UI.nextLine();					

						validMove = placeChip(posX, posY, currentPlayer.getColor(), false);
					}
				}
				else{
					// Player is an AI, use the othello class		

					// set color and player for the heuristic search
					othello.setColor(currentPlayer.getColor()); 
					othello.setPlayer(currentPlayer);

					// AI searches for the correct move
					currentPlayer.search(othello);

					// Use the solution vector as the chip placement
					Vector2 chipPosition = currentPlayer.results();

					System.out.println("I have move chip to " + chipPosition);

					// Place the chip
					placeChip(chipPosition.x, chipPosition.y, currentPlayer.getColor(), false);
				}

				othello.getTilesOwned(players[0], players[1]);
				blackChipTurn = !blackChipTurn;
			}
		}
	}

	// reset the board and the heuristics
	public void replay(){
		othello = new Othello(players[0], players[1], minimax);
		numOfEmptyTiles = 64;
		randomizeStarter();
		initializeOthelloGame();		
	}

	// reset the whole game
	public void resetGame(Scanner UI){
		minimax = false;
		othello = new Othello(players[0], players[1], minimax);
		numOfPlayers = -1;
		numOfEmptyTiles = 64;	
		initializePlayers(UI);
		randomizeStarter();
		initializeOthelloGame();	
	}

	// Set up the players of the game
	private void initializePlayers(Scanner UI){		
		System.out.println("Game Settings:");
		while(numOfPlayers < 0 || numOfPlayers > 2){
			System.out.print("0 or 2 players? ");
			numOfPlayers = UI.nextInt();
			UI.nextLine();				
		}

		//Set up players
		if(numOfPlayers == 2){		
			// Human vs Human
			System.out.print("What is first player's name? ");
			players[0].setName(UI.nextLine());

			System.out.print("What is second player's name? ");
			players[1].setName(UI.nextLine());
		}
		else if(numOfPlayers == 1){
			// Human vs AI
			System.out.print("What is your name? ");
			players[0].setName(UI.nextLine());

			players[1].setName("Siri");
			setupAI(UI, players[1]); // 1 is for player2
			players[1].setAI(true);
		}
		else{
			players[0].setName("Cortana");
			setupAI(UI, players[0]);
			players[0].setAI(true);

			players[1].setName("Siri");
			setupAI(UI, players[1]);
			players[1].setAI(true);
		}
	}

	// Randomize the chip color of each player
	private void randomizeStarter(){		
		System.out.println("\nRandomizing starting player");
		char playerOneChip = 'W';
		char playerTwoChip = 'B';		

		int start = ((int)(Math.random() * 10)) % 2;

		if(start == 0){
			playerOneChip = 'B';
			playerTwoChip = 'W';
		}

		players[0].setColor(playerOneChip);
		players[1].setColor(playerTwoChip);	
	}

	// Initialize the othello game
	private void initializeOthelloGame(){		
		players[0].resetPlayerStatus();
		players[1].resetPlayerStatus();	

		placeChip(3, 3, 'W', true);
		placeChip(3, 4, 'B', true);
		placeChip(4, 3, 'B', true);	
		placeChip(4, 4, 'W', true);		

		// Update the potential move list of each player by finding the new acceptable moves
		othello.findAcceptableMovesForPlayers(othello.getStartState(), othello.whitePotentialMoves, othello.blackPotentialMoves, othello.adjacentTilesToChips);

		blackChipTurn = true;
		gameStarted = true;
	}

	// Allow players to place the chip
	private boolean placeChip(int x, int y, char t, boolean setup){
		if(!setup){
			// placing the chip in a game that started
			if(t != 'B' && t != 'W'){
				System.out.println("Please place a correct chip: 'B' or 'W'");
				return false;
			}

			// Check if the player made a good move
			if(t == 'B'){
				if(!othello.blackPotentialMoves.contains(new Vector2(x, y))){
					System.out.println("Please place the black chip in the correct position");
					return false;
				}
			}
			else{
				if(!othello.whitePotentialMoves.contains(new Vector2(x, y))){
					System.out.println("Please place the white chip in the correct position");
					return false;
				}
			}		
		}

		// update board state
		othello.updatePuzzle(x, y, t);

		// decrease the number of tiles
		numOfEmptyTiles--;

		// increase the number of tiles owned by a player
		if(players[0].getColor() == t){
			players[0].increaseTiles();
		}
		else{
			players[1].increaseTiles();
		}

		// get the current state of the board
		char[][] board = othello.getStartState();

		if(!setup){
			// flip the chips on the board
			othello.flipChipsOnBoard(x, y, t, board);
		}

		// update the list of adjacent tiles
		othello.updateAdjacentTileList(x, y, board, othello.adjacentTilesToChips);

		if(!setup){
			// find all the possible moves for each player
			othello.findAcceptableMovesForPlayers(board, othello.whitePotentialMoves, othello.blackPotentialMoves, othello.adjacentTilesToChips);
		}

		return true;
	}

	// Function to check if the game ended
	boolean endGame(int numOfEmptyTiles){

		int playerOneTiles = players[0].getNumOfTilesOwned();
		int playerTwoTiles = players[1].getNumOfTilesOwned();

		// Player has no owned tiles, board has no tiles left or both players have no more moves means the end of a game
		if(numOfEmptyTiles == 0 || playerOneTiles == 0 || playerTwoTiles == 0 
				|| othello.whitePotentialMoves.size() + othello.blackPotentialMoves.size() == 0){

			othello.printPuzzle();			

			System.out.println("End of Game");

			// Display which player won the game
			if(playerOneTiles > playerTwoTiles){
				players[0].setWinner();
				System.out.println(players[0].getName() + " wins!");
			}
			else if (playerOneTiles == playerTwoTiles){
				System.out.println("Game ends in a tie");
			}
			else {
				players[1].setWinner();
				System.out.println(players[1].getName() + " wins!");
			}		

			return true;
		}	

		return false;
	}

	// Setup the AI for the Othello Game	
	private void setupAI(Scanner UI, Player p){
		System.out.println("AI Brain of " + p.getName());
		System.out.println("---------------------------------------------");
		System.out.println("1- Greedy Player ");
		System.out.println("2- MiniMax Sees the Future");
		System.out.println("Please select the AI you would like to use: ");

		p.setAIMode(UI.nextInt());
		UI.nextLine();

		System.out.println("---------------------------------------------");

		// Let user determine which heuristics should be used
		if(p.getAIMode() == 2){
			minimax = true;
			System.out.println("Heuristics");
			System.out.println("---------------------------------------------");
			System.out.println("1- Maximizing Number of Potential Moves Heuristic");
			System.out.println("2- Total Tile Score");
			System.out.println("3- Early Game VS Late Game Heuristic");
			System.out.println("4- Corners Are Key Heuristic");
			System.out.println("Select the heuristic you would like to use: ");

			p.setHeuristicMode(UI.nextInt());
			UI.nextLine();

			System.out.println("---------------------------------------------");			
		}		
	}
}
