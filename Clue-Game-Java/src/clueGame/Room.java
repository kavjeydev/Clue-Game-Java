package clueGame;

public class Room {
	
	private String name;
	private char label;
	
	private BoardCell centerCell;
	private BoardCell labelCell;

	public Room(String name, char label) {
		this.name = name;
		this.label = label;
	}
	
	/*
	 * return character associated with this room
	 */
	public char getLabel() {
		return label;
	}
	
	/*
	 * set cell associated with this room as the label location
	 */
	public void setLabelCell(BoardCell cell) {
		this.labelCell = cell;
	}
	
	/*
	 * return cell labeled as label for this room
	 */
	public BoardCell getLabelCell() {
		return labelCell;
	}
	
	/*
	 * set cell associated with this room as the center of the room
	 */
	public void setCenterCell(BoardCell cell) {
		this.centerCell = cell;
	}
	
	/*
	 * returns cell labeled as center of this room
	 */
	public BoardCell getCenterCell() {
		return centerCell;
	}

	/*
	 * returns name of this room
	 */
	public String getName() {
		return name;
	}
	
	
	
}
