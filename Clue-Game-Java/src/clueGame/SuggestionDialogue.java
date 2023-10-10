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

public class SuggestionDialogue extends JFrame {
	JLabel roomLabel, personLabel, weaponLabel;
	JComboBox<String> rooms, people, weapons;
	JButton submitButton;
	static Board board = Board.getInstance();
	
	public SuggestionDialogue(ArrayList<Card> deck, Room room) {
		setLayout(new GridLayout(4, 2));
		setSize(300,200);
		
		roomLabel = new JLabel("Room");
		personLabel = new JLabel("Person");
		weaponLabel = new JLabel("Weapon");
		
		setComboBoxes(deck, room);
		
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
		
		setVisible(true);
	}
	
	protected void submitButtonPressed() {
		board.makeHumanSuggestion(new Solution((Card)rooms.getSelectedItem(),
				(Card)people.getSelectedItem(), (Card)weapons.getSelectedItem()));
		dispose();
	}
	

	public void setComboBoxes(ArrayList<Card> deck, Room room) {
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
		
		rooms = new JComboBox(new Card[] {new Card(room.getName(), CardType.ROOM)});
		people = new JComboBox(personList);
		weapons = new JComboBox(weaponList);
	}
}
