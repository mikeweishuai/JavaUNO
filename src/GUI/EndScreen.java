package GUI;

import players.Player;

import javax.swing.*;

public class EndScreen {

    /**
     * Show the game over page.
     * @param p the winner
     */
    public EndScreen(Player p) {
        JFrame frame = new JFrame();

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel label = new JLabel("The winner is: " + p.getPlayerName() + " !");
        label.setBounds(190, 20, 150, 25);
        panel.add(label);

        JButton button = new JButton("Restart!");
        button.setBounds(190, 60, 100, 25);
        panel.add(button);

        frame.setSize(500, 500);
        frame.setTitle("Game over");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.add(panel);
    }

    public static void main(String[] args) {

    }
}
