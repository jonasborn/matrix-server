package de.jonasborn.matrix.server.app.clock

import de.jonasborn.matrix.server.app.App
import de.jonasborn.matrix.server.app.AppManager
import de.jonasborn.matrix.server.screen.Letters
import de.jonasborn.matrix.server.screen.Plane
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import java.awt.Color
import java.util.concurrent.CountDownLatch

class Clock implements App {

    Logger logger = LogManager.getLogger(App.class)

    Thread thread;
    ClockTime time = new ClockTime()
    boolean active = false

    CountDownLatch latch = new CountDownLatch(0)

    @Override
    void start(AppManager manager) {
        thread = Thread.start {
            while (true) {
                if (active) run(manager)
                Thread.sleep(1000)
            }
        }
    }

    @Override
    void activate(AppManager manager) {
        time.tick()
        draw(manager)
        active = true
    }

    @Override
    void deactivate(AppManager manager) {
        active = false
        latch.await()
    }

    private void run(AppManager manager) {
        time.tick()

        if (time.doUpdate()) {
            manager.screen.draw(7, 0, time.color, Letters.COLON)
            draw(manager)
        }

        blink(manager)

    }

    private void draw(AppManager manager) {
        latch = new CountDownLatch(1)
        logger.debug("Attempting to update")
        Plane plane = new Plane(manager.screen.plane.width, manager.screen.plane.height)
        plane.fill(time.color)

        plane.draw(0,0, Color.WHITE, time.hourFirst)
        plane.draw(4,0, Color.WHITE, time.hourSecond)

        plane.draw(10,0, Color.WHITE, time.minuteFirst)
        plane.draw(14,0, Color.WHITE, time.minuteSecond)
        manager.screen.fade(plane, 10, 100)
        latch.countDown()
    }

    private void blink(AppManager manager) {
        if (time.blink) {
            manager.screen.draw(7, 0, Color.WHITE, Letters.COLON)
        } else {
            manager.screen.draw(7, 0, time.color, Letters.COLON)
        }
        manager.screen.show()
    }
}
