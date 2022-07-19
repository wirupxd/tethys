package me.alpha432.oyvey.util;

import java.awt.Color;

public class ColourUtils {
    public static Color getGradientOffset(Color color, Color color2, double d) {
        int n;
        double d2;
        if (d > 1.0) {
            d2 = d % 1.0;
            n = (int)d;
            d = n % 2 == 0 ? d2 : 1.0 - d2;
        }
        d2 = 1.0 - d;
        n = (int)((double)color.getRed() * d2 + (double)color2.getRed() * d);
        int n2 = (int)((double)color.getGreen() * d2 + (double)color2.getGreen() * d);
        int n3 = (int)((double)color.getBlue() * d2 + (double)color2.getBlue() * d);
        return new Color(n, n2, n3);
    }
}
