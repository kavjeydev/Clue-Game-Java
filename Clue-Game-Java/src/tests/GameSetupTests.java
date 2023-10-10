package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Set;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.Player;
import clueGame.Solution;

public class GameSetupTests {
	// We make the Board static because we can load it one time and 
		// then do all the tests. 
		private static Board board;
		private static Card conservatory, kitchen, study, diningRoom, library, 
		lounge, hall, ballroom, billiard;
		private static Card scarlett, mustard, white, green, peacock, plum;
		private static Card revolver, dagger, pipe, rope, candleStick, wrench;
		
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
		}
		
		@Test
		public void TestPlayers() {
			ArrayList<Player> players = board.getPlayers();
			//make sure right number of players
			assertEquals(players.size(), 6);
			assertEquals(players.get(0).getIsHuman(), true);
			assertEquals(players.get(3).getIsHuman(), false);
			assertEquals(players.get(5).getIsHuman(), false);
			//test human player starting location
			assertEquals(players.get(0).getRow(), 5);
			assertEquals(players.get(0).getCol(), 3);
			//test cpu starting location
			assertEquals(players.get(4).getRow(), 8);
			assertEquals(players.get(4).getCol(), 8);
			//test another cpu starting location
			assertEquals(players.get(2).getRow(), 13);
			assertEquals(players.get(2).getCol(), 18);
		}
		
		@Test
		public void TestDeck() {
			ArrayList<Card> deck = board.getDeck();
			ArrayList<Player> players = board.getPlayers();
			ArrayList<String> tempdeck = new ArrayList<String>();
			
			for(Card card : deck) {
				
				if(!tempdeck.contains(card.getName())) {
					tempdeck.add(card.getName());					
				}
			}
			
			
			//make sure right number of cards in deck
			assertEquals(tempdeck.size(), 21);
			
			//make sure each player has 3 cards
			ArrayList<Card> hand = players.get(0).getHand();
			assertEquals(hand.size(), 3);
			hand = players.get(3).getHand();
			assertEquals(hand.size(), 3);
			
			//check solution has one of each type of card
			
			Solution answer = board.getAnswer();
			System.out.println(answer.getPerson());
			assertEquals(answer.getPerson().getType(), CardType.PERSON);
			assertEquals(answer.getWeapon().getType(), CardType.WEAPON);
			assertEquals(answer.getRoom().getType(), CardType.ROOM);
		}
}
