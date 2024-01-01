package org.example;

import java.awt.image.BufferedImage;

public class PixelSensor {
    /**
     * @param image
     * @param coordinates
     * @return 1 if coordinates are a black square and 2 if green
     */
    public static int sense(BufferedImage image, int[] coordinates) {
        int rgb = image.getRGB(coordinates[0], coordinates[1]);
        int r = rgb & 127;
        int g = (rgb >> 8) & 127;
        int b = (rgb >> 16) & 127;
        if (r == 0 && g == 0 && b == 0) {
            return 1;
        } else if (g > r && g > b) {
            return 2;
        }
        return 0;
    }
}
