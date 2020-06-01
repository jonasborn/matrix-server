package de.jonasborn.matrix.server

import de.jonasborn.matrix.server.app.AppManager
import de.jonasborn.matrix.server.app.clock.Clock
import de.jonasborn.matrix.server.app.temp.Weather
import de.jonasborn.matrix.server.screen.Mapping
import de.jonasborn.matrix.server.screen.Screen
import de.jonasborn.matrix.server.simulator.GUI

import java.awt.Color

class Main {

    public static void main(String[] args) {
        GUI gui = new GUI()
        Screen screen = new Screen(new Mapping(), gui)
        screen.fill(Color.DARK_GRAY)

        AppManager manager = new AppManager(screen)

        Clock clock = new Clock()
        Weather weather = new Weather()


        while (true) {
            clock.start(manager)
            clock.activate(manager)

            Thread.sleep(12000)
            clock.deactivate(manager)

            weather.activate(manager)

            Thread.sleep(6000)
        }


    }

}
