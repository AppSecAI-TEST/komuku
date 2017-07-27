package core;

import entity.AnalyzedData;
import entity.Point;
import enumeration.Color;
import helper.MapDriver;

import java.util.ArrayList;
import java.util.List;

public class ComboProcessor {

    private static boolean debug = false;

    //是否考虑3的情况
    private static boolean careThree = true;

    static boolean canKill(GameMap gameMap, Color targetColor, int deep) {
        boolean result = dfsKill(gameMap, targetColor, targetColor, deep);
        if (debug) {
            if (result) {
                MapDriver.printToConsole(gameMap);
            }
        }
        return result;
    }

    private static boolean dfsKill(GameMap gameMap, Color color, Color targetColor, int level) {
        if (level == 0) {
            return false;
        }
        AnalyzedData data = gameMap.getAnalyzedPoints(color);
        if (color == targetColor) {
            if (data.getFiveAttack().size() > 0) {
                return true;
            }
            List<Point> points = getComboAttackPoints(gameMap, color, data);
            for (Point point : points) {
                gameMap.setColor(point, color);
                boolean value = dfsKill(gameMap, color.getOtherColor(), targetColor, level - 1);
                if (value) {
                    gameMap.setColor(point, Color.NULL);
                    return true;
                }
                gameMap.setColor(point, Color.NULL);
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
                gameMap.setColor(point, color);
                boolean value = dfsKill(gameMap, color.getOtherColor(), targetColor, level - 1);
                if (!value) {
                    gameMap.setColor(point, Color.NULL);
                    return false;
                }
                gameMap.setColor(point, Color.NULL);
            }
            return true;
        }
    }

    private static List<Point> getComboAttackPoints(GameMap gameMap, Color color, AnalyzedData data) {
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

    private static List<Point> getComboDefencePoints(GameMap gameMap, Color color, AnalyzedData data) {
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

    public static void main(String[] args) {
        Color[][] colors = MapDriver.readMap();
        GameMap gameMap = new GameMap(colors);
        System.out.println(canKill(gameMap, Color.BLACK, 3));
    }
}
