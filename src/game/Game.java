package game;
import cards.*;
import constants.CardType;
import constants.Color;
import constants.Direction;
import players.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * The UNO game engine class, handles core game logic and behaviors.
 * Maintain states like draw pile, discard pile, players.
 * @author Shuai Wei
 */
public class Game {
    /**
     * Used for generate initial card deck
     */
    public static final Color[] COLOR_LIST = {Color.RED, Color.YELLOW, Color.BLUE, Color.GREEN};
    /**
     * Used for generate initial card deck
     */
    public static final CardType[] TYPE_LIST = {CardType.ZERO, CardType.ONE, CardType.TWO, CardType.THREE, CardType.FOUR,
            CardType.FIVE, CardType.SIX, CardType.SEVEN, CardType.EIGHT, CardType.NINE, CardType.SKIP, CardType.REVERSE,
            CardType.DRAW_TWO};
    /**
     * Used for find the number cards.
     */
    public static final CardType[] NUM_LIST = {CardType.ZERO, CardType.ONE, CardType.TWO, CardType.THREE, CardType.FOUR,
            CardType.FIVE, CardType.SIX, CardType.SEVEN, CardType.EIGHT, CardType.NINE};

    private ArrayList<Card> drawPile = new ArrayList<>();
    private ArrayList<Card> discardPile = new ArrayList<>();

    private ArrayList<Player> players = new ArrayList<>();
    private int currentPlayerIndex = 0;

    private Color currentColor;
    private Card currentCard;
    private Direction direction = Direction.RIGHT;
    private int cardStack = 0;

    /**
     * Default constructor for a game.
     * Will initialize all cards in a UNO game.
     */
    public Game() {
        initializeDrawPile();
    }

    /**
     * Game constructor with a set of players.
     * Will initialize all cards in a UNO game.
     * @param players A list of player will play the game.
     */
    public Game(ArrayList<Player> players) {
        initializeDrawPile();
        this.players = players;
    }

    /**
     * Initialize all cards in a UNO game.
     */
    private void initializeDrawPile() {
        // Add all non-wild cards
        for (CardType type: TYPE_LIST) {
            if (type != CardType.ZERO) { // Each color only has one number zero card
                for (Color color: COLOR_LIST) {
                    drawPile.add(new Card(color, type));
                }
            }
            for (Color color: COLOR_LIST) {
                drawPile.add(new Card(color, type));
            }
        }
        // Add wild cards
        for (int i = 0; i < 4; i++) {
            drawPile.add(new Card(Color.WILD, CardType.CHANGE_COLOR));
            drawPile.add(new Card(Color.WILD, CardType.WILD_FOUR));
        }
    }

    /**
     * Reuse the discard pile as the draw pile
     */
    private void reUseDiscardPile() {
        Collections.shuffle(discardPile);
        for (Card c: discardPile) {
            drawPile.add(0, c); // Place the shuffled cards under the draw pile
        }
        discardPile = new ArrayList<>();
    }

    private Direction getOppositeDirection() {
        if (direction == Direction.RIGHT) {
            return Direction.LEFT;
        } else {
            return Direction.RIGHT;
        }
    }

    /**
     * Get the next player's index of the list.
     * @param dir the direction we want to move on,
     * @return the desired player index.
     */
    private int getNextPlayerIndex(Direction dir) {
        if (dir == Direction.RIGHT) {
            return (currentPlayerIndex + 1) % players.size();
        } else {
            if (currentPlayerIndex - 1 < 0) {
                return players.size() - 1;
            } else {
                return currentPlayerIndex - 1;
            }
        }
    }

    /**
     * Switch to next turn by changing current player.
     */
    public void proceed() {
        currentPlayerIndex = getNextPlayerIndex(direction);
    }

    private void shuffleDrawPile() {
        Collections.shuffle(drawPile);
    }

    public void setDrawPile(ArrayList<Card> drawPile) {
        this.drawPile = drawPile;
    }

    public void setDiscardPile(ArrayList<Card> pile) {
        discardPile = pile;
    }

    public void setCurrentCard(Card c) {
        this.currentCard = c;
    }

    public ArrayList<Card> getDrawPile() {
        return drawPile;
    }

    public ArrayList<Card> getDiscardPile() {
        return discardPile;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Card getCurrentCard() {
        return currentCard;
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public int getCardStack() {
        return cardStack;
    }

    /**
     * Get the player on turn
     * @return the player on turn
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /**
     * Get the next player
     * @return the next player
     */
    public Player getNextPlayer() {
        return players.get(getNextPlayerIndex(direction));
    }

    /**
     * Get the previous player according to the direction
     * @return the previous player
     */
    public Player getPreviousPlayer() {
        return players.get(getNextPlayerIndex(getOppositeDirection()));
    }

    /**
     * Update states from a player plays a card.
     * @param c The card to be played.
     * @param color The color the player wants to change to (if it is a wild card). Null if not wild card.
     * @return True: if the card is valid; False: if the card is not in player's hand.
     */
    public boolean playerPlaysACard(Card c, Color color) {
        Player p = getCurrentPlayer();
        if (!p.playCard(c)) {
            return false;
        }
        currentCard = c;
        currentColor = c.getColor();
        discardPile.add(c);
        if (c.getType() == CardType.DRAW_TWO) {
            cardStack += 1;     // Split draw rule: the player before and after draw one card.
            addOneCardToPlayer(getPreviousPlayer());   // Add one card to the previous player
        }
        if (c.getType() == CardType.REVERSE) {    // Reverse the direction
            if (direction == Direction.RIGHT) {
                direction = Direction.LEFT;
            } else {
                direction = Direction.RIGHT;
            }
        }
        if (c.getColor() == Color.WILD && color != null) {    // If the player wants to play a wild card
            currentColor = color;
            if (c.getType() == CardType.WILD_FOUR) {
                cardStack += 4;
            }
        }
        proceed();
        return true;
    }

    /**
     * Start the game. Shuffle cards. Give out initial cards. Choose a player
     * to start randomly.
     */
    public void startGame() {
        // Shuffle draw piles
        shuffleDrawPile();
        // give initial 7 cards for each player
        for (Player player: players) {
            for (int i = 0; i < 7; i++) {
                player.addOneCard(drawPile.remove(drawPile.size() - 1)); // pop a card from the draw pile and push it to player's hand
            }
        }
        // Find a number card to be the initial card.
        for (int i = 0; i < drawPile.size(); i++) {
            Card c = drawPile.get(i);
            if (Arrays.asList(NUM_LIST).contains(c.getType())) {
                currentCard = c;
                currentColor = c.getColor();
                drawPile.remove(i);
                discardPile.add(c);
                break;
            }
        }
        // Set the first player to be the current player
        currentPlayerIndex = 0;
    }

    /**
     * Draw one card for a given player.
     * @param p the player who wants to draw a card.
     * @return the card which the player drew.
     */
    public Card addOneCardToPlayer(Player p) {
        if (drawPile.size() < 1) {
            reUseDiscardPile();
        }
        Card card = drawPile.remove(drawPile.size() - 1);
        p.addOneCard(card);
        return card;
    }

    /**
     * Draw one card from the draw pile if no card from hand can be played.
     * If the new card can be played, play it. Otherwise skip this turn.
     * @param potentialColor If the draw card is wild, this the color the player wants to change.
     */
    public void currentPlayerDrawOneCard(Color potentialColor) {
        Card card = addOneCardToPlayer(getCurrentPlayer());

        if (card.getColor() == currentColor || card.getColor() == Color.WILD) {
            playerPlaysACard(card, potentialColor);
        } else if (card.getType() == currentCard.getType()) {
            playerPlaysACard(card, potentialColor);
        } else {
            proceed();
        }
    }

    /**
     * The current player to get penalty and skip his turn.
     */
    public void getPenalty() {
        if (drawPile.size() < cardStack) {
            reUseDiscardPile();
        }
        for (int i = 0; i < cardStack; i++) {
            getCurrentPlayer().addOneCard(drawPile.remove(drawPile.size() - 1));  // pop out the card from draw pile and give it to the player
        }
        cardStack = 0;
        proceed();
    }

    /**
     * Check if the game is over.
     * @return True if game over; False otherwise.
     */
    public boolean ifOver() {
        if (getCurrentPlayer().getHandCards().size() == 0) {
            return true;
        }
        return false;
    }

}
