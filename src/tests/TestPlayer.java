package tests;
import cards.*;
import constants.*;

import org.junit.Test;
import players.Player;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TestPlayer {

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
     * Testing the case that playable cards exist by color.
     */
    @Test public void PlayableCardsExistByColor() {
        Player p = new Player("Mike");
        addSampleCards(p);
        Card inputCard = new Card(Color.YELLOW, CardType.TWO);
        ArrayList<Card> output = p.getPlayableCards(inputCard, inputCard.getColor());

        Card expectOutput1 = new Card(Color.WILD, CardType.CHANGE_COLOR);
        Card expectOutput2 = new Card(Color.YELLOW, CardType.DRAW_TWO);

        assertEquals(output.size(), 2);
        assertEquals(output.get(0), expectOutput1);
        assertEquals(output.get(1), expectOutput2);
    }

    /**
     * Testing that playable cards exist by number.
     */
    @Test public void PlayableCardsExistByNumber() {
        Player p = new Player("Mike");
        addSampleCards(p);
        Card inputCard = new Card(Color.YELLOW, CardType.ONE);
        ArrayList<Card> output = p.getPlayableCards(inputCard, inputCard.getColor());

        Card expectOutput = new Card(Color.GREEN, CardType.ONE);

        assertEquals(output.get(0), expectOutput);
    }

    /**
     * If there is no matching card in the hand.
     */
    @Test public void PlayableCardsNotExist() {
        Player p = new Player("Mike");
        p.addOneCard(new Card(Color.BLUE, CardType.ONE));
        Card inputCard = new Card(Color.RED, CardType.TWO);
        ArrayList<Card> output = p.getPlayableCards(inputCard, inputCard.getColor());
        assertEquals(output.size(), 0);
    }

    /**
     * If previous card is the skip card, no card can be played
     */
    @Test public void PlayableCardsNotExistSkip() {
        Player p = new Player("Mike");
        addSampleCards(p);
        Card inputCard = new Card(Color.YELLOW, CardType.SKIP);
        ArrayList<Card> output = p.getPlayableCards(inputCard, inputCard.getColor());
        assertEquals(output.size(), 0);
    }

    /**
     * Testing illegal input (input color is wild)
     * Input color of WILD is not acceptable since the previous color should be one
     * of RED, YELLOW, BLUE, or GREEN.
     */
    @Test public void PlayableCardsNullByWild() {
        Player p = new Player("Mike");
        addSampleCards(p);
        Card inputCard = new Card(Color.WILD, CardType.CHANGE_COLOR);
        assertNull(p.getPlayableCards(inputCard, inputCard.getColor()));
    }

    /**
     * Testing if a card is played, then it is removed from the hand cards and return true.
     */
    @Test public void PlayCardValid() {
        Player p = new Player("Mike");
        addSampleCards(p);
        Card input = new Card(Color.BLUE, CardType.SKIP);
        assertTrue(p.playCard(input));
        assertEquals(p.getHandCards().size(), 3);
    }

    /**
     * Testing if a not exist card is played, nothing is removed from hand cards and return false.
     */
    @Test public void PlayCardInvalid() {
        Player p = new Player("Mike");
        addSampleCards(p);
        Card input = new Card(Color.YELLOW, CardType.REVERSE);
        assertFalse(p.playCard(input));
        assertEquals(p.getHandCards().size(), 4);
    }

    /**
     * Testing a wild draw card stops the stacking and cannot be defended (return list is empty).
     */
    @Test public void PlayableCardsBlackIsKing() {
        Player p = new Player("Mike");
        addSampleCards(p);
        Card inputCard = new Card(Color.YELLOW, CardType.WILD_FOUR);
        ArrayList<Card> output = p.getPlayableCards(inputCard, inputCard.getColor());
        assertEquals(output.size(), 0);
    }
}
