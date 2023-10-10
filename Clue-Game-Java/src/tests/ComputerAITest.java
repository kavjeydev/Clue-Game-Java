package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;
import clueGame.Solution;

public class ComputerAITest {
	private static Board board;
	private static Card conservatory, kitchen, study, diningRoom, library, 
	lounge, hall, ballroom, billiard;
	private static Card scarlett, mustard, white, green, peacock, plum;
	private static Card revolver, dagger, pipe, rope, candleStick, wrench;
	static Player computer;
	
	@BeforeAll
	public static void setUp() {
		//put together deck
		conservatory = new Card("Conservatory", CardType.ROOM);
		kitchen = new Card("Kitchen", CardType.ROOM);
		study = new Card("Study", CardType.ROOM);
		diningRoom = new Card("Dining Room", CardType.ROOM);
		library = new Card("Library", CardType.ROOM);
		lounge = new Card("Lounge", CardType.ROOM);
		hall = new Card("Hall", CardType.ROOM);
		ballroom = new Card("Ballroom", CardType.ROOM);
		billiard = new Card("Billiard", CardType.ROOM);
		
		scarlett = new Card("Scarlett", CardType.PERSON);
		mustard = new Card("Mustard", CardType.PERSON);
		white = new Card("White", CardType.PERSON);
		green = new Card("Green", CardType.PERSON);
		plum = new Card("Plum", CardType.PERSON);
		peacock = new Card("Peacock", CardType.PERSON);
		
		revolver = new Card("Revolver", CardType.WEAPON);
		dagger = new Card("Dagger", CardType.WEAPON);
		pipe = new Card("Pipe", CardType.WEAPON);
		rope = new Card("Rope", CardType.WEAPON);
		candleStick = new Card("Candle Stick", CardType.WEAPON);
		wrench = new Card("Wrench", CardType.WEAPON);
		
		
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("data/ClueLayout.csv", "data/ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();
		
		board.addToDeck(conservatory);
		board.addToDeck(kitchen);
		board.addToDeck(study);
		board.addToDeck(diningRoom);
		board.addToDeck(library);
		board.addToDeck(lounge);
		board.addToDeck(hall);
		board.addToDeck(ballroom);
		board.addToDeck(billiard);
		board.addToDeck(scarlett);
		board.addToDeck(mustard);
		board.addToDeck(white);
		board.addToDeck(green);
		board.addToDeck(plum);
		board.addToDeck(peacock);
		board.addToDeck(revolver);
		board.addToDeck(dagger);
		board.addToDeck(pipe);
		board.addToDeck(rope);
		board.addToDeck(candleStick);
		board.addToDeck(wrench);
		
		
		board.deal();
		
		computer = new ComputerPlayer("name", Color.CYAN);
		computer.updateSeen(billiard);
		computer.updateSeen(dagger);
		computer.updateSeen(green);
		
	}
	
	@Test
	public void testAISuggestion() {
		
		//makes sure none of the suggestions are in the seen deck.
		computer.setRow(11);
		computer.setCol(3);
		
		assertNotEquals(computer.createSuggestion().getPerson(), green);
		assertNotEquals(computer.createSuggestion().getWeapon(), dagger);
		assertNotEquals(computer.createSuggestion().getRoom(), billiard);
		
		assertEquals(board.getRoom(board.getCell(computer.getRow(), 
				computer.getCol())).getName(), "Library");
		
		assertTrue(computer.createSuggestion().getRoom().equals(library));
	}
	
	
	@Test
	public void testAITarget() {
		//try space near a few rooms with two of the three rooms already seen or in hand
		computer.setRow(5);
		computer.setCol(10);
		computer.updateHand(hall);
		computer.updateHand(pipe);
		computer.updateHand(mustard);
		computer.updateSeen(library);
		
		//calc targets for a roll of 6 for computerg
		board.calcTargets(board.getCell(computer.getRow(), computer.getCol()), 6);
		
		//should target the room
		BoardCell target = computer.selectTarget();
		System.out.println(target.getLabel());
		assertEquals(target, board.getCell(2, 3));
	}

}
