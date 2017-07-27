package core;

import entity.Point;
import enumeration.Color;
import helper.MapDriver;

import java.util.HashMap;
import java.util.List;

class Score {

    static int getMapScore(GameMap gameMap, Color color) {
        //f[i][j][k] 表示在以 i j 为起点的坐标 k 为方向，连续5个节点的统计量
        int[][][] currentColorCount = new int[Config.size][Config.size][Config.size];


        int value = 0;
        for (int i = 0; i < Config.size; i++)
            for (int j = 0; j < Config.size; j++) {
                Point point = new Point(i, j);
                value += getMultipleCloseValue(gameMap, color, point);
                value += getOneValue(gameMap, color, point);
                value += getTwoValue(gameMap, color, point);
                value += getThreeValue(gameMap, color, point);
                value += getFourValue(gameMap, color, point);
                value += getFiveValue(gameMap, color, point);
            }
        return value;
    }

    static private int getMultipleCloseValue(GameMap gameMap, Color color, Point point) {
        Color headColor = gameMap.getColor(point);
        if (gameMap.getColor(point) == Color.NULL) {
            return 0;
        }

        int value = 0;

        for (int direct = 0; direct < 8; direct++) {
            List<Point> points = gameMap.getLinePoints(point, direct, 5);
            if (points == null) {
                continue;
            }
            List<Color> colors = gameMap.getColors(points);
            Color startColor = colors.get(1);
            if (startColor != headColor.getOtherColor()) {
                continue;
            }
            int colorCount = 1;
            int otherColorCount = 0;
            for (int i = 2; i <= 5; i++) {
                Color midColor = colors.get(i);
                if (midColor == startColor.getOtherColor()) {
                    otherColorCount++;
                    break;
                }
                if (midColor == startColor) {
                    colorCount++;
                }
            }
            if (otherColorCount > 0) {
                continue;
            }
            if (colorCount == 1) {
                value += 1;
            }
            if (colorCount == 2) {
                value += 10;
            }
            if (colorCount == 3) {
                value += 50;
            }
            if (colorCount == 4) {
                value += 100;
            }
        }

        if (headColor == color) {
            value = -value;
        }
        return value;
    }

    static private int getOneValue(GameMap gameMap, Color color, Point point) {
        return 0;
    }

    static private int getTwoValue(GameMap gameMap, Color color, Point point) {
        Color headColor = gameMap.getColor(point);
        if (gameMap.getColor(point) == Color.NULL) {
            return 0;
        }

        int value = 0;

        for (int direct = 0; direct < 8; direct++) {
            List<Point> points = gameMap.getLinePoints(point, direct, 5);
            if (points == null) {
                continue;
            }
            List<Color> colors = gameMap.getColors(points);
            Color startColor = colors.get(1);
            if (startColor != headColor.getOtherColor()) {
                continue;
            }
            int colorCount = 1;
            int otherColorCount = 0;
            for (int i = 2; i <= 5; i++) {
                Color midColor = colors.get(i);
                if (midColor == startColor.getOtherColor()) {
                    otherColorCount++;
                    break;
                }
                if (midColor == startColor) {
                    colorCount++;
                }
            }
            if (otherColorCount > 0) {
                continue;
            }
            if (colorCount == 1) {
                value += 1;
            }
            if (colorCount == 2) {
                value += 10;
            }
            if (colorCount == 3) {
                value += 50;
            }
            if (colorCount == 4) {
                value += 100;
            }
        }

        if (headColor == color) {
            value = -value;
        }
        return value;
    }

    static private int getThreeValue(GameMap gameMap, Color color, Point point) {
        return 0;
    }

    static private int getFourValue(GameMap gameMap, Color color, Point point) {
        return 0;
    }

    static private int getFiveValue(GameMap gameMap, Color color, Point point) {
        return 0;
    }

    static public void main(String[] args) {
        Color[][] colors = MapDriver.readMap();
        GameMap gameMap = new GameMap(colors);
        System.out.println(getMapScore(gameMap, Color.BLACK));
    }
}
