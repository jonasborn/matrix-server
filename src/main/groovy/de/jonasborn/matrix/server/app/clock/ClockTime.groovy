package de.jonasborn.matrix.server.app.clock

import de.jonasborn.matrix.server.screen.Form
import de.jonasborn.matrix.server.screen.Letters
import de.jonasborn.matrix.server.util.ColorUtils
import org.joda.time.DateTime

import java.awt.Color

class ClockTime {

    DateTime current = new DateTime()
    Color color

    Form hourFirst
    Form hourSecond

    Form minuteFirst
    Form minuteSecond

    boolean blink = true

    private int lastMinute = -1
    private boolean changed = true

    public void tick() {
        current = new DateTime()
        if (current.getMinuteOfDay() != lastMinute || ((current.getSecondOfMinute() % 15) == 0)) {
            changed = true
            lastMinute = current.getMinuteOfDay()
            genColor()
            genHour()
            genMinute()
        }
        genBlink()
    }

    public void genHour() {
        def s = current.getHourOfDay().toString().padLeft(2, "0")
        hourFirst = Letters.getLetter(s[0])
        hourSecond = Letters.getLetter(s[1])
    }

    public Form[] genMinute() {
        def s = current.getMinuteOfHour().toString().padLeft(2, "0")
        minuteFirst = Letters.getLetter(s[0])
        minuteSecond = Letters.getLetter(s[1])
    }

    private void genColor() {
        StringBuilder builder = new StringBuilder()
        builder.append(current.getHourOfDay().toString().padLeft(2, "0"))
        builder.append(current.getMinuteOfHour().toString().padLeft(2, "0"))
        builder.append(current.getSecondOfMinute().toString().padLeft(2, "0"))
        color =  ColorUtils.fromHex(builder.toString())
    }

    private void genBlink() {
        this.blink = ((current.secondOfMinute().get() % 2) == 0)
    }

    public boolean doUpdate() {
        if (changed) {
            changed = false
            blink = !blink
            return true
        }
        return false
    }


}
