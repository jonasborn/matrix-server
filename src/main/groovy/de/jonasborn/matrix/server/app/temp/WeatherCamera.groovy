package de.jonasborn.matrix.server.app.temp

import com.mashape.unirest.http.Unirest
import de.jonasborn.matrix.server.util.ColorUtils

import javax.imageio.ImageIO
import java.awt.Color

class WeatherCamera {

    private static String url = "https://app.muenchen.de/webcam/marienplatz//marienplatzgross000M.jpg"

    static Color firstColor
    static Color secondColor

    static {
        update()
        Thread.start {
            update()
            Thread.sleep(60000)
        }
    }

    private static void update() {
        def image = ImageIO.read(Unirest.get(url).asBinary().body)
        firstColor = ColorUtils.fromRGB(image.getRGB(300, 30))
        secondColor = ColorUtils.fromRGB(image.getRGB(1228, 12))
    }

    public static void main(String[] args) {
        update()
        println firstColor
    }

}
