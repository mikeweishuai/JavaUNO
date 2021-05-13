package tests;

import cards.*;
import constants.*;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestCard {

    /**
     *  Testing equal method of Card
     */
    @Test public void CardEquals() {
        Card a = new Card(Color.GREEN, CardType.ONE);
        Card b = new Card(Color.GREEN, CardType.ONE);
        assertEquals(a, b);
        Card c = new Card(Color.GREEN, CardType.REVERSE);
        assertNotEquals(a, c);
    }

    /**
     * Test if not equal due to different type.
     */
    @Test public void CardNotEqualsByDifferentType() {
        Card a = new Card(Color.GREEN, CardType.ONE);
        String b = "GREEN";
        assertNotEquals(a, b);
    }

    @Test public void TestStringToColor() {
        Color red = Color.stringToColor("red");
        Color blue = Color.stringToColor("blue");
        Color yellow = Color.stringToColor("yellow");
        Color green = Color.stringToColor("green");
        Color none = Color.stringToColor("123131");

        assertEquals(red, Color.RED);
        assertEquals(blue, Color.BLUE);
        assertEquals(yellow, Color.YELLOW);
        assertEquals(green, Color.GREEN);
        assertEquals(none, null);
    }

}
