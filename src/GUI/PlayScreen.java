package GUI;

import cards.Card;
import com.sun.tools.javac.Main;
import controller.MainLoop;
import game.Game;
import players.Player;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Show the main game screen.
 */
public class PlayScreen {

    private boolean ifShowHandCards = false;
    private JFrame frame;

    // Components for game status
    JLabel colorAndCardLabel = new JLabel();
    JLabel currentPlayerLabel = new JLabel();
    JLabel stackLabel = new JLabel();

    // Components for handcard list
    JList<String> handcardJList = new JList<>();
    private JScrollPane handcardScroll = new JScrollPane();

    // Components for players list
    JScrollPane playerList = new JScrollPane();

    // Components for player actions
    private JButton toggle;
    private JButton playButton;
    private JButton skipButton;
    private JTextField idInput;
    private JTextField colorInput;

    /**
     * Hide or show this frame.
     * @param b boolean value used to determine hide or show.
     */
    public void setVisible(Boolean b) {
        this.frame.setVisible(b);
    }

    /**
     * Convert a player object to a string that contains information.
     * @param p the player passed in
     * @return player's name and number of cards he/she has.
     */
    private String playerToString(Player p) {
        return p.getPlayerName() + " has " + p.getHandCards().size() + " card(s).";
    }

    /**
     * Get the current color of the game and set it to the lower case.
     * @param g the current game
     * @return the game color
     */
    private String getGameColor(Game g) {
        if (g.getCurrentColor() == null) {
            return "No color yet.";
        } else {
            return g.getCurrentColor().name().toLowerCase();
        }
    }

    /**
     * Get the previous card.
     * @param g the current game
     * @return the card name
     */
    private String getGamePreviousCard(Game g) {
        if (g.getCurrentCard() == null) {
            return "No previous card yet.";
        } else {
            return g.getCurrentCard().getType().name();
        }
    }

    /**
     * Setup a list of players on the screen.
     * @param frame input frame.
     * @param g input game.
     */
    private void setupPlayersList(JFrame frame, Game g) {
        DefaultListModel<String> l1 = new DefaultListModel<>();
        for (Player p: g.getPlayers()) {
            l1.addElement(playerToString(p));
        }
        JList<String> list = new JList<>(l1);

        playerList.setColumnHeaderView(list);
        playerList.setBounds(10, 100, 150, 150);

        frame.getContentPane().add(playerList);
    }

    /**
     * Setup a list view of all hand cards of current player.
     * @param frame the input frame
     * @param g the current game
     */
    private void setupHandCardsList(JFrame frame, Game g) {
        DefaultListModel<String> list = new DefaultListModel<>();
        for (int i = 0; i < g.getCurrentPlayer().getHandCards().size(); i++) {
            Card c = g.getCurrentPlayer().getHandCards().get(i);
            list.addElement("id: " + (i+1) + " " + c.getColor().name().toLowerCase() + " " + c.getType().name());
        }
        handcardJList = new JList<>(list);

        handcardScroll.setColumnHeaderView(handcardJList);
        handcardScroll.setBounds(200, 100, 150, 150);
        handcardScroll.setVisible(ifShowHandCards);
        frame.getContentPane().add(handcardScroll);
    }

    /**
     * Setup several static texts.
     * @param frame the input frame.
     */
    private void setupInformationBar(JFrame frame) {
        JLabel playersListLabel = new JLabel("Status of all players:");
        playersListLabel.setBounds(10, 80, 150, 25);

        JLabel handCardListLabel = new JLabel("Hand cards of current player:");
        handCardListLabel.setBounds(200, 80, 200, 25);

        frame.add(playersListLabel);
        frame.add(handCardListLabel);
    }

    /**
     * Setup the component that handle user actions.
     * @param frame current frame.
     */
    private void setupCurrentPlayerAction(JFrame frame, MainLoop loop, Game g) {
        JPanel panel = new JPanel();
        panel.setBounds(10, 270, 450, 60);

        JLabel head = new JLabel("Play a card:   id: ");
        head.setSize(100, 25);
        panel.add(head);

        idInput = new JTextField(2);
        idInput.setSize(25, 25);
        panel.add(idInput);

        JLabel mid = new JLabel("color (if it is a wild card): ");
        mid.setSize(100, 25);
        panel.add(mid);

        colorInput = new JTextField(10);
        colorInput.setSize(50, 25);
        panel.add(colorInput);

        playButton = new JButton("Play it!");
        playButton.setSize(50, 25);
        setPlayListener(loop, g);
        panel.add(playButton);

        skipButton = new JButton("Skip/Get penalty");
        skipButton.setSize(80, 25);
        setSkipListener(loop, g);
        panel.add(skipButton);

        frame.add(panel);
    }

    private void refreshScreen(Game g) {
        ifShowHandCards = false;        // Refresh the play screen to show updates
        setupGameStatus(frame, g);
        setupHandCardsList(frame, g);
        setupPlayersList(frame, g);
    }

    private void setPlayListener(MainLoop loop, Game g) {
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loop.handlePlayInputs(idInput.getText(), colorInput.getText());
                refreshScreen(g);
            }
        });
    }

    private void setSkipListener(MainLoop loop, Game g) {
        skipButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loop.handleSkip(colorInput.getText());
                refreshScreen(g);
            }
        });
    }

    /**
     * Setup the button that hide/show the list of hand cards.
     * @param frame the current frame.
     */
    private void setupHideToggle(Frame frame) {
        toggle = new JButton("Hide/Reveal");
        toggle.setBounds(355, 120, 130, 25);
        setHideListener();
        frame.add(toggle);
    }

    private void setHideListener() {
        toggle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ifShowHandCards = !ifShowHandCards;
                handcardScroll.setVisible(ifShowHandCards);
            }
        });
    }

    /**
     * Setup the component that show the game status like current color etc.
     * @param frame the current frame.
     * @param g the current game.
     */
    private void setupGameStatus(Frame frame, Game g) {
        colorAndCardLabel.setText("Current color is: " + getGameColor(g) +
                "  Current card is: " + getGamePreviousCard(g));
        colorAndCardLabel.setBounds(10, 30, 400, 25);
        frame.add(colorAndCardLabel);

        currentPlayerLabel.setText("Current player is " + g.getCurrentPlayer().getPlayerName() + ".");
        currentPlayerLabel.setBounds(10, 10, 150, 25);
        frame.add(currentPlayerLabel);

        stackLabel.setText("Number of cards in the stack: " + g.getCardStack());
        stackLabel.setBounds(10, 50, 200, 25);
        frame.add(stackLabel);
    }

    /**
     * Set up the player screen by a game object.
     * @param  g The game we want to show.
     */
    public PlayScreen(Game g, MainLoop loop) {
        // Create and set up the window.
        frame = new JFrame("Game progressing");
        frame.getContentPane().setLayout(null);

        // Setup each components
        setupInformationBar(frame);
        setupCurrentPlayerAction(frame, loop, g);
        setupHandCardsList(frame, g);
        setupPlayersList(frame, g);
        setupHideToggle(frame);
        setupGameStatus(frame, g);

        // Display the window.
        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {

    }
}