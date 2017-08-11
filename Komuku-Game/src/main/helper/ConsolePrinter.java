package helper;

import core.Config;
import entity.Counter;
import core.GameMap;
import entity.Point;
import enumeration.Color;

import java.util.Date;

public class ConsolePrinter {

    private long debugTime = new Date().getTime();

    private Counter counter;

    public void init(Counter counter) {
        this.counter = counter;
    }

    public void printInfo(Point point, int value) {
        if (Config.debug) {
            System.out.println(String.format("%s %s: %s count: %s combo count: %s combo hit %s time: %s ms", point.getX(), point.getY(), value, counter.count, counter.countCombo, counter.comboCacheHit,
                    new Date().getTime() - debugTime));
        }
    }

    public static void printMap(GameMap gameMap) {
        for (int i = 0; i < Config.size; i++) {
            for (int j = 0; j < Config.size; j++) {
                if (gameMap.getMap()[i][j] == Color.NULL) {
                    System.out.print('□');
                }
                if (gameMap.getMap()[i][j] == Color.BLACK) {
                    System.out.print('×');
                }
                if (gameMap.getMap()[i][j] == Color.WHITE) {
                    System.out.print('●');
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
