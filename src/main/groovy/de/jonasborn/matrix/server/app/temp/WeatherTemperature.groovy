package de.jonasborn.matrix.server.app.temp

import com.mashape.unirest.http.Unirest
import de.jonasborn.matrix.server.screen.Form
import de.jonasborn.matrix.server.screen.Letters
import de.jonasborn.matrix.server.util.ColorUtils

import java.awt.Color

class WeatherTemperature {

    //api.openweathermap.org/data/2.5/weather?lat=35&lon=139

    static Color foregroundColor
    static Color backgroundColor
    static Form firstLetter
    static Form secondLetter

    static {
        refresh()
        Thread.start {
            Thread.sleep(60000)
            refresh()
        }
    }

    private static void refresh() {
        def json = Unirest.get("https://api.openweathermap.org/data/2.5/weather?")
                .queryString("lat", "48.137154")
                .queryString("lon", "11.576124")
                .queryString("appid", "ca3c441f740019652f709224cbeace45")
                .asJson().body;
        println json
        def id = json.object.getJSONArray("weather").getJSONObject(0).getString("icon")
        def temp = json.object.getJSONObject("main").getDouble("temp")
        temp = temp - 273.15;
        temp = Math.round(temp) as int
        def letters = Letters.getLetters(temp.toString())
        firstLetter = letters[0]
        secondLetter = letters[1]
        set(id)
    }


    public static enum Mode {
        N01("n01", "#6a89cc", "#82ccdd"), //clear
        D01("d01", "82ccdd", "#6a89cc"), //clear

        N02("02n", "#4a69bd", "#60a3bc"),
        D02("02d", "#60a3bc", "#4a69bd"),

        N03("03n", "#1e3799", "#3c6382"),
        D03("03d", "#3c6382", "#1e3799"),

        N04("04n", "#0c2461", "#0a3d62"),
        D04("04d", "#0a3d62", "#0c2461"),

        N09("09n", "#ffffff", "#dfe4ea"),
        D09("09d", "#dfe4ea", "#ffffff"),

        N10("10n", "#f1f2f6", "#ced6e0"),
        D10("10d", "#ced6e0", "#f1f2f6"),

        N11("11n", "#a4b0be", "#57606f"), //thunder
        D11("11d", "#57606f", "#a4b0be"), //thunder

        D13("13d", "#dfe4ea", "#ffffff"), //snow
        N13("13n", "#ffffff", "#dfe4ea"), //snow

        D50("50d", "#dfe4ea", "#f1f2f6"), //mist
        N50("50n", "#f1f2f6", "#dfe4ea") //mist

        static Mode fromName(String name) {
            return values().find { it.name == name }
        }

        String name
        String foreground
        String background

        Mode(String name, String foreground, String background) {
            this.name = name
            this.foreground = foreground
            this.background = background
        }
    }

    public static void set(String id) {
        Mode mode = Mode.fromName(id)
        foregroundColor = Color.WHITE//ColorUtils.fromHex(mode.foreground)
        backgroundColor = ColorUtils.fromHex(mode.background)
    }

    public static void main(String[] args) {
        refresh()
    }
}
