package core;

import entity.CountData;
import entity.Point;
import enumeration.Color;
import enumeration.Deep;
import helper.MapDriver;

import java.util.List;

public class Game {

    private GameMap gameMap;

    private CacheMap cacheMap = new CacheMap();

    private Counter counter = new Counter();

    private Result result = new Result();

    private ConsolePrinter consolePrinter = new ConsolePrinter(counter);

    private Color aiColor;

    public void init(Color[][] map) {
        gameMap = new GameMap(map);
    }

    public Point search(Color color) {
        result.reset();
        counter.clear();
        aiColor = color;
        if (LevelProcessor.win(gameMap) != null) {
            return null;
        }
        //只有一个扩展点的情形直接返回
        List<Point> points = LevelProcessor.getExpandPoints(gameMap, color, Config.searchDeep.getValue());
        if (points.size() == 1) {
            return points.get(0);
        }
        dfsScore(Config.searchDeep.getValue(), color, Integer.MAX_VALUE, 0);
        cacheMap.clear();
        return result.getPoint();
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

    private int dfsScore(int level, Color color, Integer parentMin, Integer parentMax) {
        //输赢判定
        Color winResult = LevelProcessor.win(gameMap);
        if (winResult != null) {
            if (winResult == aiColor) {
                return Integer.MAX_VALUE;
            }
            if (winResult == aiColor.getOtherColor()) {
                return Integer.MIN_VALUE;
            }
        }
        //斩杀剪枝
        if (level == 0) {
            if (color == aiColor) {
                if (ComboProcessor.canKill(gameMap, color, Config.comboDeep)) {
                    return Integer.MAX_VALUE;
                }
            }
        }
        if (level == Config.searchDeep.getValue() - Config.fullDeep + 1) {
            //谨慎处理败北的情形
            if (color != aiColor) {
                if (ComboProcessor.canKill(gameMap, color, Config.comboDeep + Config.searchDeep.getValue() - Config.fullDeep)) {
                    return Integer.MIN_VALUE;
                }
            }
        }
        //叶子分数计算
        if (level == 0) {
            return getScore();
        }
        //计算扩展节点
        List<Point> points;

        points = LevelProcessor.getExpandPoints(gameMap, color, level);

        if (points == null || points.isEmpty()) {
            return getScore();
        }
        //进度计算
        if (level == Config.searchDeep.getValue()) {
            counter.setAllStep(points.size());
        }
        //遍历扩展节点
        int extreme = color == aiColor ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (Point point : points) {
            gameMap.setColor(point, color);
            if (color == aiColor) {
                int value = dfsScore(level - 1, color.getOtherColor(), null, extreme);
                if (value > parentMin) {
                    gameMap.setColor(point, Color.NULL);
                    return value;
                }
                if (level == Config.searchDeep.getValue()) {
                    if (value >= extreme) {
                        result.add(point, value);
                    }
                    counter.plus();
                    consolePrinter.printInfo(point, value);
                }
                if (value > extreme) {
                    extreme = value;
                    //如果能赢了，则直接剪掉后面的情形
                    if (extreme == Integer.MAX_VALUE) {
                        gameMap.setColor(point, Color.NULL);
                        return extreme;
                    }
                }
            }
            if (color != aiColor) {
                int value = dfsScore(level - 1, color.getOtherColor(), extreme, null);
                if (value < parentMax) {
                    gameMap.setColor(point, Color.NULL);
                    return value;
                }
                if (value < extreme) {
                    extreme = value;
                    //如果已经输了，则直接剪掉后面的情形
                    if (extreme == Integer.MIN_VALUE) {
                        gameMap.setColor(point, Color.NULL);
                        return extreme;
                    }
                }
            }
            gameMap.setColor(point, Color.NULL);
        }
        return extreme;
    }

    private int getScore() {
        if (Config.scoreCacheEnable) {
            return cacheMap.getCacheScore(aiColor, gameMap, counter);
        }
        counter.plusCount();
        return Score.getMapScore(gameMap, aiColor);
    }
}
