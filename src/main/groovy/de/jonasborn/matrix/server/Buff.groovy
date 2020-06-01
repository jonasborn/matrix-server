package de.jonasborn.matrix.server

import com.google.common.primitives.Ints
import com.google.common.primitives.Shorts

class Buff {

    int size
    private byte[] buffer
    private int pos = 0
    Buff(int size) {
        this.size = size
        buffer = new byte[size]
    }

    public void addInt(Number number) {
        def bytes = Ints.toByteArray(number as int)
        add(bytes[0])
        add(bytes[1])
        add(bytes[2])
        add(bytes[3])
    }

    public void addShort(Number number) {
        def bytes = Shorts.toByteArray(number as short)
        add(bytes[0])
        add(bytes[1])
    }

    public void add(byte b) {
        buffer[pos] = b
        pos++
    }

    public byte[] get() {
        return buffer
    }


    @Override
    public String toString() {
        println pos
        return Arrays.deepToString(buffer)
    }
}
