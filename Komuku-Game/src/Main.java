import core.Config;
import core.Game;
import entity.CountData;
import entity.Point;
import enumeration.Color;
import helper.MapDriver;
import helper.WinChecker;

public class Main {

    private static Game game = new Game();

    private static Color[][] map = MapDriver.readMap();

    private static int progress = 0;

    private static int currentProgress = 0;

    private static Point result = null;

    private static boolean debug = true;

    private static boolean autoRun = false;

    private static boolean updateFile = true;

    private static Color aiColor = Color.WHITE;

    public static void main(String[] args) {
        System.out.println("正在初始化数据...");
        game.init(map, new Config());
        System.out.println("开始计算...");
        if (WinChecker.win(map) != null) {
            System.out.println(WinChecker.win(map) + " win");
            return;
        }
        Config.debug = debug;
        if (!debug) {
            listen();
        }
        result = game.search(aiColor).getPoint();
        System.out.println(result);
        map[result.getX()][result.getY()] = aiColor;
        if (updateFile) {
            MapDriver.printMap(map);
        }
        if (autoRun) {
            loop();
        }
    }

    private static void loop() {
        aiColor = aiColor.getOtherColor();
        main(null);
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
