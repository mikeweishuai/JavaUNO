package tests;

import cards.Card;
import constants.CardType;
import constants.Color;

import org.junit.Test;
import static org.junit.Assert.*;
import players.BasicAI;
import players.Player;

import java.util.Arrays;


public class TestBasicAI {

    /**
     * Set up a player's hand cards.
     * @param p the input player.
     */
    public void addSampleCards(Player p) {
        p.addOneCard(new Card(Color.GREEN, CardType.ONE));
        p.addOneCard(new Card(Color.WILD, CardType.CHANGE_COLOR));
        p.addOneCard(new Card(Color.BLUE, CardType.SKIP));
        p.addOneCard(new Card(Color.YELLOW, CardType.DRAW_TWO));
    }

    /**
     * Test if the basic AI can play a card if possible
     */
    @Test public void CanPlayValidCardBasic() {
        BasicAI ai1 = new BasicAI();
        addSampleCards(ai1);

        Card c = new Card(Color.GREEN, CardType.FIVE);
        Card selectedCard = ai1.getSelectedCard(c, Color.GREEN);

        assertTrue(selectedCard.equals(new Card(Color.GREEN, CardType.ONE)) ||
                selectedCard.equals(new Card(Color.WILD, CardType.CHANGE_COLOR)));
    }

    /**
     * Test if basic AI plays no card if no card can be played
     */
    @Test public void CantPlayInvalidCardBasic() {
        BasicAI ai1 = new BasicAI("AI player 1");
        addSampleCards(ai1);

        Card c = new Card(Color.GREEN, CardType.SKIP);
        Card selectedCard = ai1.getSelectedCard(c, Color.GREEN);

        assertNull(selectedCard);
    }

    /**
     * Ask the basic AI which color it wants to change to.
     * Should return a random valid color
     */
    @Test public void GetColorBasic() {
        BasicAI ai1 = new BasicAI("AI player 1");
        Color selectedColor = ai1.getSelectedColor();
        // Test if the color list contains the selected color
        assertTrue(Arrays.asList(Color.getColorList()).contains(selectedColor));
    }
}
