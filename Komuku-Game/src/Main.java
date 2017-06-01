import core.Config;
import core.Game;
import entity.CountData;
import entity.Point;
import enumeration.Color;
import enumeration.Deep;

import java.io.*;

public class Main {

    private static Game game = new Game();

    private static Color[][] map = readMap();

    private static int progress = 0;

    private static int currentProgress = 0;

    private static Point result = null;

    private static boolean debug = true;

    public static void main(String[] args) {
        System.out.println("正在初始化数据...");
        game.init(map, Deep.FOUR);
        System.out.println("开始计算...");
        if (game.win() != null) {
            System.out.println(game.win() + " win");
            return;
        }
        Config.debug = true;
        if (!debug) {
            listen();
        }
        result = game.search(Color.WHITE);
        map[result.getX()][result.getY()] = Color.WHITE;
        printMap();
    }

    private static Color[][] readMap() {
        File file = new File("Komuku-Game/src/input.txt");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = reader.readLine();
            int size = Integer.valueOf(tempString);
            Color[][] map = new Color[size][size];
            for (int i = 0; i < size; i++) {
                tempString = reader.readLine();
                for (int j = 0; j < size; j++) {
                    switch (tempString.charAt(j)) {
                        case '.':
                            map[i][j] = Color.NULL;
                            break;
                        case '□':
                            map[i][j] = Color.NULL;
                            break;
                        case '×':
                            map[i][j] = Color.BLACK;
                            break;
                        case '●':
                            map[i][j] = Color.WHITE;
                            break;
                    }
                }
            }
            reader.close();
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }
        return null;
    }

    private static void printMap() {
        try {
            PrintWriter writer = new PrintWriter(new File("Komuku-Game/src/input.txt"));
            StringBuilder content = new StringBuilder(Config.size + "\n");
            for (int i = 0; i < Config.size; i++) {
                for (int j = 0; j < Config.size; j++) {
                    if (map[i][j] == Color.NULL) {
                        content.append('.');
                    }
                    if (map[i][j] == Color.BLACK) {
                        content.append('×');
                    }
                    if (map[i][j] == Color.WHITE) {
                        content.append('●');
                    }
                }
                content.append("\n");
            }
            content.append("●×");
            writer.write(content.toString());
            writer.close();
        } catch (Exception ignored) {
        }
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
                    Thread.sleep(1000);
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
