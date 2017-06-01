package core;

import entity.Point;

import java.util.Date;

class ConsolePrinter {

    private long debugTime = new Date().getTime();

    private Counter counter;

    ConsolePrinter(Counter counter) {
        this.counter = counter;
    }

    void printInfo(Point point, int value) {
        if (Config.debug) {
            System.out.println(point.getX() + " " + point.getY() + ": " + value + " count: " + counter.getCount() + " time: " + ((new Date().getTime() - debugTime)) + "ms");
        }
    }
}
