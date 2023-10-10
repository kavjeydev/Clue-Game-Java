package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class ComputerPlayer extends Player {

	Board board = Board.getInstance();
	Solution lastSuggestion;

	public ComputerPlayer(String name, Color color) {
		super(name, color);
	}

	public boolean getIsHuman() {
		return false;
	}

	@Override
	public Solution createSuggestion(Card room, Card person, Card weapon) {
		lastSuggestion = new Solution(room, person, weapon);
		return lastSuggestion;
	}

	@Override
	public Solution createSuggestion() {
		// get current room
		Room currentRoom = board.getRoom(board.getCell(getRow(), getCol()));
		Card room = new Card(currentRoom.getName(), CardType.ROOM);
		ArrayList<Card> weapons = new ArrayList<Card>();
		ArrayList<Card> persons = new ArrayList<Card>();

		// add valid card selections to lists
		for (Card c : board.getDeck()) {
			if (!super.checkCard(c)) {
				if (c.getType() == CardType.WEAPON)
					weapons.add(c);
				else if (c.getType() == CardType.PERSON)
					persons.add(c);
				else
					continue;
			}
		}

		// choose random weapon and person
		Random rand = new Random();
		int randPerson = rand.nextInt(persons.size());
		int randWeapon = rand.nextInt(weapons.size());

		lastSuggestion =  new Solution(room, persons.get(randPerson), weapons.get(randWeapon));
		return lastSuggestion;
	}

	public BoardCell selectTarget() {
		Random rand = new Random();
		ArrayList<BoardCell> targets = new ArrayList<BoardCell>();
		targets.addAll(board.getTargets());

		for (BoardCell cell : targets) {
			if (cell.getIsRoom() && !checkCard(new Card(Board.getInstance().getRoom(cell).getName(), CardType.ROOM))) {
				return cell;
			}
		}

		if(targets.size() != 0) {
			int randomTarget = rand.nextInt(targets.size());
			return targets.get(randomTarget);
		} else {
			return getCell();
		}
	}

	public Solution getLastSuggestion() {
		return lastSuggestion;
	}

}
