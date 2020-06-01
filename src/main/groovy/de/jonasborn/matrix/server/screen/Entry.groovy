package de.jonasborn.matrix.server.screen

class Entry {
    int x
    int y

    Entry(int x, int y) {
        this.x = x
        this.y = y
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        Entry entry = (Entry) o

        if (x != entry.x) return false
        if (y != entry.y) return false

        return true
    }

    int hashCode() {
        int result
        result = x
        result = 31 * result + y
        return result
    }


    @Override
    public String toString() {
        return "Entry{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}