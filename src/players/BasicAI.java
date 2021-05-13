package players;

import cards.Card;
import constants.Color;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class BasicAI extends Player {

    public BasicAI() {
        super();
    }

    /**
     * Constructor for AI
     * @param name The name of AI
     */
    public BasicAI(String name) {
        super(name);
    }

    /**
     * Ask the basic AI which card it wants to play.
     * @param currentCard current card at top of discard pile
     * @param currentColor current color
     * @return the card will be played. null if no card can be played.
     */
    public Card getSelectedCard(Card currentCard, Color currentColor) {
        ArrayList<Card> playableCards = getPlayableCards(currentCard, currentColor);
        // Check if there are cards can be played
        if (playableCards.size() == 0) {
            return null;
        }
        // Randomly select a card to play
        int randomIndex = ThreadLocalRandom.current().nextInt(0, playableCards.size());
        return playableCards.get(randomIndex);
    }

    /**
     * Ask the basic AI which color it wants to change to, if applicable
     * @return The selected color
     */
    public Color getSelectedColor() {
        int randomIndex = ThreadLocalRandom.current().nextInt(0, 4);
        return Color.getColorList()[randomIndex];
    }
}
