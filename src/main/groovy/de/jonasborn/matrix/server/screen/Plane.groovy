package de.jonasborn.matrix.server.screen

import com.google.common.collect.Maps

import java.awt.Color

class Plane implements Iterable<Map.Entry<Entry, Color>> {

    int width
    int height
    Map<Entry, Color> values = [:]

    Plane(Plane plane) {
        this.width = plane.width
        this.height = plane.height
        this.values = new HashMap<>(plane.values)
    }

    Plane(int width, int height, boolean prefill = true) {
        this.width = width
        this.height = height
        if (prefill) for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                set(x, y, Color.BLACK)
    }

    Plane(int width, int height, Map<Entry, Color> values) {
        this.width = width
        this.height = height
        this.values = values
    }

    public void set(int x, int y, Color color) {
        values.put(new Entry(x, y), color)
    }

    public void clear() {
        values.clear()
    }

    public void fill(Color color) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                set(x, y, color)
            }
        }
    }

    public Color get(int x, int y) {
        return values.get(new Entry(x, y))
    }

    public Color get(Entry entry) {
        return values.get(entry)
    }

    public void draw(int x, int y, Color color, Form letter) {
        Boolean[][] pattern = letter.pattern
        for (int iy = 0; iy < pattern.length; iy++) {
            for (int ix = 0; ix < pattern[iy].length; ix++) {
                if (pattern[iy][ix]) set(x + ix, y + iy, color)
            }
        }
    }

    public Plane diff(Plane plane) {
        def diff = Maps.difference(values, plane.values)
        def values = diff.entriesDiffering().collectEntries { [it.key, it.value.rightValue()] }
        return new Plane(plane.width, plane.height, values)
    }

    public static void main(String[] args) {
        Plane a = new Plane(4, 4)
        a.set(0,0, Color.CYAN)
        Plane b = new Plane(4, 4)

        println a.diff(b)

    }

    public void draw(int ax, int ay, Color[][] colors) {
        for (int y = 0; y < colors.length; y++) {
            for (int x = 0; x < colors[y].length; x++) {
                set(x, y, colors[y][x])
            }
        }
    }

    public Plane copy() {
        return new Plane(this)
    }

    @Override
    public String toString() {
        StringBuilder top = new StringBuilder()
        for (int y = 0; y < height; y++) {

            StringBuilder row = new StringBuilder()
            for (int x = 0; x < width; x++) {
                StringBuilder builder = new StringBuilder()
                def v = get(x, y)
                if (v == null) {
                    builder.append("[           ] ")
                } else {
                    builder.append("[")
                    builder.append(v.red.toString().padLeft(3))
                    builder.append("|")
                    builder.append(v.green.toString().padLeft(3))
                    builder.append("|")
                    builder.append(v.blue.toString().padLeft(3))
                    builder.append("] ")
                }
                row.append(builder)
            }
            top.append(row).append("\n")
        }
        return top.toString()
    }

    @Override
    Iterator<Map.Entry<Entry, Color>> iterator() {
        return values.iterator()
    }
}
