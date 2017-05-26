package core;

import entity.Point;
import enumeration.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {

    private boolean debug = false;

    private int searchDeep = 4;

    private List<Point> resultList = new ArrayList<>();

    private Point resultPoint;

    private int count = 0;

    private GameMap gameMap;

    public void init(Color[][] map) {
        gameMap = new GameMap(map);
    }

    public Point search(Color color) {
        resultPoint = null;
        resultList.clear();
        count = 0;
        getMaxScore(searchDeep, color, Integer.MAX_VALUE);
        return resultPoint;
    }

    private int getMaxScore(int level, Color color, int parentMin) {
        if (level == 0) {
            count++;
            return Score.getMapScore(gameMap, color);
        }
        if (LevelProcessor.win(gameMap)) {
            count++;
            return Score.getMapScore(gameMap, color);
        }

        int max = Integer.MIN_VALUE;
        List<Point> points = LevelProcessor.getExpandPoints(gameMap);
        for (Point point : points) {
            gameMap.setColor(point, color);
            int value = getMinScore(level - 1, color.getOtherColor(), max);
            if (value > parentMin) {
                gameMap.setColor(point, Color.NULL);
                return value;
            }
            if (level == searchDeep) {
                if (value >= max) {
                    recordResult(point, value, max);
                }
                printInfo(point, points, value);
            }
            if (value >= max) {
                max = value;
            }
            gameMap.setColor(point, Color.NULL);
        }
        return max;
    }

    private int getMinScore(int level, Color color, int parentMin) {
        if (LevelProcessor.win(gameMap)) {
            count++;
            return Score.getMapScore(gameMap, color.getOtherColor());
        }

        int min = Integer.MAX_VALUE;
        List<Point> points = LevelProcessor.getExpandPoints(gameMap);
        for (Point point : points) {
            gameMap.setColor(point, color);
            int value = getMaxScore(level - 1, color.getOtherColor(), min);
            if (value < parentMin) {
                gameMap.setColor(point, Color.NULL);
                return value;
            }
            if (value < min) {
                min = value;
            }
            gameMap.setColor(point, Color.NULL);
        }
        return min;
    }

    private void recordResult(Point point, int value, int max) {
        if (value > max) {
            resultList.clear();
        }
        resultList.add(point);
        resultPoint = resultList.get(new Random().nextInt(resultList.size()));
    }

    private void printInfo(Point point, List<Point> points, int value) {
        if (debug) {
            System.out.printf(point.getX() + " " + point.getY() + ": " + value + " count: " + count);
        } else {
            int index = points.indexOf(point);
            if (index == 0) {
                points.forEach(p -> System.out.print("="));
                System.out.println();
            }
            System.out.print(">");
        }
    }
}
