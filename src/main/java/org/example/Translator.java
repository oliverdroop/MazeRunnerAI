package org.example;

public class Translator {

    public static int[] translate(int x, int y, int direction) {
        switch(direction) {
            case 0:
                return new int[] {x, y - 1};
            case 1:
                return new int[] {x + 1, y};
            case 2:
                return new int[] {x, y + 1};
            case 3:
                return new int[] {x - 1, y};
            default:
                throw new RuntimeException(String.format("Unable to translate %d", direction));
        }
    }
}
