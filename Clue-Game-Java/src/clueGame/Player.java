package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public abstract class Player {
	private String name;
	private Color color;
	private int row, col;
	private ArrayList<Card> hand;
	private Set<Card> seen;
	private boolean readyToAccuse;
	boolean hasLost;
	
	Board board = Board.getInstance();
	
	public abstract Solution createSuggestion(Card room, Card person, Card weapon);
	
	public abstract Solution createSuggestion();
	
	public Player(String name, Color color) {
		seen = new HashSet<Card>();
		hand = new ArrayList<Card>();
		this.name = name;
		this.color = color;
		this.hasLost = false;
	}
	
	//Draws players on the game board.
	public void draw(Graphics g, int width, int height) {
		if(hasLost)
			return;
		Board board = Board.getInstance();
		int cellHeight = height/Board.NUM_ROWS;
		int cellWidth = width/Board.NUM_ROWS;
		int roomOffset = 0;
		
		for(Player p : board.getPlayers()) {
			if(p == this)
				break;
			if(p.getCell().equals(getCell())) {
				roomOffset += 5;
			}
		}
		
		g.setColor(color);
		g.fillOval(col*cellWidth+roomOffset, row*cellHeight, cellWidth, cellHeight);
		g.setColor(Color.black);
		g.drawOval(col*cellWidth+roomOffset, row*cellHeight, cellWidth, cellHeight);
	}
	
	public void updateHand(Card card) {
		hand.add(card);
	}
	
	public String getName() {
		return name;
	}

	public abstract boolean getIsHuman();

	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}

	public ArrayList<Card> getHand() {
		return hand;
	}
	
	public void setRow(int row) {
		this.row = row;
	}
	
	public void setCol(int col) {
		this.col = col;
	}
	
	public String toString() {
		return name + (getIsHuman() ? ", human player" : ", computer player");
	}
	
	public Card disproveSuggestion(Solution sol) {
		ArrayList<Card> matching = new ArrayList<Card>();
		
		for(int i = 0; i < hand.size(); i++) {
			if(sol.getPerson().equals(hand.get(i))) {
				matching.add(hand.get(i));
			}
			else if(sol.getWeapon().equals(hand.get(i))) {
				matching.add(hand.get(i));
			}
			else if(sol.getRoom().equals(hand.get(i))) {
				matching.add(hand.get(i));
			}
		}

		
		if(matching.size() == 0) {
			return null;
		}
		
		if(matching.size() == 1) {

			return matching.get(0);
		}
		
		Random rand = new Random();
		int randNum = rand.nextInt(matching.size());
		
		return matching.get(randNum);
	}
	
	public void updateSeen(Card card) {
		seen.add(card);
	}
	
	public Set<Card> getSeen() {
		return seen;
	}

	public abstract BoardCell selectTarget();

	public Color getColor() {
		return color;
	}
	
	public BoardCell getCell() {
		return board.getCell(row, col);
	}

	public void setCell(BoardCell cell) {
		board.getCell(row, col).setOccupied(false);
		row = cell.getRow();
		col = cell.getCol();
		cell.setOccupied(true);
	}

	public boolean isReadyToAccuse() {
		return readyToAccuse;
	}

	public boolean checkCard(Card c) {
		return hand.contains(c) && seen.contains(c);
	}

	public void setIsReadyToAccuse(boolean b) {
		readyToAccuse = b;
	}

	public boolean getHasLost() {
		return hasLost;
	}

	public void setHasLost(boolean b) {
		hasLost = b;
	}
}