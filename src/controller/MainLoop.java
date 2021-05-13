package controller;

import GUI.EndScreen;
import GUI.PlayScreen;
import GUI.StartScreen;
import cards.Card;
import constants.CardType;
import constants.Color;
import game.Game;
import players.AdvancedAI;
import players.BasicAI;
import players.Player;

import javax.swing.*;
import java.util.ArrayList;

public class MainLoop {
    StartScreen startScreen;
    PlayScreen playScreen;
    EndScreen endScreen;
    Game g;

    int numPlayers = -1;
    int numBasicAIs = -1;
    int numAdvancedAIs = -1;

    public void setNumPlayers(int num) {
        numPlayers = num;
    }

    /**
     * Check if a string represents an integer.
     * @param input the input string
     * @return the integer it represents. -1 if not an integer.
     */
    private int ifInteger(String input) {
        if (input == null || input.length() == 0) {
            return -1;
        }
        int num;
        try {
            num = Integer.parseInt(input);
        } catch (Exception e) {
            return -1;
        }
        return num;
    }

    /**
     * Check if the input string is a number in range (1,10)
     * @param input the input string
     * @return true if the string is in the range, otherwise false
     */
    private boolean checkValidPlayerNumber(String input, String input2, String input3) {
        int num1 = ifInteger(input);
        int num2 = ifInteger(input2);
        int num3 = ifInteger(input3);
        if (num1 == -1 || num2 == -1 || num3 == -1) {
            return false;
        }
        int sum = num1 + num2 + num3;
        return (1 < sum && sum < 10);
    }

    /**
     * Create a list of players of size of n
     * @param numHuman the number of human players
     * @param numBasic the number of basic AIs
     * @param numAdvanced the number of advanced AIs
     * @return the created list of players
     */
    private ArrayList<Player> createPlayerListFromNum(int numHuman, int numBasic, int numAdvanced) {
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 1; i < numHuman + 1; i++) {
            players.add(new Player("Player " + i));
        }
        for (int i = numHuman + 1; i < numHuman + 1 + numBasic; i++) {
            players.add(new BasicAI("Basic AI " + (i - numHuman)));
        }
        for (int i = numHuman + numBasic + 1; i < numHuman + 1 + numBasic + numAdvanced; i++) {
            players.add(new AdvancedAI("Advanced AI " + (i - numHuman - numBasic)));
        }
        return players;
    }

    /**
     * Handle the player number inputs from the start screen.
     * If the input is valid, we will leave the start screen and start the game.
     * @param numInput player number input
     */
    public void handlePlayerNumInput(String numInput, String basicInput, String advancedInput) {
        if (checkValidPlayerNumber(numInput, basicInput, advancedInput)) {
            this.setNumPlayers(Integer.parseInt(numInput));
            this.numBasicAIs = Integer.parseInt(basicInput);
            this.numAdvancedAIs = Integer.parseInt(advancedInput);
            this.setupGame();
        } else {
            JOptionPane.showMessageDialog(null, "The inputs must be numbers and the total player must be from 2 to 8!");
        }
    }

    private int validateIdInput(String idInput) {
        // Check if card id input is a number
        if (ifInteger(idInput) == -1) {
            JOptionPane.showMessageDialog(null, "The id input must be an integer!");
            return -1;
        }
        // Check if the id is valid
        int id = ifInteger(idInput);
        int handCardSize = g.getCurrentPlayer().getHandCards().size();
        if (id < 1 || id > handCardSize) {
            JOptionPane.showMessageDialog(null, "Invalid id.");
            return -1;
        }
        return id;
    }

    private void checkGameOver() {
        if (g.getPreviousPlayer().getHandCards().size() == 0) {
            playScreen.setVisible(false);
            endScreen = new EndScreen(g.getCurrentPlayer());
        }
    }

    /**
     * Check if current round is AI's round,
     * if so, let the AI play a card or skip.
     */
    private void checkAIRound() {
        // Check if current player is an AI
        if (g.getCurrentPlayer().getClass() == BasicAI.class) {
            BasicAI ai = (BasicAI) g.getCurrentPlayer();
            // Determine to play of skip/penalty
            if (ai.getSelectedCard(g.getCurrentCard(), g.getCurrentColor()) != null) {
                // Play a card
                g.playerPlaysACard(ai.getSelectedCard(g.getCurrentCard(), g.getCurrentColor()), ai.getSelectedColor());
            } else {    // Ai will skip the round
                handleSkip(ai.getSelectedColor().name().toLowerCase());
            }
        }
        if (g.getCurrentPlayer().getClass() == AdvancedAI.class) {
            AdvancedAI ai = (AdvancedAI) g.getCurrentPlayer();
            // Determine to play of skip/penalty
            if (ai.getSelectedCard(g.getCurrentCard(), g.getCurrentColor()) != null) {
                // Play a card
                g.playerPlaysACard(ai.getSelectedCard(g.getCurrentCard(), g.getCurrentColor()), ai.getSelectedColor());
            } else {    // Ai will skip the round
                handleSkip(ai.getSelectedColor().name().toLowerCase());
            }
        }
    }

    /**
     * Handle the play button with id input and color input.
     * Use those inputs to update the game.
     * @param idInput id of the card that want to play
     * @param colorInput the color that the player want to change to (if the card is wild)
     */
    public void handlePlayInputs(String idInput, String colorInput) {
        // If id is not a number or not a valid index, just return.
        int id = validateIdInput(idInput);
        if (id == -1) {
            return;
        }
        // Check if the corresponding card is playable.
        ArrayList<Card> playableCards = g.getCurrentPlayer().getPlayableCards(g.getCurrentCard(), g.getCurrentColor());
        Card selectedCard = g.getCurrentPlayer().getHandCards().get(id - 1);
        if (playableCards.contains(selectedCard)) {
            // If the selected card is not a wild card.
            if (selectedCard.getColor() != Color.WILD) {
                g.playerPlaysACard(selectedCard, null);
                checkAIRound();
                checkGameOver();
            } else {    // If it is a wild card, we need to handle the colorInput.
                if (Color.stringToColor(colorInput) == null) {
                    JOptionPane.showMessageDialog(null, "Input color is invalid!");
                    return;
                }
                Color color = Color.stringToColor(colorInput);
                g.playerPlaysACard(selectedCard, color);
                checkAIRound();
                checkGameOver();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Selected card is not playable.");
            return;
        }
    }

    private void stopSkipping() {
        if (g.getCurrentCard().getType() == CardType.SKIP) {
            g.setCurrentCard(new Card(g.getCurrentColor(), CardType.NONE));     // Set the card type to none then skip this round to prevent skip forever
        }
        if (g.getCurrentCard().getType() == CardType.WILD_FOUR) {
            g.setCurrentCard(new Card(g.getCurrentColor(), CardType.NONE));     // Set the card type to none then skip this round to prevent skip forever (Black is king rule)
        }
    }

    /**
     * Handle the skip button with a color input
     * Determine if the current player take penalty, skip the turn, or draw one card and play it if possible.
     * @param colorInput potential color to change
     */
    public void handleSkip(String colorInput) {
        if (g.getCardStack() == 0) {    // No card in the stack
            // If previous is a skip card, draw a card and do nothing
            if (g.getCurrentCard().getType() == CardType.SKIP) {
                g.addOneCardToPlayer(g.getCurrentPlayer());
                stopSkipping();
                g.proceed();
                checkAIRound();
            } else {
                if (Color.stringToColor(colorInput) == null) {
                    JOptionPane.showMessageDialog(null, "Please provide a color that you want to change to if you draw a wild card.");
                    return;
                }
                g.currentPlayerDrawOneCard(Color.stringToColor(colorInput));     // Else draw one card and play it if possible
                stopSkipping();
                checkAIRound();
            }
        } else {    // There are cards in stack
            g.getPenalty();
            stopSkipping();
            checkAIRound();
        }
    }

    /**
     * Setup the game once we get valid player numbers.
     */
    public void setupGame() {
        // Close the start page if input is valid
        if (numPlayers != -1) {
            startScreen.setVisible(false);
            g = new Game(createPlayerListFromNum(numPlayers, numBasicAIs, numAdvancedAIs));
            g.startGame();
            playScreen = new PlayScreen(g, this);

        }
    }

    /**
     * Launch the start screen.
     */
    public MainLoop() {
        startScreen = new StartScreen(this);
    }

    public static void main(String[] args) {
        new MainLoop();
    }
}
