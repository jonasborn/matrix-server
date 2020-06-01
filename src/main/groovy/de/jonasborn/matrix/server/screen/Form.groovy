package de.jonasborn.matrix.server.screen

class Form {

    int width
    int height
    Boolean[][] pattern

    Form(int width, int height, Boolean[][] pattern) {
        this.width = width
        this.height = height
        this.pattern = pattern
    }


    @Override
    public String toString() {
        StringBuilder total = new StringBuilder()
        for (int y = 0; y < height; y++) {
            StringBuilder row = new StringBuilder()
            for (int x = 0; x < width; x++) {
                if (y < pattern.length) {
                    if (x < pattern[y].length) {
                        if (pattern[y][x]) {
                            row.append("#")
                        } else {
                            row.append("_")
                        }
                    }else {
                        row.append("_")
                    }
                }else {
                    row.append("_")
                }
            }
            total.append(row).append("\n")
        }
        return total.toString()
    }

}
