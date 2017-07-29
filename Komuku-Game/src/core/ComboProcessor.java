package core;

import entity.Point;
import enumeration.Color;
import helper.MapDriver;

import java.util.ArrayList;
import java.util.List;

public class ComboProcessor {

    private static boolean debug = false;

    //是否考虑3的情况
    private static boolean careThree = true;

    static boolean canKill(GameMap gameMap, Color targetColor, int deep, Score score) {
        boolean result = dfsKill(gameMap, targetColor, targetColor, deep, score);
        if (debug) {
            if (result) {
                MapDriver.printToConsole(gameMap);
            }
        }
        return result;
    }

    private static boolean dfsKill(GameMap gameMap, Color color, Color targetColor, int level, Score score) {
        if (level == 0) {
            return false;
        }
        Analyzer data = new Analyzer(gameMap, color, targetColor, gameMap.getNeighbor(color), score);
        if (color == targetColor) {
            if (data.getFiveAttack().size() > 0) {
                return true;
            }
            List<Point> points = getComboAttackPoints(gameMap, color, data);
            for (Point point : points) {
                setColor(point, color, Color.NULL, targetColor, score, gameMap);
                boolean value = dfsKill(gameMap, color.getOtherColor(), targetColor, level - 1, score);
                if (value) {
                    setColor(point, Color.NULL, color, targetColor, score, gameMap);
                    return true;
                }
                setColor(point, Color.NULL, color, targetColor, score, gameMap);
            }
            return false;
        } else {
            if (data.getFiveAttack().size() > 0) {
                return false;
            }
            List<Point> points = getComboDefencePoints(gameMap, color, data);
            //如果没有能防的则结束
            if (points.size() == 0) {
                return false;
            }
            for (Point point : points) {
                setColor(point, color, Color.NULL, targetColor, score, gameMap);
                boolean value = dfsKill(gameMap, color.getOtherColor(), targetColor, level - 1, score);
                if (!value) {
                    setColor(point, Color.NULL, color, targetColor, score, gameMap);
                    return false;
                }
                setColor(point, Color.NULL, color, targetColor, score, gameMap);
            }
            return true;
        }
    }

    private static List<Point> getComboAttackPoints(GameMap gameMap, Color color, Analyzer data) {
        //如果有对方冲4，则防冲4
        if (!data.getFourDefence().isEmpty()) {
            return new ArrayList<>(data.getFourDefence());
        }
        //如果有对方活3，冲四
        if (careThree) {
            if (!data.getThreeDefence().isEmpty()) {
                return new ArrayList<>(data.getFourAttack());
            }
        }
        List<Point> result = new ArrayList<>();
        result.addAll(data.getFourAttack());
        if (careThree) {
            result.addAll(data.getThreeOpenAttack());
        }
        return result;
    }

    private static List<Point> getComboDefencePoints(GameMap gameMap, Color color, Analyzer data) {
        //如果有对方冲4，则防冲4
        if (!data.getFourDefence().isEmpty()) {
            return new ArrayList<>(data.getFourDefence());
        }
        if (careThree) {
            //如果有对方活3，则防活3或者冲四
            if (!data.getThreeDefence().isEmpty()) {
                return new ArrayList<Point>(data.getFourAttack()) {{
                    addAll(data.getThreeDefence());
                }};
            }
        }
        return new ArrayList<>();
    }


    private static void setColor(Point point, Color color, Color forwardColor, Color aiColor, Score score, GameMap gameMap) {
        score.setColor(point, color, forwardColor, aiColor);
        gameMap.setColor(point, color);
    }

    public static void main(String[] args) {
        Color[][] colors = MapDriver.readMap();
        GameMap gameMap = new GameMap(colors);
        MapDriver.printToConsole(gameMap);
        Score score = new Score();
        score.init(gameMap, Color.BLACK);
        long time = System.currentTimeMillis();
        System.out.println(canKill(gameMap, Color.BLACK, 9, score));
        System.out.println(System.currentTimeMillis() - time + "ms");
    }
}
