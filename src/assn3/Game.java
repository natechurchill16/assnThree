package assn3;

import java.util.LinkedList;

import javax.swing.JDialog;

/**
 * This class should be where the game is controlled.
 * 
 */

import javax.swing.JOptionPane;


public class Game {
	//Attributes of the game class
	private boolean usersTurn = false;
	private UserInterface ui = null;
	//This is currently used for the UI but may be able to use it other places
	private Node currentGameState;
	private LinkedList<Integer> currentIndex;
	
	
	/**
	 * Constructor for the game class
	 * 
	 */
	public Game() {
		this.ui = new UserInterface();
		this.currentIndex = new LinkedList<Integer>();
		for(int i=0; i < 7; i++) {
			currentIndex.add(0);
		}

	}
	
	/**
	 * This method starts the game
	 */
	public void startGame() {
		for(;;) {
			JOptionPane optionPane = new JOptionPane("Do you want to move first? y/n", JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, null, null);
			optionPane.setWantsInput(true);
			JDialog dialog = optionPane.createDialog(null, "Wecome!");
			dialog.setLocation(100, 220);
			dialog.setVisible(true);
			String usrInput = (String) optionPane.getInputValue();			
			usrInput = usrInput.toLowerCase();
			
			if (usrInput.equals("y")) {
				usersTurn = true;
				break;
			} else if (usrInput.equals("n")){
				usersTurn = false;
				break;
			} 
		}
		

		int col;
		if (usersTurn) {
			col = getUserMove();
		} else {
			col = 3; //Maybe some thing else for the first bot move????
		}
		int row = generateRow(col);

		int playerNum = 0;
		if (usersTurn) {
			playerNum = 1;
		}
		//apply move to the UI
		this.ui.applyMove(row, col, playerNum);
		this.currentGameState = new Node(col, getPlayerInt(this.usersTurn));
		updateState(row, col, getPlayerInt(this.usersTurn));
		this.usersTurn = !this.usersTurn;
		getAndApplyMove();
		
	}
		
		
		
	
	
	public void getAndApplyMove() {
		int col;
		if (usersTurn) {
			col = getUserMove();
		} else {
			col = getBotMove();
		}
		int row = generateRow(col);

		int playerNum = 0;
		if (usersTurn) {
			playerNum = 1;
		}
		this.ui.applyMove(row, col, playerNum);
		updateState(row, col, getPlayerInt(this.usersTurn));

		this.usersTurn = !this.usersTurn;
		printState();

		StateSpace blankSpace = new StateSpace();
		int win = blankSpace.winningState(this.currentGameState);
		
		if (win == 0) {
			getAndApplyMove();
		} else {
			if (win == 1) {
				JOptionPane.showMessageDialog(null, "Player 1  WON!!");
			}else {
				JOptionPane.showMessageDialog(null, "Player 2  WON!!");
			}
		}
		
	}
	
	/**
	 * Get an integer from the user, Nothing else
	 * 
	 * @return the integer from the user
	 */
	public int getUserMove() {
		boolean goodInput = false;
		int column = 0;
		while (!goodInput) {
			String input = JOptionPane.showInputDialog("Enter your move 0 to 6");
			boolean p1 = false;
			boolean p2 = false;
			if(input.matches("[0-6]{1}")) {
				p1 = true;
				column = Integer.parseInt(input);
			}
			if (validateMove(column)) {
				p2 = true;	
			}
			
			goodInput = (p1 && p2);
		}
		
		
		return column;
	}
	
	public int getBotMove(){
		boolean goodInput = false;
		int randomNum = 0;
		while (!goodInput) {
			randomNum =  (int)(Math.random() * 7);
			if (validateMove(randomNum)) {
				 goodInput = true;	
			}
		}
		
		//StateSpace graph = new StateSpace(this.currentGameState, getPlayerInt(this.usersTurn));
		
		
		
		return randomNum;
	}
	
	/** a pre-validation method to validate the player BEFORE generate row is called
	 * 
	 * @param col
	 * @return
	 */
	private boolean validateMove(int col) {
		int filled = this.currentIndex.get(col);
		if (filled < 6) {
			return true;
		}else {
			return false;
		}	
	}
	
	private int generateRow(int col) {
		int row = this.currentIndex.get(col);
		int newRow = row + 1;
		this.currentIndex.set(col, newRow); //set the next index
		return row;
	}
	
	private int getPlayerInt(boolean usersTurn) {
		if (usersTurn) {
			return 1;
		}else {
			return 2;
		}
	}
	
	private void updateState(int row, int col, int playerNum) {
		int[][] game = this.currentGameState.getGameState();
		
		game[row][col] = playerNum;
		this.currentGameState.setGameState(game);
	}
	
	
	public void printState() {
		int[][] game = this.currentGameState.getGameState();
		for (int i = game.length -1; i >=0 ; i--) {
			for (int j = 0; j < game[i].length; j++) {
				System.out.printf("%d  ", game[i][j]);
			}
			System.out.println("");
		}
		System.out.println("\n\n");
	}
	
	
	
	

}
