package clueGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.Set;



public class BoardCell {
	private static final int DOOR_DIRECTION_WIDTH = 4;
	private static final int FONT_SIZE = 15;
	private Set<BoardCell> adjList = new HashSet<BoardCell>();
	private int row;
	private int col;
	private boolean isRoom;
	private boolean isOccupied;
	private String symbol;
	private DoorDirection doorDirection;
	boolean isTarget = false;
	
	public BoardCell(int row, int col) {
		 this.row = row; 
		 this.col = col;
	}
	
	
	//Draws doors on the game board.
	public void draw(Graphics g, int width, int height) {
		Board board = Board.getInstance();
		int cellHeight = height/Board.NUM_ROWS;
		int cellWidth = width/Board.NUM_ROWS;
		
		g.setColor(getCellColor());
		g.fillRect(col*cellWidth, row*cellHeight, cellWidth, cellHeight);
		g.setColor(Color.black);
		g.drawRect(col*cellWidth, row*cellHeight, cellWidth, cellHeight);
		if(isLabel()) {
			Font comic = new Font("Comic Sans MS", Font.BOLD, FONT_SIZE);
			g.setFont(comic);
			g.setColor(Color.black);
			g.drawChars(board.getRoom(this).getName().toCharArray(), 0, 
					board.getRoom(this).getName().length(), col*cellWidth, row*cellHeight);
		}
		//If there is a doorway on the path, doors of different dimensions are drawn.
		if(isDoorway()) {
			int dX, dY, dWidth, dHeight;
			if(doorDirection == DoorDirection.LEFT) {
				dX = col*cellWidth;
				dY = row*cellHeight;
				dWidth = cellWidth/DOOR_DIRECTION_WIDTH;
				dHeight = cellHeight;
			} else if(doorDirection == DoorDirection.RIGHT) {
				dX = col*cellWidth + (cellWidth * 3/4);
				dY = row*cellHeight;
				dWidth = cellWidth/DOOR_DIRECTION_WIDTH;
				dHeight = cellHeight;
			} else if(doorDirection == DoorDirection.DOWN) {
				dX = col*cellWidth;
				dY = row*cellHeight + (cellHeight * 3/4);
				dWidth = cellWidth;
				dHeight = cellHeight/DOOR_DIRECTION_WIDTH;
			} else {
				dX = col*cellWidth;
				dY = row*cellHeight;
				dWidth = cellWidth;
				dHeight = cellHeight/DOOR_DIRECTION_WIDTH;
			}
			g.setColor(Color.blue);
			g.fillRect(dX, dY, dWidth, dHeight);
			g.drawRect(dX, dY, dWidth, dHeight);
		}
	}
	
	public void setIsTarget(boolean target) {
		isTarget = target;
	}
	
	public boolean getIsTarget() {
		return isTarget;
	}
	
	//Changes color depending on target requirements.
	public Color getCellColor() {
		if(isTarget)
			return Color.cyan;
		
		switch(symbol.charAt(0)) {
		case 'W':
			if(symbol.length() > 1)
				return Color.orange;
			else
				return Color.yellow;
		case 'X':
			return Color.black;
		default:
			return Color.green;
		}
	}
	
	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	/*
	 * add cell to this cell's list of adjacent cells
	 */
	public void addAdjacency(BoardCell cell) {
		adjList.add(cell);
	}
	
	/*
	 * returns list of cells adjacent to this cell
	 */
	public Set<BoardCell> getAdjList() {
		return adjList;
	}
	
	/*
	 * set this cell as a room cell
	 */
	public void setIsRoom(boolean isRoom) {
		this.isRoom = isRoom;
	}
	
	/*
	 * returns true if this cell is part of a room and not a walkable space
	 */
	public boolean getIsRoom() {
		return !(Board.getInstance().getRoom(this).getName().equals("Walkway")
				|| Board.getInstance().getRoom(this).getName().equals("Unused"));
	}
	
	/*
	 * returns true if this cell is occupied by a player
	 */
	public boolean getIsOccupied() {
		return isOccupied;
	}
	
	/*
	 * sets the occupancy status of the cell (whether or not a player is on it)
	 */
	public void setOccupied(boolean occu) {
		isOccupied = occu;
	}

	
	/*
	 * returns true if this cell is a doorway
	 */
	public boolean isDoorway() {
		//check if label is W followed by another character
		return symbol.charAt(0) == 'W' && symbol.length() > 1;
	}

	/*
	 * returns the direction of doorway if this space is a doorway,
	 * otherwise, returns null
	 */
	public DoorDirection getDoorDirection() {
		if(!this.isDoorway()) return null;
		
		//decide direction off of second character
		switch(symbol.charAt(1)) {
		case '>':
			return DoorDirection.RIGHT;
		case '<':
			return DoorDirection.LEFT;
		case 'v':
			return DoorDirection.DOWN;
		default:
			return DoorDirection.UP;
		}
		
	}

	

	public boolean isRoomCenter() {
		//get second character if present
		char secondChar = ' ';
		if(symbol.length() > 1)
			secondChar = symbol.charAt(1);
		
		//return true if second character is an asterisk
		if(secondChar == '*')
			return true;
		
		return false;
	}

	/*
	 * returns character representing room secret a secret passage goes to,
	 * if this cell is a secret passage, otherwise return 0
	 */
	public char getSecretPassage() {
		if(symbol.length() <= 1)
			return 0;
		char c = symbol.charAt(1);
		if(c == '*' || c == '#')
			return 0;
		return c;
	}
	
	public boolean isLabel() {
		//get second character if present
		char secondChar = ' ';
		if(symbol.length() > 1)
			secondChar = symbol.charAt(1);
		
		//return true if second character is a pound sign
		if(secondChar == '#')
			return true;
		
		return false;
	}

	public void setLabel(String symbol) {
		this.symbol = symbol;
	}

	public String getLabel() {
		return symbol;
	}

	public void setDoorDirection(DoorDirection input) {
		doorDirection = input;
	}

	public boolean isSecretPassage() {
		//if the second char is a secret passage char (i.e. not direction or type label),
		//give the room a secret passage
		return (getLabel().length() > 1 && getLabel().charAt(1) != '*'
				&& getLabel().charAt(1) != '#'
				&& getLabel().charAt(1) != '^'
				&& getLabel().charAt(1) != '>'
				&& getLabel().charAt(1) != '<'
				&& getLabel().charAt(1) != 'v');
	}

	

	
}
