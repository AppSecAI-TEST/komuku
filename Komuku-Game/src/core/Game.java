package core;

import entity.CountData;
import entity.Point;
import enumeration.Color;
import enumeration.Deep;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {

    private boolean debug = false;

    private Deep searchDeep;

    private List<Point> resultList = new ArrayList<>();

    private Point resultPoint;

    private int count = 0;

    private GameMap gameMap;

    private Counter counter = new Counter();

    public void init(Color[][] map, Deep deep) {
        gameMap = new GameMap(map);
        searchDeep = deep;
    }

    public void init(Color[][] map) {
        init(map, Deep.FOUR);
    }

    public Point search(Color color) {
        resultPoint = null;
        resultList.clear();
        counter.clear();
        count = 0;
        Deep searchDeep = this.searchDeep;
        getMaxScore(searchDeep.getValue(), color, Integer.MAX_VALUE);
        return resultPoint;
    }

    public Color win() {
        return LevelProcessor.win(gameMap);
    }

    public CountData getCountData() {
        CountData data = new CountData();
        data.setAllStep(counter.allStep);
        data.setCount(counter.count);
        data.setFinishStep(counter.finishStep);
        return data;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    private int getMaxScore(int level, Color color, int parentMin) {
        if (level == 0) {
            count++;
            return Score.getMapScore(gameMap, color);
        }
        if (LevelProcessor.win(gameMap) != null) {
            count++;
            return Score.getMapScore(gameMap, color);
        }

        int max = Integer.MIN_VALUE;
        List<Point> points = LevelProcessor.getExpandPoints(gameMap);
        if (level == searchDeep.getValue()) {
            counter.setAllStep(points.size());
        }
        for (Point point : points) {
            gameMap.setColor(point, color);
            int value = getMinScore(level - 1, color.getOtherColor(), max);
            if (value > parentMin) {
                gameMap.setColor(point, Color.NULL);
                return value;
            }
            if (level == searchDeep.getValue()) {
                if (value >= max) {
                    recordResult(point, value, max);
                }
                counter.plus();
                printInfo(point, value);
            }
            if (value >= max) {
                max = value;
            }
            gameMap.setColor(point, Color.NULL);
        }
        return max;
    }

    private int getMinScore(int level, Color color, int parentMin) {
        if (LevelProcessor.win(gameMap) != null) {
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

    private void printInfo(Point point, int value) {
        if (debug) {
            System.out.println(point.getX() + " " + point.getY() + ": " + value + " count: " + count);
        }
    }
}
