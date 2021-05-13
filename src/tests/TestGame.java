package tests;
import cards.*;
import constants.*;

import org.junit.Test;
import game.Game;
import players.Player;


import java.util.ArrayList;
import static org.junit.Assert.*;

public class TestGame {
    /*
        Helper functions
     */

    /**
     * Get the occurrence of an element from an arraylist
     * @param cards a list of cards
     * @param c input card
     * @return occurrence of the card in the list of cards
     */
    private int occurrence(ArrayList<Card> cards, Card c) {
        int count = 0;
        for (Card card : cards) {
            if (card.equals(c)) {
                count++;
            }
        }
        return count;
    }

    private Game setSampleGameTwoPlayers() {
        ArrayList<Player> players = new ArrayList<>();
        Player p1 = new Player("AAA");
        Player p2 = new Player("BBB");
        players.add(p1);
        players.add(p2);
        Game g = new Game(players);
        g.startGame();
        return g;
    }

    private Game setSampleGameThreePlayers() {
        ArrayList<Player> players = new ArrayList<>();
        Player p1 = new Player("AAA");
        Player p2 = new Player("BBB");
        Player p3 = new Player("CCC");
        players.add(p1);
        players.add(p2);
        players.add(p3);
        return new Game(players);
    }

    /*
        Test suites
     */

    /**
     * Testing if the number of cards is correct after initialization.
     */
    @Test public void TestCardInitializationCount() {
        Game g = new Game();
        assertEquals(g.getDrawPile().size(), 108);
    }

    /**
     * Testing the non-wild card are correctly initialized.
     */
    @Test public void TestNonWildCardsInit() {
        Game g = new Game();
        for (CardType type: Game.TYPE_LIST) {
            for (Color color: Game.COLOR_LIST) {
                if (type == CardType.ZERO) {
                    assertEquals(occurrence(g.getDrawPile(), new Card(color, type)), 1);
                } else {
                    assertEquals(occurrence(g.getDrawPile(), new Card(color, type)), 2);
                }
            }
        }
    }

    /**
     * Testing the wild cards are correctly initialized.
     */
    @Test public void TestActionCardsInit() {
        Game g = new Game();
        assertEquals(occurrence(g.getDrawPile(), new Card(Color.WILD, CardType.CHANGE_COLOR)), 4);
        assertEquals(occurrence(g.getDrawPile(), new Card(Color.WILD, CardType.WILD_FOUR)), 4);
    }

    /**
     * Testing if the cards are shuffled at the start of a game.
     */
    @Test public void RandomShuffledAtStart() {
        ArrayList<Player> p1 = new ArrayList<>();
        p1.add(new Player("Tim"));
        ArrayList<Player> p2 = new ArrayList<>();
        p2.add(new Player("Bob"));
        Game g1 = new Game(p1);
        Game g2 = new Game(p2);
        g1.startGame();
        g2.startGame();
        ArrayList<Card> out1 = g1.getPlayers().get(0).getHandCards(); // Get the hand cards of the only player in g1
        ArrayList<Card> out2 = g2.getPlayers().get(0).getHandCards(); // Get the hand cards of the only player in g2
        boolean ifRandom = false;
        for (int i = 0; i < out1.size(); i++) {
            if (!out1.get(i).equals(out2.get(i))) {
                ifRandom = true;
                break;
            }
        }
        assertTrue(ifRandom);
    }

    /**
     * Test if all players get seven cards when game start.
     */
    @Test public void PlayerGetSevenInitialCards() {
        Game g = setSampleGameTwoPlayers();
        ArrayList<Player> outPlayers = g.getPlayers();
        for (Player p: outPlayers) {
            assertEquals(p.getHandCards().size(), 7);
        }
    }

    /**
     * Test if the current player is the first player after start the game.
     */
    @Test public void CheckCurrentPlayerAtStart() {
        Game g = setSampleGameTwoPlayers();
        Player current = g.getCurrentPlayer();
        assertEquals(current.getPlayerName(), "AAA");
    }

    /**
     * Test playing a not wild card. States should be updated properly.
     */
    @Test public void GamePlayCardNotWild() {
        Game g = setSampleGameTwoPlayers();
        Player p = g.getCurrentPlayer();
        Card c = new Card(Color.RED, CardType.ZERO);
        p.addOneCard(c);
        g.playerPlaysACard(c, null);
        assertEquals(p.getHandCards().size(), 7);   // 7 + 1 - 1 = 7
        assertEquals(g.getDiscardPile().size(), 2); // Discard pile number raise from 1 to 2
        assertEquals(g.getCurrentCard(), c);              // Successfully set the current card
        assertEquals(g.getCurrentColor(), c.getColor());  // Successfully set the current color
        assertEquals(g.getCurrentPlayerIndex(), 1); // Move to next turn
    }

    /**
     * Test playing a wild change color card.
     */
    @Test public void GamePlayCardWild() {
        Game g = setSampleGameTwoPlayers();
        Player p = g.getCurrentPlayer();
        Card c = new Card(Color.WILD, CardType.CHANGE_COLOR);
        p.addOneCard(c);
        g.playerPlaysACard(c, Color.YELLOW);
        assertEquals(p.getHandCards().size(), 7);
        assertEquals(g.getDiscardPile().size(), 2);
        assertEquals(g.getCurrentCard(), c);
        assertEquals(g.getCurrentColor(), Color.YELLOW);
        assertEquals(g.getCurrentPlayerIndex(), 1);
    }

    /**
     * When play a reverse card, the direction should change.
     * Thus the next player is different.
     */
    @Test public void GamePlayCardReverse() {
        Game g = setSampleGameTwoPlayers();
        Player p = g.getCurrentPlayer();
        Card c = new Card(Color.RED, CardType.REVERSE);
        p.addOneCard(c);
        g.playerPlaysACard(c, null);
        assertEquals(g.getCurrentPlayerIndex(), g.getPlayers().size() - 1);
    }

    /**
     * Test if the card does not exist in player's hand, should return false.
     */
    @Test public void GamePlayCardInvalid() {
        Game g = setSampleGameTwoPlayers();
        Card c = new Card(Color.RED, CardType.CHANGE_COLOR);
        assertFalse(g.playerPlaysACard(c, null));
    }

    /**
     * Test draw one card with case:
     * the card draw from the pile is not playable
     */
    @Test public void DrawOneCardNotPlay() {
        Player p = new Player("A");
        ArrayList<Player> players = new ArrayList<>();
        players.add(p);
        Game g = new Game(players);

        Card a = new Card(Color.RED, CardType.ONE);
        Card b = new Card(Color.BLUE, CardType.TWO);
        Card c = new Card(Color.GREEN, CardType.THREE);
        Card d = new Card(Color.YELLOW, CardType.FOUR);

        p.addOneCard(a);

        ArrayList<Card> pile = new ArrayList<>();
        pile.add(b);
        pile.add(c);
        g.setDrawPile(pile);
        g.setCurrentCard(d);

        g.currentPlayerDrawOneCard(null);

        assertEquals(p.getHandCards().size(), 2);
        assertEquals(g.getDrawPile().size(), 1);
        assertEquals(g.getDiscardPile().size(), 0);
    }

    /**
     * Test draw one card with case:
     * the card draw from the pile is playable
     */
    @Test public void DrawOneCardPlay() {
        Player p = new Player("A");
        ArrayList<Player> players = new ArrayList<>();
        players.add(p);
        Game g = new Game(players);

        Card a = new Card(Color.RED, CardType.ONE);
        Card b = new Card(Color.BLUE, CardType.TWO);
        Card c = new Card(Color.GREEN, CardType.THREE);
        Card d = new Card(Color.RED, CardType.THREE);

        p.addOneCard(a);

        ArrayList<Card> pile = new ArrayList<>();
        pile.add(b);
        pile.add(c);
        g.setDrawPile(pile);
        g.setCurrentCard(d);

        g.currentPlayerDrawOneCard(null);

        assertEquals(p.getHandCards().size(), 1);
        assertEquals(g.getDrawPile().size(), 1);
        assertEquals(g.getDiscardPile().size(), 1);
    }

    /**
     * Test draw one card with case:
     * draw pile is empty, will reuse the cards in discard pile
     */
    @Test public void DrawOneCardReuse() {
        Player p = new Player("A");
        ArrayList<Player> players = new ArrayList<>();
        players.add(p);
        Game g = new Game(players);

        Card a = new Card(Color.RED, CardType.ONE);
        Card b = new Card(Color.BLUE, CardType.TWO);
        Card c = new Card(Color.GREEN, CardType.THREE);
        Card d = new Card(Color.RED, CardType.FOUR);

        p.addOneCard(a);

        ArrayList<Card> pile = new ArrayList<>();
        pile.add(b);
        pile.add(c);
        ArrayList<Card> emptyPile = new ArrayList<>();
        g.setDiscardPile(pile);
        g.setDrawPile(emptyPile);
        g.setCurrentCard(d);

        g.currentPlayerDrawOneCard(null);

        assertEquals(p.getHandCards().size(), 2);
        assertEquals(g.getDrawPile().size(), 1);
        assertEquals(g.getDiscardPile().size(), 0);
    }

    /**
     * Test if the next player get a single + penalty.
     */
    @Test public void GetPenaltySimple() {
        Game g = setSampleGameThreePlayers();
        g.getCurrentPlayer().addOneCard(new Card(Color.RED, CardType.DRAW_TWO));
        g.playerPlaysACard(new Card(Color.RED, CardType.DRAW_TWO), null);
        g.getPenalty();
        assertEquals(g.getPlayers().get(1).getHandCards().size(), 1);   // player 0 use draw two card causes player 1 to draw 2 cards and skip his round
    }

    /**
     * Test if the next player get a +5 penalty by stacking.
     */
    @Test public void GetPenaltyStack() {
        Game g = setSampleGameThreePlayers();
        g.getCurrentPlayer().addOneCard(new Card(Color.WILD, CardType.WILD_FOUR));
        g.playerPlaysACard(new Card(Color.WILD, CardType.WILD_FOUR), Color.BLUE);
        g.getCurrentPlayer().addOneCard(new Card(Color.RED, CardType.DRAW_TWO));
        g.playerPlaysACard(new Card(Color.RED, CardType.DRAW_TWO), null);
        g.getPenalty();
        assertEquals(g.getPlayers().get(2).getHandCards().size(), 5);
        assertEquals(g.getCardStack(), 0);
    }

    /**
     * Test if successfully detect game over.
     * Player 1 (current player) has not cards.
     */
    @Test public void CheckGameOverPositive() {
        Game g = setSampleGameThreePlayers();
        Card c = new Card(Color.RED, CardType.ONE);
        ArrayList<Player> players = g.getPlayers();
        players.get(1).addOneCard(c);
        players.get(2).addOneCard(c);
        assertTrue(g.ifOver());
    }

    /**
     * Test if game is not over yet.
     * All players have at least one card.
     */
    @Test public void CheckGameOverNegative() {
        Game g = setSampleGameThreePlayers();
        Card c = new Card(Color.RED, CardType.ONE);
        ArrayList<Player> players = g.getPlayers();
        players.get(0).addOneCard(c);
        players.get(1).addOneCard(c);
        players.get(2).addOneCard(c);
        assertFalse(g.ifOver());
    }

    /**
     * Test if the previous player draw a card when the current player played a draw two.
     */
    @Test public void PlayACardSplitDraw() {
        Game g = setSampleGameThreePlayers();
        g.getCurrentPlayer().addOneCard(new Card(Color.RED, CardType.DRAW_TWO));
        g.playerPlaysACard(new Card(Color.RED, CardType.DRAW_TWO), null);
        assertEquals(g.getPlayers().get(2).getHandCards().size(), 1);
    }

}
