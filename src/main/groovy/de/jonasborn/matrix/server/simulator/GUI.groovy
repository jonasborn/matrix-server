package de.jonasborn.matrix.server.simulator

import com.google.common.primitives.Ints
import com.google.common.primitives.Shorts
import de.jonasborn.matrix.server.screen.Bridge
import de.jonasborn.matrix.server.screen.Letters
import de.jonasborn.matrix.server.screen.Mapping
import de.jonasborn.matrix.server.screen.Plane
import de.jonasborn.matrix.server.screen.Screen
import de.jonasborn.matrix.server.util.ColorUtils
import de.jonasborn.matrix.server.util.NumberUtils

import javax.swing.ImageIcon
import javax.swing.JFrame
import javax.swing.JLabel
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage

class GUI implements Bridge {

    Mapping mapping = new Mapping()
    int width = 80
    int height = 80
    Graphics2D graphics
    BufferedImage image;
    ImageIcon icon
    JLabel label

    GUI() {

        int imageWidth = mapping.width * width
        int imageHeight = mapping.height * height

        JFrame frame = new JFrame("matrix")

        image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB)
        graphics = image.graphics as Graphics2D
        graphics.setColor(Color.BLACK)
        graphics.fillRect(0, 0, image.width, image.height)

        frame.setSize(image.width + width, image.height + height)
        icon = new ImageIcon(image);
        label = new JLabel(icon);
        frame.add(label)
        frame.show(true)

    }

    public void set(int id, Color color) {
        def e = mapping.get(id)
        def x = e.x
        def y = e.y
        //println "Drawing $x|$y -> $id";
        graphics.setColor(color)
        graphics.fillRect(e.x * width, e.y * height, width, height)
    }


    public static void main(String[] args) {
        def gui = new GUI()
        def s = new Screen(new Mapping(), gui)
        s.write("hallo hong", 100, ColorUtils.fromHex("55CBD3"), ColorUtils.fromHex("C7DAC7"), false)

        Plane p = s.plane.copy()
        p.fill(Color.GREEN)
        s.fade(p, 10, 100)
    }

    public static void mainb(String[] args) {
        def gui = new GUI()
        def s = new Screen(new Mapping(), gui)

        def r = s.plane.copy()
        while (true) {
            r.draw(0,0, Color.BLUE, Letters.a)
            r.draw(4,0, Color.RED, Letters.a)
            r.draw(8,0, Color.GREEN, Letters.a)
            s.fade(r, 10, 100)

            r.draw(0,0, Color.GREEN, Letters.a)
            r.draw(4,0, Color.BLUE, Letters.a)
            r.draw(8,0, Color.RED, Letters.a)
            s.fade(r, 10, 100)

            r.draw(0,0, Color.RED, Letters.a)
            r.draw(4,0, Color.GREEN, Letters.a)
            r.draw(8,0, Color.BLUE, Letters.a)
            s.fade(r, 10, 100)

        }
    }

    public static void mains(String[] args) {
        def gui = new GUI()
        def s = new Screen(new Mapping(), gui)


        int last = -1

        long distance = System.currentTimeMillis()

        while (true) {


            def min = new Date().minutes
            Color color = ColorUtils.fade(Color.RED, Color.BLUE, 60, new Date().seconds)
            //if (last != min || System.currentTimeMillis() - distance > 5000) {
            //    distance = System.currentTimeMillis()
            s.clear()
            //    last = min
            def minutes = NumberUtils.split(min)
            if (minutes.length < 2) {
                s.draw(14, 0, color, Letters.getLetter(minutes[0]))
            } else {
                s.draw(10, 0, color, Letters.getLetter(minutes[0]))
                s.draw(14, 0, color, Letters.getLetter(minutes[1]))
            }

            def hour = NumberUtils.split(new Date().hours)

            if (hour.length < 2) {
                s.draw(4, 0, color, Letters.getLetter(hour[0]))
            } else {
                s.draw(0, 0, color, Letters.getLetter(hour[0]))
                s.draw(4, 0, color, Letters.getLetter(hour[1]))
            }
            //}

            if ((new Date().seconds % 2) == 0) {
                s.draw(7, 0, color, Letters.doublepoint)
            } else {
                s.draw(7, 0, Color.BLACK, Letters.doublepoint)
            }

            s.show()

            Thread.sleep(1000)


        }

    }

    int counter = 0;

    @Override
    void receive(byte[] data) {

        if (data[0] == 0x04 as byte) {
            Integer id = Ints.fromBytes(data[1], data[2], data[3], data[4])
            Short red = Shorts.fromBytes(data[5], data[6])
            Short green = Shorts.fromBytes(data[7], data[8])
            Short blue = Shorts.fromBytes(data[9], data[10])
            def color = new Color(red, green, blue)
            set(id, color)
            counter++
        }
        if (data[0] == 0x01 as byte) {
            graphics.setColor(Color.BLACK)
            graphics.fillRect(0, 0, image.width, image.height)
        }
        if (data[0] == 0x02 as byte) {
            println "Accepted " + counter + " elements"
            label.repaint()
            counter = 0;
        }
    }
}
