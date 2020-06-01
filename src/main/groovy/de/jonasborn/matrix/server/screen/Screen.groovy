package de.jonasborn.matrix.server.screen


import de.jonasborn.matrix.server.Buff
import de.jonasborn.matrix.server.util.ColorUtils

import java.awt.Color

class Screen {

    Mapping mapping
    Bridge bridge
    Plane plane

    Screen(Mapping mapping, Bridge bridge) {
        this.mapping = mapping
        this.bridge = bridge
        plane = new Plane(mapping.width, mapping.height)
        /*for (int x = 0; x < mapping.width; x++)
            for (int y = 0; y < mapping.height; y++)
                plane.set(x, y, Color.BLACK)*/

    }

    public void set(int x, int y, Color color) {
        Integer id = mapping.get(x, y)
        //println "Sending $x|$y -> $id";
        Buff buff = new Buff(11)
        buff.add(0x04 as byte)
        buff.addInt(id)
        buff.addShort(color.red)
        buff.addShort(color.green)
        buff.addShort(color.blue)
        bridge.receive(buff.get())
        plane.set(x, y, color)
    }

    public void set(Entry entry, Color color) {
        this.set(entry.x, entry.y, color)
    }

    public void draw(int x, int y, Color color, Form letter) {
        Boolean[][] pattern = letter.pattern
        for (int iy = 0; iy < pattern.length; iy++) {
            for (int ix = 0; ix < pattern[iy].length; ix++) {
                if (pattern[iy][ix]) set(x + ix, y + iy, color)
            }
        }
    }

    public void clear() {
        bridge.receive([0x01] as byte[])
        for (int x = 0; x < mapping.width; x++)
            for (int y = 0; y < mapping.height; y++)
                plane.set(x, y, Color.BLACK)
    }


    public void fill(Color color) {
        for (int x = 0; x < mapping.width; x++)
            for (int y = 0; y < mapping.height; y++)
                set(x, y, color)
    }


    public void show() {
        bridge.receive([0x02] as byte[])
    }

    public void fadeStep(Plane target, int steps, int step) {
        def plane = new Plane(this.plane)
        def diff = plane.diff(target)
        Map<Entry, Color> values = diff.collectEntries {
            def before = plane.get(it.key)
            def after = it.value
            return [it.key, ColorUtils.fade(before, after, steps, step)]
        }
        values.each { set(it.key, it.value) }
    }

    public void fade(Plane plane, int steps, long timeout) {
        for (int i = 0; i < steps; i++) {
            fadeStep(plane, steps, i)
            show()
            sleep(timeout)
        }
        this.plane = new Plane(plane)
    }

    private Boolean[][] combine(List<Form> letters) {
        def width = letters.collect { it.width }.sum() + letters.size() - 1
        def height = letters.collect { it.height }.max()
        Boolean[][] pattern = new Boolean[height][width]

        int x = 0;
        letters.each {
            for (int iy = 0; iy < it.pattern.length; iy++) {
                for (int ix = 0; ix < it.pattern[iy].length; ix++) {
                    pattern[iy][x + ix] = it.pattern[iy][ix]
                }
            }
            x += it.width + 1
        }
        return pattern
    }

    public void write(String text, long timeout, Color textColor, Color bgrColor, boolean doFade = false) {
        Boolean[][] pattern = combine(Letters.getLetters("     " + text))
        def max = pattern.collect { it.length }.max()
        for (int round = 0; round < (max + mapping.width); round++) {
            Plane plane = new Plane(mapping.width, mapping.height)
            if (doFade) {
                plane.fill(bgrColor)
            } else {
                fill(bgrColor)
            }
            for (int y = 0; y < mapping.height; y++) {
                for (int x = 0; x < mapping.width; x++) {
                    if (y < pattern.length && round + x < pattern[y].length) {
                        if (pattern[y][round + x]) {
                            if (doFade) {
                                plane.set(x, y, textColor)
                            } else {
                                set(x, y, textColor)
                            }
                        }
                    }
                }
            }

            if (doFade) {
                fade(plane, 6, (timeout / 6) as int)
            } else {
                show()
            }
            Thread.sleep(timeout)
        }
    }


    public static void main(String[] args) {
        def s = new Screen(new Mapping(), null)

    }
}
