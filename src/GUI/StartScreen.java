package GUI;

import controller.MainLoop;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartScreen {

    JFrame frame;
    JButton button;
    JTextField inputText;
    JLabel label;

    JLabel basicAILabel;
    JTextField basicAIInput;

    JLabel advancedAILabel;
    JTextField advancedAIInput;

    /**
     * Hide or show this frame.
     * @param b boolean value used to determine hide or show.
     */
    public void setVisible(Boolean b) {
        this.frame.setVisible(b);
    }

    private void setupPlayerNumberInput(JPanel panel) {
        label = new JLabel("Number of players: ");
        label.setBounds(10, 20, 150, 25);
        panel.add(label);

        inputText = new JTextField(10);
        inputText.setBounds(130, 20, 50, 25);
        panel.add(inputText);
    }

    private void setupBasicAINumberInput(JPanel panel) {
        basicAILabel = new JLabel("Number of basic AIs: ");
        basicAILabel.setBounds(10, 50, 150, 25);
        panel.add(basicAILabel);

        basicAIInput = new JTextField(10);
        basicAIInput.setBounds(130, 50, 50, 25);
        panel.add(basicAIInput);
    }

    private void setupAdvancedAINumberInput(JPanel panel) {
        advancedAILabel = new JLabel("Number of advanced AIs: ");
        advancedAILabel.setBounds(10, 80, 150, 25);
        panel.add(advancedAILabel);

        advancedAIInput = new JTextField(10);
        advancedAIInput.setBounds(130, 80, 50, 25);
        panel.add(advancedAIInput);
    }

    /**
     * Show the start screen with some game configurations.
     */
    public StartScreen(MainLoop loop) {

        frame = new JFrame();
        JPanel panel = new JPanel();
        panel.setLayout(null);

        setupPlayerNumberInput(panel);
        setupBasicAINumberInput(panel);
        setupAdvancedAINumberInput(panel);

        button = new JButton("Start!");
        button.setBounds(80, 120, 100, 25);
        setStartListener(loop);
        panel.add(button);

        frame.setSize(500, 500);
        frame.setTitle("Game start");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.add(panel);
    }

    /**
     * Setting up action listener for the start button
     * @param loop the game loop
     */
    private void setStartListener(MainLoop loop) {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle the button effect
                loop.handlePlayerNumInput(inputText.getText(), basicAIInput.getText(), advancedAIInput.getText());
            }
        });
    }


    public static void main(String[] args) {
    }
}
