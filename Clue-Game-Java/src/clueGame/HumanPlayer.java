package clueGame;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class HumanPlayer extends Player{
	private Map<Card, Player> cardSource;
	
	public HumanPlayer(String name, Color color) {
		super(name, color);
		cardSource = new HashMap<Card, Player>();
	}
	
	public boolean getIsHuman() {
		return true;
	}
	
	public Player whoIsCardFrom(Card card) {
		return cardSource.get(card);
	}
	
	@Override
	public void updateHand(Card card) {
		super.updateHand(card);
		cardSource.put(card, this);
	}
	
	public void updateSeen(Card card, Player from) {
		super.updateSeen(card);
		cardSource.put(card, from);
	}

	@Override
	public Solution createSuggestion(Card room, Card person, Card weapon) {
		return new Solution(room, person, weapon);
	}

	@Override
	public Solution createSuggestion() {
		return new Solution(new Card("room", CardType.ROOM), new Card("person", CardType.PERSON)
				, new Card("weapon", CardType.WEAPON));
	}

	@Override
	public BoardCell selectTarget() {
		return new BoardCell(2, 2);
	}

}
