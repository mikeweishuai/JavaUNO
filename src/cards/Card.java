package cards;

import constants.*;

/**
 * A class used to store UNO cards information.
 * @author Shuai Wei
 */
public class Card {

    private final Color color;

    private final CardType type;

    public Card(Color color, CardType type) {
        this.color = color;
        this.type = type;
    }

    public Color getColor() {
        return this.color;
    }

    public CardType getType() {
        return this.type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Card)) {
            return false;
        }

        Card temp = (Card) o;

        return (temp.getColor() == this.getColor())
                && (temp.getType() == this.getType());
    }
}


