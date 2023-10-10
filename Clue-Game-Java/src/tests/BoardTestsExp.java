package tests;


import java.util.Set;


import org.junit.Assert;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class BoardTestsExp {

	Board board;

	// initializes new test board.

	@BeforeEach
	public void setup() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("data/ClueLayout.csv", "data/ClueSetup.txt");
		// Initialize will load config files
		board.initialize();
	}

	// Tests adjacent cells

	@Test
	public void testAdjacency() {
		BoardCell cell = board.getCell(0, 0);
		Set<BoardCell> testList = cell.getAdjList();
		// testList.add(new BoardCell(4,4));
		Assert.assertTrue(testList.contains(board.getCell(1, 0)));
		Assert.assertTrue(testList.contains(board.getCell(0, 1)));
		Assert.assertEquals(2, testList.size());
	}

	// Tests getTarget with different movement lengths.

	@Test
	public void testTargetsNormal() {
		BoardCell cell = board.getCell(0, 0);
		board.calcTargets(cell, 3);
		Set<BoardCell> targets = board.getTargets();
		// testList.add(new BoardCell(4,4));
		Assert.assertEquals(6, targets.size());
		Assert.assertTrue(targets.contains(board.getCell(3, 0)));
		Assert.assertTrue(targets.contains(board.getCell(2, 1)));
		Assert.assertTrue(targets.contains(board.getCell(0, 1)));
		Assert.assertTrue(targets.contains(board.getCell(1, 2)));
		Assert.assertTrue(targets.contains(board.getCell(0, 3)));
		Assert.assertTrue(targets.contains(board.getCell(1, 0)));

	}

	// Tests getTargets with occupied cells.

	@Test
	public void testTargetsMixed() {
		board.getCell(0, 2).setOccupied(true);
		board.getCell(1, 2).setIsRoom(true);
		BoardCell cell = board.getCell(0, 3);
		board.calcTargets(cell, 3);
		Set<BoardCell> targets = board.getTargets();
		// testList.add(new BoardCell(4,4));
		Assert.assertEquals(3, targets.size());
		Assert.assertTrue(targets.contains(board.getCell(1, 2)));
		Assert.assertTrue(targets.contains(board.getCell(2, 2)));
		Assert.assertTrue(targets.contains(board.getCell(3, 3)));
	}

}
