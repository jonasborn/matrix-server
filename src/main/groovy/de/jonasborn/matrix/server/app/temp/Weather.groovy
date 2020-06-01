package de.jonasborn.matrix.server.app.temp

import de.jonasborn.matrix.server.app.App
import de.jonasborn.matrix.server.app.AppManager
import de.jonasborn.matrix.server.screen.Letters
import de.jonasborn.matrix.server.screen.Plane
import de.jonasborn.matrix.server.util.ColorUtils

import java.awt.Color

class Weather implements App {

    @Override
    void start(AppManager manager) {

    }

    @Override
    void activate(AppManager manager) {
        Color textColor = Color.WHITE
        Plane plane = new Plane(manager.screen.plane)

        plane.draw(0, 0, ColorUtils.getGradient(plane.width, plane.height, WeatherCamera.firstColor, WeatherCamera.secondColor))

        plane.draw(2, 0, textColor, WeatherTemperature.firstLetter)
        plane.draw(6, 0, textColor, WeatherTemperature.secondLetter)
        plane.draw(10, 0, textColor, Letters.DAGREE)
        plane.draw(12, 0, textColor, Letters.C)
        manager.screen.fade(plane, 7, 100)
    }

    @Override
    void deactivate(AppManager manager) {

    }
}
