package clueGame;

public class Solution {
	private Card person, weapon, room;
	
	public Solution(Card room, Card person, Card weapon) {
		this.person = person;
		this.weapon = weapon;
		this.room = room;
	}
	
	@Override
	public boolean equals(Object other) {
		return this.person.equals(((Solution) other).getPerson()) && this.room.equals(((Solution) other).getRoom())
				&& this.weapon.equals(((Solution) other).getWeapon());
	}

	public Card getPerson() {
		return person;
	}

	public Card getWeapon() {
		return weapon;
	}

	public Card getRoom() {
		return room;
	}
}
