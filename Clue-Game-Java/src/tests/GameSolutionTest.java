package tests;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.ArrayList;

import static org.junit.Assert.assertFalse;


import org.junit.jupiter.api.BeforeAll;

import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;
import clueGame.Solution;

public class GameSolutionTest {
	
	private static Board board;
	private static Card conservatory, kitchen, study, diningRoom, library, 
	lounge, hall, ballroom, billiard;
	private static Card scarlett, mustard, white, green, peacock, plum;
	private static Card revolver, dagger, pipe, rope, candleStick, wrench;
	
	static Player human;
	static Player AI1;
	static Player AI2;
	
	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
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
		
		human = new HumanPlayer("Human", Color.BLACK);
		AI1 = new ComputerPlayer("AI1", Color.GREEN);
		AI2 = new ComputerPlayer("AI2", Color.BLUE);
		
		human.updateHand(study);
		human.updateHand(mustard);
		human.updateHand(pipe);
		
		AI1.updateHand(hall);
		AI1.updateHand(green);
		AI1.updateHand(rope);
		
		AI2.updateHand(billiard);
		AI2.updateHand(white);
		AI2.updateHand(dagger);
		
		
		
	}
	
	@Test
	public void testAccusation() {
		board.setAnswer(study, mustard, dagger);
		//returns true for the solution
		assertTrue(board.checkAccusation(new Solution(study, mustard, dagger)));
		//returns false as these are not the solution
		assertFalse(board.checkAccusation(new Solution(study, mustard, pipe)));
		assertFalse(board.checkAccusation(new Solution(study, green, dagger)));
		assertFalse(board.checkAccusation(new Solution(ballroom, mustard, dagger)));
		assertFalse(board.checkAccusation(new Solution(hall, white, rope)));
	}
	
	@Test
	public void testDisproveSuggestion() {
		int room = 0;
		int person = 0;
		int weapon = 0;
		Player player = new HumanPlayer("Scarlett", Color.GRAY);
		player.updateHand(study);
		player.updateHand(green);
		player.updateHand(rope);
		
		//Returns the one card that matches with the players hand.
		assertEquals(player.disproveSuggestion(new Solution(study, white, pipe)), study);
		assertEquals(player.disproveSuggestion(new Solution(hall, green, pipe)), green);
		assertEquals(player.disproveSuggestion(new Solution(hall, white, rope)), rope);
		//Test no matching cards.
		assertEquals(player.disproveSuggestion(new Solution(ballroom, white, pipe)), null);
		
		for(int i = 0; i < 300; i++) {
			if(player.disproveSuggestion(new Solution(study, green, rope)) == rope) {
				weapon++;
			}
			else if(player.disproveSuggestion(new Solution(study, green, rope)) == green) {
				person++;
			}
			else if(player.disproveSuggestion(new Solution(study, green, rope)) == study) {
				room++;
			}
		}
		
		assertTrue(weapon >= 1);
		assertTrue(room >= 1);
		assertTrue(person >= 1);
	}
	
	@Test
	public void testSuggestionMade() {		
		Solution suggestion = human.createSuggestion(library, plum, revolver);
		
		//no players can disprove.
		assertEquals(board.handleSuggestionCard(suggestion, human), null);
		
		suggestion = human.createSuggestion(study, plum, revolver);
		//only accusing player can disprove.
		assertEquals(board.handleSuggestionCard(suggestion, human), null);
		
		suggestion = human.createSuggestion(hall, plum, revolver);
		//only AI1 can disprove.
		assertEquals(board.handleSuggestionCard(suggestion, human), hall);
		
		suggestion = AI1.createSuggestion(billiard, plum, revolver);
		//only AI2 can disprove.
		assertEquals(board.handleSuggestionCard(suggestion, AI1), billiard);
		
		suggestion = AI1.createSuggestion(hall, green, rope);
		//only AI1 can disprove.
		assertEquals(board.handleSuggestionCard(suggestion, AI1), null);

		suggestion = AI2.createSuggestion(billiard, white, dagger);
		//only AI2 can disprove.
		assertEquals(board.handleSuggestionCard(suggestion, AI2), null);
				
		suggestion = AI2.createSuggestion(study, green, dagger);
		//only human can disprove.
		assertEquals(board.handleSuggestionCard(suggestion, AI2), study);
	}
}
