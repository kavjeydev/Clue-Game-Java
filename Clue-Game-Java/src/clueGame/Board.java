package clueGame;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel{
	// constants
	public static int NUM_ROWS = 25;
	public static int NUM_COLS = 24;
	public static int NUM_PLAYERS = 6;
	public int lostCount = 0;

	// lists and board data
	private BoardCell[][] grid;
	private String[][] symbolGrid;
	private Map<Character, Room> symbolRoomMap;

	// BoardCell lists for calculating adjacency
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;

	// singelton instance
	private static Board instance = new Board();

	// filepaths for setup
	private String csv;
	private String setup;

	private ArrayList<Player> players;
	private Solution theAnswer;
	private ArrayList<Card> deck;
	
	private Player currentPlayer;
	private int currentRoll;
	private boolean playerHasMoved;
	
	ClueGame game = ClueGame.getInstance();
	
	private Color[] colors = {Color.red, Color.orange, Color.pink, Color.blue, Color.white, Color.green};
	private boolean waitingForSuggestion;

	// constructor is private to ensure only one can be created
	private Board() {
		super();
	}

	/*
	 * this method returns the static Board instance
	 */
	public static Board getInstance() {
		return instance;
	}
	
	//Draws all players and cells on the gameboard.
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for(BoardCell[] row : grid) {
			for(BoardCell cell : row) {
				cell.draw(g, getWidth(), getHeight());
			}
		}
		for(Player player : players) {
			player.draw(g, getWidth(), getHeight());
		}
		g.setColor(Color.BLUE);
	}

	/*
	 * finds all possible target spaces for a given start cell and path length
	 */
	public void calcTargets(BoardCell startCell, int pathLength) {
		// clear the sets to make sure we don't mix up lists
		visited.clear();
		targets.clear();
		// make sure current cell is marked as visited
		visited.add(startCell);
		findAllTargets(startCell, pathLength);
		

		
	}

	/*
	 * recursive method that finds all possible targets for a move given a starting
	 * space and a roll (number of spaces to move) this method should only be called
	 * from the outside by the calcTargets() method
	 */
	private void findAllTargets(BoardCell startCell, int pathLength) {
		// go through adjacency list of current cell
		for (BoardCell cell : startCell.getAdjList()) {
			// make sure the cell we're looking at is neither visited nor occupied
			if (!visited.contains(cell) && pathLength >= 1) {
				// add to visited list
				visited.add(cell);
				// if the path length is one (only one move left) we add the cell to our targets
				if (pathLength == 1 && !cell.getIsOccupied()) {
					targets.add(cell);
				} else if (cell.getIsRoom()) {
					targets.add(cell);
				} else if (!cell.getIsOccupied()) {
					findAllTargets(cell, pathLength - 1);
				}
				visited.remove(cell);
			}

		}
		
	}

	/*
	 * initialize the board (since we are using singleton pattern)
	 */
	public void initialize() {
		
		
		symbolRoomMap = new HashMap<Character, Room>();

		symbolGrid = new String[NUM_ROWS][NUM_COLS];
		grid = new BoardCell[NUM_ROWS][NUM_COLS];

		deck = new ArrayList<Card>();

		players = new ArrayList<Player>();

		targets = new HashSet<BoardCell>();
		visited = new HashSet<BoardCell>();

		players = new ArrayList<Player>();
		
		addMouseListener(new ClueMouseListener());

		// create board grid of cells
		for (int i = 0; i < NUM_ROWS; i++) {
			for (int j = 0; j < NUM_COLS; j++) {
				grid[i][j] = new BoardCell(i, j);
			}
		}

		// wrap in try catch in case of bad formatting
		try {
			loadSetupConfig();
			loadLayoutConfig();
		} catch (BadConfigFormatException err) {
			System.out.println(err);
			return;
		}

		for (int i = 0; i < NUM_ROWS; i++) {
			for (int j = 0; j < NUM_COLS; j++)
				calcAdjacencies(grid[i][j]);
		}
		
		deal();

		players.get(0).setRow(5);
		players.get(0).setCol(3);

		players.get(1).setRow(18);
		players.get(1).setCol(18);

		players.get(2).setRow(0);
		players.get(2).setCol(17);

		players.get(3).setRow(8);
		players.get(3).setCol(22);

		players.get(4).setRow(8);
		players.get(4).setCol(8);

		players.get(5).setRow(20);
		players.get(5).setCol(4);

		currentPlayer = players.get(0);
	}

	/*
	 * deals cards to players' hands and sets solution cards
	 */
	public void deal() {
		Random rand = new Random();
		// make a copy of deck
		ArrayList<Card> tempDeck = new ArrayList<Card>();
		tempDeck.addAll(deck);

		// generate indices for random cards
		int roomIndex = rand.nextInt(8);
		int personIndex = rand.nextInt(5) + 9;
		int weaponIndex = rand.nextInt(5) + 15;

		// add three different type cards to solution and remove them from the deck in
		// play
		theAnswer = new Solution(tempDeck.get(roomIndex), tempDeck.get(weaponIndex),
				tempDeck.get(personIndex));
	

		tempDeck.remove(roomIndex);
		tempDeck.remove(personIndex - 1);
		tempDeck.remove(weaponIndex - 2);

		// shuffle deck together
		Collections.shuffle(tempDeck);

		// iterate through players, adding 3 random cards to each hand
		for (int i = 0; i < players.size(); i++) {
			players.get(i).updateHand(tempDeck.get(3*i));
			players.get(i).updateHand(tempDeck.get((3*i) + 1));
			players.get(i).updateHand(tempDeck.get((3*i) + 2));
		}
		
		

	}

	/*
	 * calculates cell adjacencies
	 */
	public void calcAdjacencies(BoardCell cell) {
		int i, j;
		i = cell.getRow();
		j = cell.getCol();
		BoardCell up;
		BoardCell down;
		BoardCell left;
		BoardCell right;

		// ignore some directions and set to null if outside array

		if (i < NUM_ROWS - 1)
			down = grid[i + 1][j];
		else
			down = null;

		if (i > 0)
			up = grid[i - 1][j];
		else
			up = null;

		if (j < NUM_COLS - 1)
			right = grid[i][j + 1];
		else
			right = null;

		if (j > 0)
			left = grid[i][j - 1];
		else
			left = null;

		BoardCell[] dirs = { up, down, left, right };

		// if cell is part of a room, don't operate on it
		if (!cell.getIsRoom()) {
			for (BoardCell dir : dirs) {
				// skip if we ignored a direction from earlier
				if (dir == null)
					continue;
				if (cell.isDoorway()) {
					if (dir.getIsRoom()) {
						// make sure door direction matches room we're looking at before adding
						if ((dir.equals(up) && cell.getDoorDirection() == DoorDirection.UP)
								|| (dir.equals(down) && cell.getDoorDirection() == DoorDirection.DOWN)
								|| (dir.equals(left) && cell.getDoorDirection() == DoorDirection.LEFT)
								|| (dir.equals(right) && cell.getDoorDirection() == DoorDirection.RIGHT)) {
							cell.addAdjacency(getRoom(dir).getCenterCell());
							getRoom(dir).getCenterCell().addAdjacency(cell);
						}
						continue;
					}
					// add if not a walkway
					if (!getRoom(dir).getName().equals("Unused")) {
						cell.addAdjacency(dir);
					}
				} else if (!cell.getIsRoom()) {
					// add if a walkway
					if (getRoom(dir).getName().equals("Walkway")) {
						cell.addAdjacency(dir);
					}
				}
			}
		} else if (cell.isSecretPassage()) {
			getRoom(cell).getCenterCell().addAdjacency(getRoom(cell.getLabel().charAt(1)).getCenterCell());
		}
	}

	/*
	 * sets filepaths for configuration files
	 */
	public void setConfigFiles(String csv, String setup) {
		this.csv = csv;
		this.setup = setup;
	}

	/*
	 * load setup file and create rooms based on contents
	 */
	public void loadSetupConfig() throws BadConfigFormatException {
		Scanner inFile;

		// account for file not existing with try-catch
		try {
			inFile = new Scanner(new File(setup));
		} catch (FileNotFoundException err) {
			System.out.println("Input file for setup not found");
			return;
		}

		// go through every line

		while (inFile.hasNextLine()) {
			String line = inFile.nextLine();
			int lineCounter = 1;
			String[] splitLine;

			// make sure line is not a comment
			if (!line.substring(0, 2).equals("//")) {
				// split into pieces by comma
				splitLine = line.split(", ");

				// make sure each entry is preceded by "room" or "space"
				if (!splitLine[0].equals("Room") && !splitLine[0].equals("Space") &&
						!splitLine[0].equals("Weapon") && !splitLine[0].equals("Person"))
					throw new BadConfigFormatException(lineCounter, line);

				//create cards and add to deck with proper parameters
				if(splitLine[0].equals("Weapon")) {
					addToDeck(new Card(splitLine[1], CardType.WEAPON));
				} else if(splitLine[0].equals("Person")) {
					if(players.isEmpty())
						players.add(new HumanPlayer(splitLine[1], colors[0]));
					else
						players.add(new ComputerPlayer(splitLine[1], colors[players.size()]));
					addToDeck(new Card(splitLine[1], CardType.PERSON));
				} else if(splitLine[0].equals("Room")) {
					addToDeck(new Card(splitLine[1], CardType.ROOM));
				}
				
				// make sure max 3 values on each line
				if (splitLine.length == 3) {
					// add room to symbol map
					Room room = new Room(splitLine[1], splitLine[2].charAt(0));
					symbolRoomMap.put(room.getLabel(), room);
				}
				lineCounter++;
			}

		}
		
		inFile.close();

	}


	/*
	 * load layout file and set up board from contents
	 */
	public void loadLayoutConfig() throws BadConfigFormatException {
		Scanner inFile;

		// account for file not found with try-catch
		try {
			inFile = new Scanner(new File(csv));
		} catch (FileNotFoundException err) {
			System.out.println("Input file for board not found");
			return;
		}

		// keep track of line
		int currentLine = 0;
		// go over file
		while (inFile.hasNextLine()) {
			// just set a whole row of the board to a line split by comma
			symbolGrid[currentLine] = inFile.nextLine().split(",");

			// make sure every line is the right num of columns
			if (symbolGrid[currentLine].length != NUM_COLS)
				throw new BadConfigFormatException("different number of columns in each row");
			currentLine++;

		}
		inFile.close();

		// set symbols for all cells after files are read in
		for (int i = 0; i < NUM_ROWS; i++) {
			for (int j = 0; j < NUM_COLS; j++) {
				BoardCell cell = grid[i][j];

				// make sure the cell labels are not longer than allowed
				if (symbolGrid[i][j].length() > 2)
					throw new BadConfigFormatException("layout file has bad labels");

				// make sure there is a room associated with the label given
				if (!symbolRoomMap.containsKey(symbolGrid[i][j].charAt(0)))
					throw new BadConfigFormatException("layout file contains rooms not in setup");

				cell.setLabel(symbolGrid[i][j]);
				cell.setIsRoom(symbolGrid[i][j].charAt(0) != 'W' && symbolGrid[i][j] != "X");
				// if the cell is a label or a center, set the room property to match
				if (cell.isLabel()) {
					symbolRoomMap.get(cell.getLabel().charAt(0)).setLabelCell(cell);
				}
				if (cell.isRoomCenter()) {
					symbolRoomMap.get(cell.getLabel().charAt(0)).setCenterCell(cell);
				}

				// set door direction based on second char
				if (cell.getLabel().length() > 1 && cell.getLabel().charAt(0) == 'W') {
					char c = cell.getLabel().charAt(1);
					if (c == '<')
						cell.setDoorDirection(DoorDirection.LEFT);
					else if (c == '>')
						cell.setDoorDirection(DoorDirection.RIGHT);
					else if (c == '^')
						cell.setDoorDirection(DoorDirection.UP);
					else if (c == 'v')
						cell.setDoorDirection(DoorDirection.DOWN);
					else
						System.out.println("Error reading spaces in layout file");
				}
			}
		}
	}
	
	/**
	 * function in which most events of a turn occur
	 */
	public void doTurn() {
		//skip player if they've already lost the game
		if(currentPlayer.getHasLost())
			return;
		//roll the die
		Random rand = new Random();
		currentRoll = rand.nextInt(5) + 1;
					
		//get targets
		calcTargets(currentPlayer.getCell(), currentRoll);
		
		//if the current player is a computer,  go through cpu turn
		if(!currentPlayer.getIsHuman()) {
			doComputerTurn();
		} else {
			doHumanTurn();
		}
		game.updateGUI();
		repaint();
	}
	
	/**
	 * game loss when computer guesses correctly
	 * @param winner : player who won game
	 */
	private void loseGame(Player winner) {
		JOptionPane.showMessageDialog(null, winner.getName() + " won! You lost!\n" + "The correct answer was:\n"
				+ theAnswer.getPerson() + " in the " + theAnswer.getRoom() + " with the "
				+ theAnswer.getWeapon() + ".");
		game.dispose();
		
	}
	
	/**
	 * game loss when human guesses incorrectly
	 */
	public void loseGame() {
		JOptionPane.showMessageDialog(null, "You guessed wrong! You lost!\n" + "The correct answer was:\n"
					+ theAnswer.getPerson() + " in the " + theAnswer.getRoom() + " with the "
					+ theAnswer.getWeapon() + ".");
		game.dispose();
	}
	
	/**
	 * human turn operations
	 */
	private void doHumanTurn() {
		//reset guess UI
		game.controlPanel.setGuess("");
		game.controlPanel.setGuessResult("");
		
		//mark all targets
		for(BoardCell cell : targets) {
			cell.setIsTarget(true);
		}
		
		//set state to waiting for movement
		if(targets.size() != 0)
			playerHasMoved = false;
		else
			playerHasMoved = true;
	}
	
	/**
	 * computer turn operations
	 */
	private void doComputerTurn() {
		//if cpu is ready to accuse
		if(currentPlayer.isReadyToAccuse()) {
			//see if they're correct
			if(checkAccusation(((ComputerPlayer) currentPlayer).getLastSuggestion())) {
				loseGame(currentPlayer);
				return;
			//otherwise, notify human they lost and remove them from the board
			} else {
				JOptionPane.showMessageDialog(null, currentPlayer.getName() + " guessed wrong! They lost!");
				//disables draw function
				currentPlayer.setHasLost(true);
				lostCount++;
				if(lostCount == 5)
					winGame();
				return;
			}
		}
		//if they aren't accusing, choose a cell to go to
		currentPlayer.setCell(currentPlayer.selectTarget());
		//see if landed in a room and suggest if true
		if(currentPlayer.getCell().getIsRoom()) {
			
			Solution suggestion = currentPlayer.createSuggestion();
			Card disprovenCard = handleSuggestionCard(suggestion, currentPlayer);
			Player disprovenPlayer = handleSuggestionPlayer(suggestion, currentPlayer);
			
			//update UI with cpu guess
			game.controlPanel.setGuess(suggestion.getPerson() + " with the " + suggestion.getWeapon() + " in the " + suggestion.getRoom());
			
			//report suggestion disproven and who disproved, if necessary
			if(disprovenPlayer != null) {
				game.controlPanel.setGuessResult("Suggestion disproven by " + disprovenPlayer.getName() + ".");
				currentPlayer.updateSeen(disprovenCard);
			} else {
				game.controlPanel.setGuessResult("Suggestion was not disproven.");
				currentPlayer.setIsReadyToAccuse(true);
			}
			
		} else {
			//reset guess UI
			game.controlPanel.setGuess("");
			game.controlPanel.setGuessResult("");
		}
	}

	/**
	 * moves on to next player and executes turn operations
	 */
	public void nextTurn() {
		if(players.indexOf(currentPlayer) + 1 < players.size())
			currentPlayer = players.get(players.indexOf(currentPlayer) + 1);
		else
			currentPlayer = players.get(0);
		doTurn();
	}

	/**
	 * performs relevant operations given an x and y location board was clicked on
	 * @param x : x position of click
	 * @param y : y position of click
	 */
	public void handleBoardClicked(int x, int y) {
		//make sure current player is human and hasn't moved yet
		if(!currentPlayer.getIsHuman() || playerHasMoved)
			return;
		
		//convert from pixel coords to board cell coords
		int col = x / (getWidth() / Board.NUM_COLS);
		int row = y / (getHeight() / Board.NUM_ROWS);
		BoardCell cellClicked;
		//make sure clicked coords within bounds of board
		if(col <= Board.NUM_COLS && row <= Board.NUM_ROWS)
			cellClicked = getCell(row, col);
		else
			return;
		
		
		//if cell is in a room, make sure player will move to the room center
		if(cellClicked.getIsRoom())
			cellClicked = getRoom(cellClicked).getCenterCell();
		
		//check if player clicks a valid target (or if they're in a room, the same room clicked)
		if(cellClicked.getIsTarget() || (getRoom(cellClicked) == getRoom(currentPlayer.getCell())
				&& cellClicked.getIsRoom())) {
			
			//move player
			currentPlayer.setCell(cellClicked);
			
			//if moved to a room, make a suggestion
			if(cellClicked.getIsRoom()) {
				waitingForSuggestion = true;
				humanMakeSuggestion(getRoom(cellClicked));
			}
			
			//remove all target markings
			for(BoardCell cell : targets) {
				cell.setIsTarget(false);
			}
			playerHasMoved = true;
			repaint();
		} else {
			//error if invalid target
			JOptionPane.showMessageDialog(null, "You can't move there.");
		}
		
	}
	
	/**
	 * create dialogue for a suggestion from a human player
	 */
	public void humanMakeSuggestion(Room room) {
		SuggestionDialogue suggestionDialogue = new SuggestionDialogue(deck, room);
	}

	/**
	 * checks if a solution correctly identifies answer to game
	 * @param solution : solution to check
	 * @return true if solution is correct
	 */
	public boolean checkAccusation(Solution solution) {
		return theAnswer.equals(solution);
	}

	/**
	 * set game answer
	 * @param room
	 * @param person
	 * @param weapon
	 */
	public void setAnswer(Card room, Card person, Card weapon) {
		theAnswer = new Solution(room, person, weapon);
	}

	/**
	 * handles a player's suggestion of an answer
	 * @param sol : suggestion to handle
	 * @param maker : player who made suggestion
	 * @return Card that disproved suggestion, or null if none found
	 */
	public Card handleSuggestionCard(Solution sol, Player maker) {
		if(handleSuggestionPlayer(sol, maker) != null)
			return handleSuggestionPlayer(sol, maker).disproveSuggestion(sol);
		else
			return null;
	}
	
	/**
	 * handles a player's suggestion of an answer
	 * @param sol : suggestion to handle
	 * @param maker : player who made suggestion
	 * @return Player that disproved suggestion, or null if none found
	 */
	public Player handleSuggestionPlayer(Solution sol, Player maker) {
		//put suggested player in room
		for(Player p : players) {
			if(p.getName().equals(sol.getPerson().getName())){
				p.setCell(maker.getCell());
			}
		}
		//see if anyone can disprove the suggestion
		for (int j = 0; j < players.size(); j++) {
			if (players.get(j).disproveSuggestion(sol) != null && !players.get(j).equals(maker)) {
				return players.get(j);
			}
		}

		return null;
	}
	
	/**
	 * sets up accusation dialogue for human player
	 */
	public void accuse() {
		AccuseDialogue accuse = new AccuseDialogue(deck);
	}

	/**
	 * displays win message and closes game
	 */
	public void winGame() {
		JOptionPane.showMessageDialog(null, "You Win!");
		game.dispose();
	}
	
	/**
	 * runs relevant operations for a human player suggestion
	 * @param solution : suggestion made by human player
	 */
	public void makeHumanSuggestion(Solution solution) {
		Player disprover = handleSuggestionPlayer(solution, currentPlayer);
		Card evidence = handleSuggestionCard(solution, currentPlayer);
		game.controlPanel.setGuess(solution.getPerson() + " with the " + 
				solution.getWeapon() + " in the " + solution.getRoom());
		if(evidence == null) {
			game.controlPanel.setGuessResult("No one could disprove your suggestion.");
		} else {
			game.controlPanel.setGuessResult(disprover.getName() + " disproved your suggestion with "
					+ evidence.getName() + ".");
			((HumanPlayer) currentPlayer).updateSeen(evidence, disprover);
		}
		game.updateGUI();
		repaint();
	}

	/**
	 * returns room associated with a cell, if one exists otherwise, returns null
	 * @param cell : cell to check room of
	 * @return room associated with cell
	 */
	public Room getRoom(BoardCell cell) {
		// get cell's symbol
		char cellChar = symbolGrid[cell.getRow()][cell.getCol()].charAt(0);
		return symbolRoomMap.get(cellChar);
	}

	/*
	 * returns room associated with a label character, if one exists otherwise,
	 * returns null
	 */
	public Room getRoom(char c) {
		return symbolRoomMap.get(c);
	}

	/*
	 * returns number of rows in board
	 */
	public int getNumRows() {
		return Board.NUM_ROWS;
	}

	/*
	 * returns number of columns on board
	 */
	public int getNumColumns() {
		return Board.NUM_COLS;

	}

	/*
	 * returns specified cell on board by row and column position
	 */
	public BoardCell getCell(int i, int j) {
		return grid[i][j];
	}

	/*
	 * return adjacency cells of given row, col
	 */
	public Set<BoardCell> getAdjList(int i, int j) {
		return grid[i][j].getAdjList();
	}

	/*
	 * return list of possible targets for a roll calculated in calcTargets()
	 */
	public Set<BoardCell> getTargets() {
		return targets;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public Solution getAnswer() {
		return theAnswer;
	}

	public ArrayList<Card> getDeck() {
		return deck;

	}

	public void addToDeck(Card card) {
		deck.add(card);
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	public int getCurrentRoll() {
		return currentRoll;
	}

	public boolean getPlayerHasMoved() {
		return playerHasMoved;
	}

	

}