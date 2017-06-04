package core;

import entity.CountData;
import entity.Point;
import enumeration.Color;
import enumeration.ComboDeep;
import enumeration.SearchType;

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
        dfsScore(Config.searchDeep.getValue(), color, Integer.MAX_VALUE, 0, SearchType.NORMAL);
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

    private int dfsScore(int level, Color color, Integer parentMin, Integer parentMax, SearchType searchType) {
        //叶子分数计算
        if (level == 0) {
            if (Config.scoreCacheEnable) {
                return cacheMap.getCacheScore(aiColor, gameMap, counter);
            }
            if (searchType == SearchType.NORMAL) {
                return dfsScore(Config.comboDeep.getValue(), aiColor, Integer.MAX_VALUE, 0, SearchType.COMBO);
            }
            return getScore();
        }
        //输赢判定
        if (LevelProcessor.win(gameMap) != null) {
            return getScore();
        }
        //计算扩展节点
        List<Point> points = searchType == SearchType.NORMAL ?
                LevelProcessor.getExpandPoints(gameMap) :
                LevelProcessor.getComboPoints(gameMap, color);
        if (points == null || points.isEmpty()) {
            return getScore();
        }
        //进度计算
        if (level == Config.searchDeep.getValue() && searchType == SearchType.NORMAL) {
            counter.setAllStep(points.size());
        }
        //遍历扩展节点
        int extreme = color == aiColor ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (Point point : points) {
            gameMap.setColor(point, color);
            if (color == aiColor) {
                int value = dfsScore(level - 1, color.getOtherColor(), null, extreme, searchType);
                if (value > parentMin) {
                    gameMap.setColor(point, Color.NULL);
                    return value;
                }
                if (level == Config.searchDeep.getValue() && searchType == SearchType.NORMAL) {
                    if (value >= extreme) {
                        result.add(point, value);
                    }
                    counter.plus();
                    consolePrinter.printInfo(point, value);
                }
                if (value > extreme) {
                    extreme = value;
                }
            }
            if (color != aiColor) {
                int value = dfsScore(level - 1, color.getOtherColor(), extreme, null, searchType);
                if (value < parentMax) {
                    gameMap.setColor(point, Color.NULL);
                    return value;
                }
                if (value < extreme) {
                    extreme = value;
                }
            }
            gameMap.setColor(point, Color.NULL);
        }
        return extreme;
    }

    private int getScore() {
        counter.plusCount();
        return Score.getMapScore(gameMap, aiColor);
    }
}
