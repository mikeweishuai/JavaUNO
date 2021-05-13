package players;

import cards.Card;
import constants.CardType;
import constants.Color;

import java.util.ArrayList;
import java.util.Collections;

public class AdvancedAI extends BasicAI {
    Color preferredColor;

    public AdvancedAI(String name) {
        super(name);
    }

    /**
     * The color that has most occurrence.
     */
    private void findPreferredColor() {
        int countRed = 0;
        int countBlue = 0;
        int countYellow = 0;
        int countGreen = 0;
        for (Card card: getHandCards()) {
            if (card.getColor().equals(Color.RED)) {
                countRed++;
            } else if (card.getColor().equals(Color.BLUE)) {
                countBlue++;
            } else if (card.getColor().equals(Color.YELLOW)) {
                countYellow++;
            } else if (card.getColor().equals(Color.GREEN)) {
                countGreen++;
            }
        }
        ArrayList<Integer> counter = new ArrayList<>();
        counter.add(countRed);
        counter.add(countBlue);
        counter.add(countYellow);
        counter.add(countGreen);
        Collections.sort(counter);
        if (countRed == counter.get(3)) {
            preferredColor = Color.RED;
        } else if (countBlue == counter.get(3)) {
            preferredColor = Color.BLUE;
        } else if (countYellow == counter.get(3)) {
            preferredColor = Color.YELLOW;
        } else if (countGreen == counter.get(3)) {
            preferredColor = Color.GREEN;
        }
    }


    /**
     * Priority: draw two card with preferred color -> number card with preferred color -> draw two card -> number card ->
     * wild draw four -> wild change color
     * @param currentCard current card at top of discard pile
     * @param currentColor current color
     * @return the card to be played
     */
    @Override
    public Card getSelectedCard(Card currentCard, Color currentColor) {
        ArrayList<Card> playableCards = getPlayableCards(currentCard, currentColor);
        // Check if there are cards can be played
        if (playableCards.size() == 0) {
            return null;
        }
        // Select a card with strategy
        findPreferredColor();
        for (Card card: playableCards) {    // play draw two card with preferred color
            if (card.getType() == CardType.DRAW_TWO && card.getColor() == preferredColor) {
                return card;
            }
        }
        for (Card card: playableCards) {    // number card with preferred color
            if (card.getColor() == preferredColor) {
                return card;
            }
        }
        for (Card card: playableCards) {    // draw two card
            if (card.getColor() != Color.WILD && card.getType() == CardType.DRAW_TWO) {
                return card;
            }
        }
        for (Card card: playableCards) {    // number card
            if (card.getColor() != Color.WILD) {
                return card;
            }
        }
        for (Card card: playableCards) {    // wild draw four
            if (card.getType() == CardType.WILD_FOUR) {
                return card;
            }
        }
        return playableCards.get(0);    // wild change color (others)
    }

    /**
     * Get the preferred color. An advanced AI prefer a color that has
     * most occurrence in its hand cards.
     * @return the preferred color.
     */
    @Override
    public Color getSelectedColor() {
        findPreferredColor();
        return preferredColor;
    }
}
