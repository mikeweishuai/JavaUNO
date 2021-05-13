package constants;

import java.util.Locale;

public enum Color {
    GREEN, BLUE, YELLOW, RED, WILD;

    /**
     * Get the color enum value by string
     * @param input the input string
     * @return the corresponding enum value, null if the input string is not valid.
     */
    public static Color stringToColor(String input) {
        String str = input.toLowerCase();
        return switch (str) {
            case "red" -> Color.RED;
            case "blue" -> Color.BLUE;
            case "yellow" -> Color.YELLOW;
            case "green" -> Color.GREEN;
            default -> null;
        };
    }

    /**
     * Get a list of colors
     * @return The color list
     */
    public static Color[] getColorList() {
        Color[] colorList = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW};
        return colorList;
    }
}
