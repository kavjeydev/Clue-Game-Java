package tests;

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class BoardAdjTargetTest {
	// We make the Board static because we can load it one time and 
	// then do all the tests. 
	private static Board board;
	
	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("data/ClueLayout.csv", "data/ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();
	}

	// Ensure that player does not move around within room
	// These cells are LIGHT ORANGE on the planning spreadsheet
	@Test
	public void testAdjacenciesRooms()
	{	
		//test Observatory (has a secret passage)
		Set<BoardCell> testList = board.getAdjList(4, 21);
		assertEquals(testList.size(), 2);
		assertTrue(testList.contains(board.getCell(3, 18)));
		assertTrue(testList.contains(board.getCell(2, 3)));
		
		//test Library
		testList = board.getAdjList(10, 3);	
		assertEquals(testList.size(), 3);
		assertTrue(testList.contains(board.getCell(9, 9)));
		assertTrue(testList.contains(board.getCell(10, 9)));
		assertTrue(testList.contains(board.getCell(14, 2)));
	}

	
	// Ensure door locations include their rooms and also additional walkways
	// These cells are LIGHT ORANGE on the planning spreadsheet
	@Test
	public void testAdjacencyDoor()
	{
		//test doorway to library
		Set<BoardCell> testList = board.getAdjList(14, 2);
		assertEquals(testList.size(), 4);
		assertTrue(testList.contains(board.getCell(14, 3)));
		assertTrue(testList.contains(board.getCell(14, 1)));
		assertTrue(testList.contains(board.getCell(15, 2)));
		assertTrue(testList.contains(board.getCell(10, 3)));
		
		//test doorway to kitchen
		testList = board.getAdjList(14, 2);
		assertEquals(testList.size(), 4);
		assertTrue(testList.contains(board.getCell(14, 3)));
		assertTrue(testList.contains(board.getCell(14, 1)));
		assertTrue(testList.contains(board.getCell(15, 2)));
		assertTrue(testList.contains(board.getCell(10, 3)));
	}
	
	// Test a variety of walkway scenarios
	// These tests are Dark Orange on the planning spreadsheet
	@Test
	public void testAdjacencyWalkways()
	{
		// Test random middle space
		Set<BoardCell> testList = board.getAdjList(15, 9);
		assertEquals(testList.size(), 4);
		assertTrue(testList.contains(board.getCell(15, 10)));
		assertTrue(testList.contains(board.getCell(15, 8)));
		assertTrue(testList.contains(board.getCell(16, 9)));
		assertTrue(testList.contains(board.getCell(14, 9)));
		
		// Test top edge space
		testList = board.getAdjList(0, 7);
		assertEquals(testList.size(), 2);
		assertTrue(testList.contains(board.getCell(0, 8)));
		assertTrue(testList.contains(board.getCell(1, 7)));
		
		// Test middle space by unused space
		testList = board.getAdjList(14, 16);
		assertEquals(testList.size(), 3);
		assertTrue(testList.contains(board.getCell(15, 16)));
		assertTrue(testList.contains(board.getCell(14, 17)));
		assertTrue(testList.contains(board.getCell(13, 16)));
			
	}
	
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsInKitchen() {
		// test a roll of 1
		board.calcTargets(board.getCell(21, 15), 1);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(targets.size(), 3);
		assertTrue(targets.contains(board.getCell(17, 17)));
		assertTrue(targets.contains(board.getCell(18, 12)));
		assertTrue(targets.contains(board.getCell(20, 1)));
		
		// test a roll of 3
		board.calcTargets(board.getCell(21, 15), 3);
		targets = board.getTargets();
		assertEquals(targets.size(), 10);
		assertTrue(targets.contains(board.getCell(20, 1)));
		assertTrue(targets.contains(board.getCell(20, 12)));
		assertTrue(targets.contains(board.getCell(19, 11)));
		assertTrue(targets.contains(board.getCell(17, 11)));
		assertTrue(targets.contains(board.getCell(17, 13)));
		assertTrue(targets.contains(board.getCell(16, 16)));
		assertTrue(targets.contains(board.getCell(15, 17)));
		assertTrue(targets.contains(board.getCell(16, 18)));
		assertTrue(targets.contains(board.getCell(18, 18)));
		assertTrue(targets.contains(board.getCell(17, 15)));
			
	}

	// Tests out of room center, 1, 3 and 4
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsAtDoor() {
		// test a roll of 1
		board.calcTargets(board.getCell(3, 18), 1);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(targets.size(), 4);
		assertTrue(targets.contains(board.getCell(4, 21)));
		assertTrue(targets.contains(board.getCell(3, 17)));
		assertTrue(targets.contains(board.getCell(4, 18)));
		assertTrue(targets.contains(board.getCell(2, 18)));
		
		// test a roll of 2
		board.calcTargets(board.getCell(3, 18), 2);
		targets = board.getTargets();
		assertEquals(targets.size(), 6);
		assertTrue(targets.contains(board.getCell(4, 21)));
		assertTrue(targets.contains(board.getCell(5, 18)));
		assertTrue(targets.contains(board.getCell(4, 17)));
		assertTrue(targets.contains(board.getCell(2, 17)));
		assertTrue(targets.contains(board.getCell(1, 18)));
		assertTrue(targets.contains(board.getCell(4, 14)));
	}

	@Test
	public void testTargetsInWalkway1() {
		// test a roll of 1
		board.calcTargets(board.getCell(15, 18), 1);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(targets.size(), 3);
		assertTrue(targets.contains(board.getCell(14, 18)));
		assertTrue(targets.contains(board.getCell(16, 18)));
		assertTrue(targets.contains(board.getCell(15, 17)));
		
		//test roll of 3
		board.calcTargets(board.getCell(15, 18), 3);
		targets = board.getTargets();
		assertEquals(targets.size(), 12);
		assertTrue(targets.contains(board.getCell(17, 21)));
		assertTrue(targets.contains(board.getCell(12, 18)));
		assertTrue(targets.contains(board.getCell(13, 19)));
		assertTrue(targets.contains(board.getCell(14, 20)));
		assertTrue(targets.contains(board.getCell(13, 17)));
		assertTrue(targets.contains(board.getCell(14, 16)));
		assertTrue(targets.contains(board.getCell(16, 16)));
		assertTrue(targets.contains(board.getCell(17, 17)));
		assertTrue(targets.contains(board.getCell(18, 18)));
		assertTrue(targets.contains(board.getCell(13, 19)));
	}

	@Test
	// test to make sure occupied locations do not cause problems
	public void testTargetsOccupied() {
		// test a roll of 2 blocked 1 down in a doorway
		board.getCell(11, 9).setOccupied(true);
		board.calcTargets(board.getCell(10, 9), 2);
		board.getCell(11, 9).setOccupied(false);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(targets.size(), 4);
		assertTrue(targets.contains(board.getCell(11, 10)));
		assertTrue(targets.contains(board.getCell(9, 10)));
		assertTrue(targets.contains(board.getCell(8, 9)));	
		assertTrue(targets.contains(board.getCell(10, 3)));
	
		//test roll of 2 with all spaces around blocked but in a doorway
		board.getCell(17, 3).setOccupied(true);
		board.getCell(18, 4).setOccupied(true);
		board.getCell(19, 3).setOccupied(true);
		board.calcTargets(board.getCell(18, 3), 2);
		board.getCell(17, 3).setOccupied(false);
		board.getCell(18, 4).setOccupied(false);
		board.getCell(19, 3).setOccupied(false);
		targets = board.getTargets();
		System.out.println(targets.size());
		assertEquals(targets.size(), 1);
		assertTrue(targets.contains(board.getCell(20, 1)));	
		
		//test roll of 4 with a blocked doorway and 2 below
		board.getCell(7, 17).setOccupied(true);
		board.getCell(7, 18).setOccupied(true);
		board.getCell(3, 17).setOccupied(true);
		board.calcTargets(board.getCell(6, 17), 4);
		board.getCell(7, 17).setOccupied(false);
		board.getCell(3, 17).setOccupied(false);
		targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(4, 17)));
		assertTrue(targets.contains(board.getCell(5, 18)));
		assertTrue(targets.contains(board.getCell(3, 18)));	

	}
}
