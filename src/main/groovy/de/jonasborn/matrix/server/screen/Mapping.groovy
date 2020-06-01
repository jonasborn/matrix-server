package de.jonasborn.matrix.server.screen

import java.awt.Point

class Mapping  {

    private Map<Integer, List<Integer>> delegate = [:];
    int width
    int height
    {
        delegate.put(0, [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17])
        delegate.put(1, [34,33,32,31,30,29,28,27,26,25,24,23,22,21,20,19,18])
        delegate.put(2, [35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51])
        delegate.put(3, [68,67,66,65,64,63,62,61,60,59,58,57,56,55,54,53,52])
        delegate.put(4, [69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85])
    }

    Mapping() {
        height = delegate.size()
        width = delegate.collect {it.value.size()}.max()
    }

    public Entry get(int i) {

        for (int y = 0; y < delegate.size(); y++) {
            for (int x = 0; x < delegate.get(y).size(); x++) {
                if (delegate.get(y).get(x) == i) return new Entry(x, y)
            }
        }
        return null
    }

    public Integer get(int x, int y) {
        def list = delegate.get(y)
        if (list == null) return null
        return list.get(x)
    }

    public Integer get(Entry entry) {
        return get(entry.x, entry.y)
    }




}
