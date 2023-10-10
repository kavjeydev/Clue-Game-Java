package clueGame;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ClueGame extends JFrame {
	static Board board;
	static ClueGame game = getInstance();
	GameControlPanel controlPanel;
	GameCardPanel cardPanel;
	
	public ClueGame() {
		super();
	}
	
	public static ClueGame getInstance() {
		if(game == null) {
			game = new ClueGame();
			return game;
		} else {
			return game;
		}
	}
	
	public void initialize() {
		controlPanel = new GameControlPanel();
		cardPanel = new GameCardPanel();
		
		setSize(720, 800);
				
		add(board, BorderLayout.CENTER);
		add(cardPanel, BorderLayout.EAST);
		add(controlPanel, BorderLayout.SOUTH);
	}
	
	public static void main(String[] args) {
		//set up game
		board = Board.getInstance();
		board.setConfigFiles("data/ClueLayout.csv", "data/ClueSetup.txt");
		board.initialize();
		ClueGame game = ClueGame.getInstance();
		game.initialize();
		game.cardPanel.updatePanels();
		
		game.setVisible(true);
		
		JOptionPane.showMessageDialog(game, "You are " + board.getPlayers().get(0).getName()
				+".\nCan you find the solution\n"+ "before the Computer Players?", 
				"Welcome to Clue", JOptionPane.INFORMATION_MESSAGE);
		
			
		board.doTurn();
		game.updateGUI();
	}
	
	public void updateGUI() {
		controlPanel.updateTurnGUI();
		cardPanel.updatePanels();
		setVisible(true);
	}
	
}
