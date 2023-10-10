package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicButtonListener;

public class GameControlPanel extends JPanel {
	
	JPanel turnInfoContainer = new JPanel();
	JPanel turnContainer = new JPanel();
	JLabel whoseTurnLabel = new JLabel("Whose Turn?");
	JTextField whoseTurn = new JTextField();
	JPanel rollContainer = new JPanel();
	JLabel rollLabel = new JLabel("Roll:");
	JTextField rollNumber = new JTextField();
	JPanel guessInfoContainer = new JPanel();
	JPanel guessContainer = new JPanel();
	JPanel guessResultContainer = new JPanel();
	JTextField guess = new JTextField();
	JTextField guessResult = new JTextField();
	
	boolean accuse = false;
	
	private static Board board = Board.getInstance();

	/**
	 * Constructor for the panel, it does 90% of the work
	 */
	public GameControlPanel()  {
		setLayout(new GridLayout(2, 0));
		turnInfoContainer.setLayout(new GridLayout(1, 4));
		turnContainer.setLayout(new GridLayout(2, 0));
		rollContainer.setLayout(new GridLayout(0, 2));
		guessInfoContainer.setLayout(new GridLayout(0, 2));
		guessContainer.setLayout(new GridLayout(1, 0));
		guessResultContainer.setLayout(new GridLayout(1, 0));
		guessContainer.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));
		guessResultContainer.setBorder(new TitledBorder(new EtchedBorder(), "Guess Result"));
		
		turnContainer.add(whoseTurnLabel, BorderLayout.NORTH);
		turnContainer.add(whoseTurn, BorderLayout.SOUTH);
		
		turnInfoContainer.add(turnContainer, BorderLayout.WEST);
		
		rollContainer.add(rollLabel);
		rollContainer.add(rollNumber);
		
		rollNumber.setEditable(false);
		whoseTurn.setEditable(false);
		guess.setEditable(false);
		guessResult.setEditable(false);
		
		turnInfoContainer.add(rollContainer);
		
		JButton accuseButton = new JButton();
		accuseButton.setText("Make Accusation");
		turnInfoContainer.add(accuseButton, BorderLayout.EAST);
		
		JButton nextButton = new JButton();
		nextButton.setText("NEXT");
		turnInfoContainer.add(nextButton, BorderLayout.EAST);
		
		nextButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				    nextButtonPressed();
			  };
		});
		
		accuseButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				    accuseButtonPressed();
			  };
		});
		
		guessResultContainer.add(guessResult);
		guessContainer.add(guess);
		
		guessInfoContainer.add(guessContainer);
		guessInfoContainer.add(guessResultContainer);
		
		add(turnInfoContainer);
		add(guessInfoContainer);
	}
	
	
	private void nextButtonPressed() {
		if((board.getCurrentPlayer().getIsHuman() && board.getPlayerHasMoved()) ||
				!board.getCurrentPlayer().getIsHuman()) {
			board.nextTurn();
			updateTurnGUI();
		} else if(!board.getPlayerHasMoved()) {
			JOptionPane.showMessageDialog(null, "You need to move first.");
		} else {
			JOptionPane.showMessageDialog(null, "Cannot move on yet.");
		}
	}
	
	public void accuseButtonPressed() {
		if(board.getCurrentPlayer().getIsHuman() && !board.getPlayerHasMoved()) {
			board.accuse();
		} else {
			JOptionPane.showMessageDialog(null, "Cannot accuse right now.");
		}
	}


	/**
	 * Main to test the panel
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		GameControlPanel panel = new GameControlPanel();  // create the panel
		JFrame frame = new JFrame();  // create the frame 
		frame.setContentPane(panel); // put the panel in the frame
		frame.setSize(750, 180);  // size the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		frame.setVisible(true); // make it visible
		
		// test filling in the data
		panel.setGuess( "I have no guess!");
		panel.setGuessResult( "So you have nothing?");

	}

	public boolean accusePressed() {
		return accuse;
	}
	
	public void setAccuse(boolean accused) {
		accuse = accused;
	}
	
	
	public void setGuessResult(String string) {
		guessResult.setText(string);	
	}


	public void setGuess(String string) {
		guess.setText(string);
	}


	public void updateTurnGUI() {
		whoseTurn.setText(board.getCurrentPlayer().getName());
		rollNumber.setText(Integer.toString(board.getCurrentRoll()));
	}
}