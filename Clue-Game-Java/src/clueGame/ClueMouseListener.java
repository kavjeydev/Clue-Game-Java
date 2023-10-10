package clueGame;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ClueMouseListener implements MouseListener {
	
	private static Board board = Board.getInstance();
	
	@Override
	public void mouseClicked(MouseEvent e) {
		board.handleBoardClicked(e.getX(), e.getY());
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
	
}
