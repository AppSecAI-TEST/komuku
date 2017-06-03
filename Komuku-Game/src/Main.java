import core.Config;
import core.Game;
import entity.CountData;
import entity.Point;
import enumeration.Color;
import enumeration.Deep;
import helper.MapDriver;

import java.io.*;

public class Main {

    private static Game game = new Game();

    private static Color[][] map = MapDriver.readMap();

    private static int progress = 0;

    private static int currentProgress = 0;

    private static Point result = null;

    private static boolean debug = false;

    public static void main(String[] args) {
        System.out.println("正在初始化数据...");
        game.init(map);
        System.out.println("开始计算...");
        if (game.win() != null) {
            System.out.println(game.win() + " win");
            return;
        }
        Config.debug = debug;
        if (!debug) {
            listen();
        }
        result = game.search(Color.WHITE);
        map[result.getX()][result.getY()] = Color.WHITE;
        MapDriver.printMap(map);
    }

    private static void listen() {
        new Thread(() -> {
            while (true) {
                CountData data = game.getCountData();
                if (data.getAllStep() > 0 && progress == 0) {
                    progress = data.getAllStep();
                    for (int i = 0; i < progress; i++) {
                        System.out.print("=");
                    }
                    System.out.println();
                }
                if (data.getFinishStep() > currentProgress) {
                    for (int i = 0; i < data.getFinishStep() - currentProgress; i++) {
                        System.out.print(">");
                    }
                    currentProgress = data.getFinishStep();
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (progress == currentProgress && progress > 0) {
                    System.out.println();
                    System.out.println(result.getX() + " " + result.getY());
                    return;
                }
            }
        }).start();
    }

}
