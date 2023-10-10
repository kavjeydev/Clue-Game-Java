package Experiment;

import java.util.HashSet;
import java.util.Set;

public class TestBoard {
	
	private static TestBoard instance;
	private TestBoardCell[][] grid;
	private Set<TestBoardCell> targets;
	private Set<TestBoardCell> visited;
	final static int COLS = 4;
	final static int ROWS = 4;
	

	
	
	private TestBoard() {
		//allocated memory for the grid
		grid = new TestBoardCell[ROWS][COLS];
		targets = new HashSet<TestBoardCell>();
		visited = new HashSet<TestBoardCell>();
		
		//create board grid of cells
		for(int i = 0; i < ROWS; i++) {
			for(int j = 0; j < COLS; j++) {
				grid[i][j] = new TestBoardCell(i, j);;
			}
		}
		
		//calculate cell adjacencies
		calcAdjacencies();
	}
	
	
	/*
	 * adds adjacent cells to a set variable to each cell in the grid
	 */
	public void calcAdjacencies() {
		for(int i = 0; i < ROWS; i++) {
			for(int j = 0; j < COLS; j++) {
				
				//check left
				if(i > 0) {
					 if(!grid[i - 1][j].getOccupied()) {
						 grid[i][j].addAdjacency(grid[i - 1][j]);
					 }
				 }

				//check up
				 if(j > 0) {
					 if(!grid[i][j - 1].getOccupied()) {
						 grid[i][j].addAdjacency(grid[i][j - 1]);
					 }
				 }
				 
				 //check right
				 if(i + 1 < ROWS) {
					 if(!grid[i + 1][j].getOccupied()) {
						 grid[i][j].addAdjacency(grid[i + 1][j]);
					 }
				 }

				 //check down
				 if(j + 1 < COLS) {
					 if(!grid[i][j + 1].getOccupied()) {
						 grid[i][j].addAdjacency(grid[i][j + 1]);
					 }
				 }
			}
		}
	}
	
	/*
	 * TestBoard is a "singleton" class, which this method enforces by acting as a public access to a
	 * single instance of the class
	 */
	public static synchronized TestBoard getInstance() {
		//only create an instance if it doesn't already exist
		if(instance == null) {
			instance = new TestBoard();
			return instance;
		}
	
		return instance;
	}
	
	/*
	 * finds all possible target spaces for a given start cell and path length
	 */
	public void calcTargets(TestBoardCell startCell, int pathLength) {
		//clear the sets to make sure we don't mix up lists
		visited.clear();
		targets.clear();
		//make sure current cell is marked as visited
		visited.add(startCell);
		findAllTargets(startCell, pathLength);
	}
	
	public void findAllTargets(TestBoardCell startCell, int pathLength) {
		
		for(TestBoardCell cell : startCell.getAdjList()) {
			if(!visited.contains(cell) && !cell.getOccupied()) {
				visited.add(cell);
				if(pathLength == 1 && !cell.getOccupied())
					targets.add(cell);
				else if(!cell.getOccupied() && !cell.getRoom()){
					findAllTargets(cell, pathLength - 1);
				}
				else if(cell.getRoom())
					targets.add(cell);
				
			}
			visited.remove(cell);
		}
		
	}
	
	/*
	 * returns list of possible targets
	 */
	public Set<TestBoardCell> getTargets(){
		return targets;
	}
	
	/*
	 * returns cell at given board position
	 */
	public TestBoardCell getCell(int row, int column) {
		return grid[row][column];
	}
	
}