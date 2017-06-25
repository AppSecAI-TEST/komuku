package helper;

import core.Config;
import core.Game;
import core.GameMap;
import enumeration.Color;

import java.io.*;

public class MapDriver {

    public static Color[][] readMap() {
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

    public static void printMap(Color[][] map) {
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

    public static void printToConsole(GameMap gameMap) {
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
    }
}
