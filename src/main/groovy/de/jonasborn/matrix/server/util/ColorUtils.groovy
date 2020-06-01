package de.jonasborn.matrix.server.util

import java.awt.Color

class ColorUtils {

    public static Color fade(Color source, Color target, int steps, int step) {

        if (steps == step) return target

        def modes = [source.red > target.red, source.green > target.green, source.blue > target.blue]
        def diffs = [
                modes[0] ? source.red - target.red : target.red - source.red,
                modes[1] ? source.green - target.green : target.green - source.green,
                modes[2] ? source.blue - target.blue : target.blue - source.blue
        ]

        def max = diffs.max()

        if (modes[0]) source.red - (diffs[0] / steps) * step

        double[] values = [
                (modes[0]) ? source.red - (diffs[0] / steps) * step : source.red + (diffs[0] / steps) * step,
                (modes[1]) ? source.green - (diffs[1] / steps) * step : source.green + (diffs[1] / steps) * step,
                (modes[2]) ? source.blue - (diffs[2] / steps) * step : source.blue + (diffs[2] / steps) * step
        ]


        return new Color(
                Math.round(values[0]) as int,
                Math.round(values[1]) as int,
                Math.round(values[2]) as int
        )

    }

    public static Color fromHex(String colorStr) {
        colorStr = colorStr.replace("#", "")
        return new Color(
                Integer.valueOf(colorStr.substring(0, 2), 16),
                Integer.valueOf(colorStr.substring(2, 4), 16),
                Integer.valueOf(colorStr.substring(4, 6), 16));
    }

    public static Color fromRGB(int clr) {
        int red = (clr & 0x00ff0000) >> 16;
        int green = (clr & 0x0000ff00) >> 8;
        int blue = clr & 0x000000ff;
        return new Color(red, green, blue)
    }

    public static Color getAverage(Color... colors) {
        def red = []
        def green = []
        def blue = []
        colors.each {
            red.add(it.red)
            green.add(it.green)
            blue.add(it.blue)
        }


        return new Color(
                red.sum() / red.size() as int,
                green.sum() / green.size() as int,
                blue.sum() / blue.size() as int
        )

    }

    public static Color[][] getGradient(int width, int height, Color source, Color target) {
        Color[][] result = new Color[height][width];
        Color[] line = new Color[width];
        for (int i = 0; i < width; i++) line[i] = fade(source,  target, width, i)

        for (int y = 0; y < height; y++) for (int x = 0; x < width; x++) result[y][x] = line[x]

        return result
    }

    public static void main(String[] args) {
        fade(Color.RED, Color.ORANGE, 10, 1)
    }

}
