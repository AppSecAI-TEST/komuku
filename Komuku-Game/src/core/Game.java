package core;

import entity.AnalyzedData;
import entity.CountData;
import entity.Point;
import enumeration.Color;
import enumeration.Deep;
import helper.MapDriver;

import java.util.List;

public class Game {

    private GameMap gameMap;

    private Counter counter = new Counter();

    private Result result = new Result();

    private ConsolePrinter consolePrinter = new ConsolePrinter(counter);

    private Color aiColor;

    private Config config;

    private Score score = new Score();

    public void init(Color[][] map, Config config) {
        gameMap = new GameMap(map);
        this.config = config;
    }

    public Result search(Color color) {
        result.reset();
        counter.clear();
        aiColor = color;
        if (WinChecker.win(gameMap) != null) {
            return null;
        }
        //积分预处理
        score.init(gameMap, aiColor);
        //只有一个扩展点的情形直接返回
        Analyzer data = new Analyzer(gameMap, aiColor, color, gameMap.getNeighbor(color), score);
        List<Point> points = LevelProcessor.getExpandPoints(gameMap, data, config.searchDeep.getValue(), config.searchDeep.getValue());
        if (points.size() == 1) {
            result.add(points.get(0), 0);
            return result;
        }
        dfsScore(config.searchDeep.getValue(), color, Integer.MAX_VALUE, 0);
        return result;
    }

    public Color win() {
        return WinChecker.win(gameMap);
    }

    public CountData getCountData() {
        CountData data = new CountData();
        data.setAllStep(counter.allStep);
        data.setCount(counter.count);
        data.setFinishStep(counter.finishStep);
        return data;
    }

    private int dfsScore(int level, Color color, Integer parentMin, Integer parentMax) {
        //斩杀剪枝
        if (level == 0) {
            if (color == aiColor) {
                if (ComboProcessor.canKill(gameMap, color, config.comboDeep, score)) {
                    return Integer.MAX_VALUE;
                }
            }
        }
        if (level == 1) {
            //谨慎处理败北的情形
            if (color != aiColor) {
                if (ComboProcessor.canKill(gameMap, color, config.comboDeep + 2, score)) {
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
        Analyzer data = new Analyzer(gameMap, color, aiColor, gameMap.getNeighbor(color), score);
        //输赢判定
        if (!data.getFiveAttack().isEmpty()) {
            if (color == aiColor) {
                return Integer.MAX_VALUE;
            }
            if (color == aiColor.getOtherColor()) {
                return Integer.MIN_VALUE;
            }
        }
        points = LevelProcessor.getExpandPoints(gameMap, data, level, config.searchDeep.getValue());

        if (points == null || points.isEmpty()) {
            return getScore();
        }
        //进度计算
        if (level == config.searchDeep.getValue()) {
            counter.setAllStep(points.size());
        }
        //遍历扩展节点
        int extreme = color == aiColor ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (Point point : points) {
            setColor(point, color, Color.NULL, aiColor);
            if (color == aiColor) {
                int value = dfsScore(level - 1, color.getOtherColor(), null, extreme);
                if (value > parentMin) {
                    setColor(point, Color.NULL, color, aiColor);
                    return value;
                }
                if (level == config.searchDeep.getValue()) {
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
                        setColor(point, Color.NULL, color, aiColor);
                        return extreme;
                    }
                }
            }
            if (color != aiColor) {
                int value = dfsScore(level - 1, color.getOtherColor(), extreme, null);
                if (value < parentMax) {
                    setColor(point, Color.NULL, color, aiColor);
                    return value;
                }
                if (value < extreme) {
                    extreme = value;
                    //如果已经输了，则直接剪掉后面的情形
                    if (extreme == Integer.MIN_VALUE) {
                        setColor(point, Color.NULL, color, aiColor);
                        return extreme;
                    }
                }
            }
            setColor(point, Color.NULL, color, aiColor);
        }
        return extreme;
    }

    private void setColor(Point point, Color color, Color forwardColor, Color aiColor) {
        score.setColor(point, color, forwardColor, aiColor);
        gameMap.setColor(point, color);
    }

    private int getScore() {
        counter.plusCount();
        return score.getMapScore();
    }
}
