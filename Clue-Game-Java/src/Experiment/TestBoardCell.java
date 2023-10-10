package Experiment;

import java.util.HashSet;
import java.util.Set;

public class TestBoardCell {
	private Set<TestBoardCell> adjList = new HashSet<TestBoardCell>();
	private int row;
	private int col;
	private boolean isRoom;
	private boolean isOccupied;

	
	public TestBoardCell(int row, int col) {
		 this.row = row; 
		 this.col = col;
	}
	
	/*
	 * add a cell to this cell's list of adjacencies
	 */
	public void addAdjacency(TestBoardCell cell) {
		adjList.add(cell);
	}
	
	/*
	 * returns list of cells adjacent to this cell
	 */
	public Set<TestBoardCell> getAdjList() {
		return adjList;
	}
	
	/*
	 * set this cell as a room cell
	 */
	public void setIsRoom(boolean isRoom) {
		this.isRoom = isRoom;
	}
	
	/*
	 * returns true if this cell is part of a room
	 */
	public boolean getRoom() {
		return isRoom;
	}
	
	/*
	 * returns true if this cell is occupied by a player
	 */
	public boolean getOccupied() {
		return isOccupied;
	}
	
	/*
	 * sets the occupancy status of the cell (whether or not a player is on it)
	 */
	public void setOccupied(boolean occu) {
		isOccupied = occu;
	}
}
