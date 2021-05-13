package tests;
import cards.Card;
import constants.CardType;
import constants.Color;

import org.junit.Test;
import static org.junit.Assert.*;

import players.AdvancedAI;

public class TestAdvancedAI {

    /**
     * Test if the preferred color is the most appearance color
     */
    @Test public void CorrectPreferredColor() {
        AdvancedAI ai = new AdvancedAI("ai");
        ai.addOneCard(new Card(Color.GREEN, CardType.ONE));
        ai.addOneCard(new Card(Color.GREEN, CardType.ONE));
        ai.addOneCard(new Card(Color.YELLOW, CardType.TWO));

        assertEquals(ai.getSelectedColor(), Color.GREEN);
    }

    /**
     * The draw two card with preferred color will be played first
     */
    @Test public void DrawTwoWithColor() {
        AdvancedAI ai = new AdvancedAI("ai");
        ai.addOneCard(new Card(Color.GREEN, CardType.DRAW_TWO));
        ai.addOneCard(new Card(Color.RED, CardType.DRAW_TWO));
        ai.addOneCard(new Card(Color.GREEN, CardType.ONE));
        ai.addOneCard(new Card(Color.YELLOW, CardType.ONE));
        ai.addOneCard(new Card(Color.WILD, CardType.WILD_FOUR));
        ai.addOneCard(new Card(Color.WILD, CardType.CHANGE_COLOR));

        assertEquals(ai.getSelectedCard(new Card(Color.GREEN, CardType.ONE), Color.GREEN), new Card(Color.GREEN, CardType.DRAW_TWO));
    }

    /**
     * The number card with preferred color will be played first.
     */
    @Test public void NumberWithColor() {
        AdvancedAI ai = new AdvancedAI("ai");
        ai.addOneCard(new Card(Color.GREEN, CardType.ONE));
        ai.addOneCard(new Card(Color.GREEN, CardType.ONE));
        ai.addOneCard(new Card(Color.YELLOW, CardType.TWO));

        Card inputCard = new Card(Color.GREEN, CardType.TWO);
        assertEquals(ai.getSelectedCard(inputCard, Color.GREEN), new Card(Color.GREEN, CardType.ONE));
    }

    /**
     * The draw two card will be played first
     */
    @Test public void DrawTwoFirst() {
        AdvancedAI ai = new AdvancedAI("ai");
        ai.addOneCard(new Card(Color.YELLOW, CardType.ONE));
        ai.addOneCard(new Card(Color.GREEN, CardType.DRAW_TWO));

        Card inputCard = new Card(Color.YELLOW, CardType.TWO);
        assertEquals(ai.getSelectedCard(inputCard, Color.GREEN), new Card(Color.GREEN, CardType.DRAW_TWO));
    }

    /**
     * The number card will be played first
     */
    @Test public void NumberFirst() {
        AdvancedAI ai = new AdvancedAI("ai");
        ai.addOneCard(new Card(Color.GREEN, CardType.ONE));
        ai.addOneCard(new Card(Color.WILD, CardType.WILD_FOUR));

        Card inputCard = new Card(Color.GREEN, CardType.TWO);
        assertEquals(ai.getSelectedCard(inputCard, Color.GREEN), new Card(Color.GREEN, CardType.ONE));
    }

    /**
     * The wild draw four card will be played first
     */
    @Test public void WildFourFirst() {
        AdvancedAI ai = new AdvancedAI("ai");
        ai.addOneCard(new Card(Color.WILD, CardType.CHANGE_COLOR));
        ai.addOneCard(new Card(Color.WILD, CardType.WILD_FOUR));
        ai.addOneCard(new Card(Color.RED, CardType.ONE));

        Card inputCard = new Card(Color.GREEN, CardType.TWO);
        assertEquals(ai.getSelectedCard(inputCard, Color.GREEN), new Card(Color.WILD, CardType.WILD_FOUR));
    }

    /**
     * Otherwise we will play wild change color
     */
    @Test public void ChangeColor() {
        AdvancedAI ai = new AdvancedAI("ai");
        ai.addOneCard(new Card(Color.WILD, CardType.CHANGE_COLOR));
        ai.addOneCard(new Card(Color.RED, CardType.ONE));

        Card inputCard = new Card(Color.GREEN, CardType.TWO);
        assertEquals(ai.getSelectedCard(inputCard, Color.GREEN), new Card(Color.WILD, CardType.CHANGE_COLOR));
    }
}
