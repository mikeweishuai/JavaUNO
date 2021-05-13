package players;

import cards.Card;
import constants.*;

import java.util.ArrayList;

/**
 * The player class in a UNO game.
 * Each player will hold a list of cards. The cards will be inserted or deleted
 * based on the behaviors.
 * @author Shuai Wei.
 */
public class Player {
    private ArrayList<Card> handCards = new ArrayList<>();
    private String name;

    public Player() {
        name = "default player";
    }

    /**
     * Constructor for a player
     * @param name The player's name
     */
    public Player(String name) {
        this.name = name;
    }

    /**
     * Add the given card the player's hand cards.
     * @param card the card to be added.
     */
    public void addOneCard(Card card) {
        handCards.add(card);
    }

    public ArrayList<Card> getHandCards() {
        return handCards;
    }

    public String getPlayerName() {
        return name;
    }

    /**
     * A player tries to play a card. If a card is played, it will be removed from
     * the hand cards.
     * @param card the card the player wants to play with
     * @return True if the card exists; False if the card not exist.
     */
    public boolean playCard(Card card) {
        for (Card handCard: handCards) {
            if (handCard.equals(card)) {
                handCards.remove(card);
                return true;
            }
        }
        return false;
    }

    /**
     * Get all playable cards according to the input card and current color.
     * @param inputCard the previous card played
     * @param inputColor current color
     * @return An ArrayList of Card. Null if input color is wild.
     */
    public ArrayList<Card> getPlayableCards(Card inputCard, Color inputColor) {
        // If it is the start of a game, all cards
        // check input if legal
        if (inputColor == Color.WILD) {
            return null;
        }
        ArrayList<Card> playableCards = new ArrayList<>();
        // if input is skip card, no card can be played
        if (inputCard.getType() == CardType.SKIP) {
            return playableCards;
        }
        // If the input card is black (wild) draw two, the current player have to get penalty
        if (inputCard.getType() == CardType.WILD_FOUR) {
            return playableCards;
        }

        for (Card handCard : handCards) {
            // if the player has cards with same color (or wild)
            if (handCard.getColor() == inputColor || handCard.getColor() == Color.WILD) {
                playableCards.add(handCard);
                continue;
            }
            // check if the player has cards that have the same type
            if (handCard.getType() == inputCard.getType()) {
                playableCards.add(handCard);
            }
        }

        return playableCards;
    }

}
