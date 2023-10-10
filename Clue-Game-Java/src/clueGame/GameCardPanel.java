package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;


public class GameCardPanel extends JPanel{
	
	private JTextField name;
	JPanel peoplePanel = new JPanel();
	JPanel roomPanel = new JPanel();
	JPanel weaponPanel = new JPanel();
	
	static Board board = Board.getInstance();
	HumanPlayer human;
	
	public GameCardPanel() {
		human = (HumanPlayer) board.getPlayers().get(0);
		
		//set layouts for panels
		setBorder(new TitledBorder(new EtchedBorder(), "Known Cards"));
		setLayout(new GridLayout(3, 1));
		peoplePanel.setLayout(new GridLayout(0, 1));
		peoplePanel.setBorder(new TitledBorder(new EtchedBorder(), "People"));
		roomPanel.setLayout(new GridLayout(0, 1));
		roomPanel.setBorder(new TitledBorder(new EtchedBorder(), "Rooms"));
		weaponPanel.setLayout(new GridLayout(0, 1));
		weaponPanel.setBorder(new TitledBorder(new EtchedBorder(), "Weapons"));
		
		//add panels to this class
		add(peoplePanel);
		add(roomPanel);
		add(weaponPanel);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		//set up game
		board = Board.getInstance();
		board.setConfigFiles("data/ClueLayout.csv", "data/ClueSetup.txt");
		board.initialize();
		board.deal();
		
		GameCardPanel gui = new GameCardPanel();
		
		//main jframe
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("GUI Example");
		frame.setSize(200, 750);
		
		//add some random cpu's cards to seen list
		gui.human.updateSeen(board.getPlayers().get(1).getHand().get(0), board.getPlayers().get(1));
		gui.human.updateSeen(board.getPlayers().get(2).getHand().get(0), board.getPlayers().get(2));
		gui.human.updateSeen(board.getPlayers().get(3).getHand().get(0), board.getPlayers().get(3));
				
		//update panel data with card info
		gui.updatePanels();
		
		//make window visible
		frame.add(gui, BorderLayout.CENTER);
		frame.setVisible(true);
	}
	
	public void updatePanels() {
		updatePanel(CardType.ROOM, roomPanel);
		updatePanel(CardType.PERSON, peoplePanel);
		updatePanel(CardType.WEAPON, weaponPanel);
	}
	
	public void updatePanel(CardType type, JPanel panel) {
		//reset before adding everything
		panel.removeAll();
		
		panel.add(new JLabel("In Hand:"));
		
		//add all relevant cards from player hand
		for(int i = 0; i < human.getHand().size(); i++) {
			Card card = human.getHand().get(i);
			if(card.getType() == type) {
				JTextField cardText = new JTextField(card.getName());
				cardText.setBackground(Color.lightGray);
				cardText.setEditable(false);
				panel.add(cardText);
			}
		}
		
		panel.add(new JLabel("Seen:"));
		
		//add all relevant cards from seen list
		for(Card card : human.getSeen()) {
			Player from = human.whoIsCardFrom(card);
			if(card.getType() == type) {
				JTextField cardText = new JTextField(card.getName());
				cardText.setBackground(from.getColor());
				cardText.setEditable(false);
				panel.add(cardText);
			}
		}
	}
}
