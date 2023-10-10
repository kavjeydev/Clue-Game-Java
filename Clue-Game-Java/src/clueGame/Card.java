package clueGame;

public class Card {
	private String name;
	private CardType type;
	
	public Card(String name, CardType type) {
		this.name = name;
		this.type = type;
	}
	
	@Override
	public boolean equals(Object other) {
		return name.equals(((Card) other).getName());
	}
	
	public CardType getType() {
		return type;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
