package clueGame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AccuseDialogue extends JFrame {
	JLabel roomLabel, personLabel, weaponLabel;
	JComboBox<String> rooms, people, weapons;
	JButton submitButton, cancelButton;
	static Board board = Board.getInstance();
	
	public AccuseDialogue(ArrayList<Card> deck) {
		setLayout(new GridLayout(4, 2));
		setSize(300,200);
		
		roomLabel = new JLabel("Room");
		personLabel = new JLabel("Person");
		weaponLabel = new JLabel("Weapon");
		
		setComboBoxes(deck);
		
		add(roomLabel, BorderLayout.WEST);
		add(rooms, BorderLayout.EAST);
		add(personLabel, BorderLayout.WEST);
		add(people, BorderLayout.EAST);
		add(weaponLabel, BorderLayout.WEST);
		add(weapons, BorderLayout.EAST);
		
		submitButton = new JButton();
		submitButton.setText("SUBMIT");
		add(submitButton, BorderLayout.WEST);
		
		submitButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  submitButtonPressed();
			  };
		});
		
		cancelButton = new JButton();
		cancelButton.setText("CANCEL");
		add(cancelButton, BorderLayout.EAST);
		
		cancelButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  dispose();
			  }
		});
		
		setVisible(true);
	}
	
	protected void submitButtonPressed() {
		Board board = Board.getInstance();
		
		Card room = (Card) rooms.getSelectedItem();
		Card person = (Card) people.getSelectedItem();
		Card weapon = (Card) weapons.getSelectedItem();
		
		
		if(board.checkAccusation(new Solution(room, person, weapon))) {
			board.winGame();
		}
		else {
			board.loseGame();
		}
		
		dispose();
	}
	

	public void setComboBoxes(ArrayList<Card> deck) {
		//combo boxes can be built with vectors, but not arraylists
		Vector<Card> roomList = new Vector<Card>();
		Vector<Card> personList = new Vector<Card>();
		Vector<Card> weaponList = new Vector<Card>();
		for(Card card : deck) {
			if(card.getType() == CardType.ROOM) {
				roomList.add(card);
			} else if(card.getType() == CardType.PERSON) {
				personList.add(card);
			} else if(card.getType() == CardType.WEAPON) {
				weaponList.add(card);
			}
		}
		rooms = new JComboBox(roomList);		
		people = new JComboBox(personList);
		weapons = new JComboBox(weaponList);
	}
}