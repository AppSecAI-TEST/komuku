package core;

import entity.Counter;
import entity.Point;
import enumeration.Color;
import helper.ConsolePrinter;
import helper.MapDriver;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ComboProcessor {

    private GameMap gameMap;

    private Score score;

    private Counter counter;

    private Config config;

    private Cache cache;

    public void init(GameMap gameMap, Score score, Counter counter, Config config, Cache cache) {
        this.gameMap = gameMap;
        this.score = score;
        this.counter = counter;
        this.config = config;
        this.cache = cache;
    }

    boolean canKill(Color targetColor) {
        return dfsKill(gameMap, targetColor, targetColor, config.comboDeep, score);
    }

    private boolean dfsKill(GameMap gameMap, Color color, Color targetColor, int level, Score score) {
        //缓存
        Boolean cacheResult = cache.getComboResult();
        if (cacheResult != null) {
            return cacheResult;
        }

        if (level == 0) {
            counter.countCombo++;
            return returnValue(false);
        }
        List<Point> rangePoints;
        rangePoints = gameMap.getNeighbor();
        Analyzer data = new Analyzer(gameMap, color, rangePoints, score);
        if (color == targetColor) {
            if (data.getFiveAttack().size() > 0) {
                counter.countCombo++;
                return returnValue(true);
            }
            List<Point> points = getComboAttackPoints(data);
            for (Point point : points) {
                setColor(point, color, Color.NULL, targetColor, score, gameMap);
                boolean value = dfsKill(gameMap, color.getOtherColor(), targetColor, level - 1, score);
                if (value) {
                    setColor(point, Color.NULL, color, targetColor, score, gameMap);
                    return returnValue(true);
                }
                setColor(point, Color.NULL, color, targetColor, score, gameMap);
            }
            return returnValue(false);
        } else {
            if (data.getFiveAttack().size() > 0) {
                return returnValue(false);
            }
            List<Point> points = getComboDefencePoints(data);
            //如果没有能防的则结束
            if (points.size() == 0) {
                return returnValue(false);
            }
            for (Point point : points) {
                setColor(point, color, Color.NULL, targetColor, score, gameMap);
                boolean value = dfsKill(gameMap, color.getOtherColor(), targetColor, level - 1, score);
                if (!value) {
                    setColor(point, Color.NULL, color, targetColor, score, gameMap);
                    return returnValue(false);
                }
                setColor(point, Color.NULL, color, targetColor, score, gameMap);
            }
            return returnValue(true);
        }
    }

    private List<Point> getComboAttackPoints(Analyzer data) {
        //如果有对方冲4，则防冲4
        if (!data.getFourDefence().isEmpty()) {
            return new ArrayList<>(data.getFourDefence());
        }
        //如果有对方活3，冲四
        if (!data.getThreeDefence().isEmpty()) {
            return new ArrayList<>(data.getFourAttack());
        }
        List<Point> result = new ArrayList<>();
        result.addAll(data.getFourAttack());
        result.addAll(data.getThreeOpenAttack());
        return result;
    }

    private List<Point> getComboDefencePoints(Analyzer data) {
        //如果有对方冲4，则防冲4
        if (!data.getFourDefence().isEmpty()) {
            return new ArrayList<>(data.getFourDefence());
        }
        //如果有对方活3，则防活3或者冲四
        if (!data.getThreeDefence().isEmpty()) {
            return new ArrayList<Point>(data.getFourAttack()) {{
                addAll(data.getThreeDefence());
            }};
        }
        return new ArrayList<>();
    }

    private boolean returnValue(boolean value) {
        cache.recordComboResult(value);
        return value;
    }

    private void setColor(Point point, Color color, Color forwardColor, Color aiColor, Score score, GameMap gameMap) {
        score.setColor(point, color, forwardColor, aiColor);
        gameMap.setColor(point, color);
    }

    public static void main(String[] args) {
        Color[][] colors = MapDriver.readMap();
        GameMap gameMap = new GameMap(colors);
        ConsolePrinter.printMap(gameMap);
        Score score = new Score();
        score.init(gameMap, Color.BLACK);
        long time = System.currentTimeMillis();
        Config config = new Config();
        config.comboDeep = 7;
        ComboProcessor comboProcessor = new ComboProcessor();
        comboProcessor.init(gameMap, score, new Counter(), config, new Cache(config, gameMap));
        System.out.println(comboProcessor.canKill(Color.BLACK));
        System.out.println(System.currentTimeMillis() - time + "ms");
    }
}
