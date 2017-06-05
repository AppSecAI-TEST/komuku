package core;

import entity.Point;
import enumeration.Color;
import helper.MapDriver;

import java.util.ArrayList;
import java.util.List;

class LevelProcessor {

    private static boolean debug = false;

    private static int[] sequenceWeight = {0, 1, 10, 100, 1000, 10000};

    static Color win(GameMap gameMap) {
        for (int i = 0; i < Config.size; i++)
            for (int j = 0; j < Config.size; j++) {
                Point point = new Point(i, j);
                if (gameMap.getColor(point) != Color.NULL) {
                    for (int direct = 0; direct < 4; direct++) {
                        Color color = gameMap.getColor(point);
                        if (gameMap.checkColors(color, point, direct, 0, 4)) {
                            return gameMap.getColor(point);
                        }
                    }
                }
            }
        return null;
    }

    static List<Point> getComboPoints(GameMap gameMap, Color color) {
        List<Point> points;

        int range = 3;
        points = gameMap.getNeighbor(range);

        List<Point> result = new ArrayList<>();
        List<Point> highPoints = new ArrayList<>();
        List<Point> midPoints = new ArrayList<>();
        List<Point> lowPoints = new ArrayList<>();
        points.forEach(point -> {
            for (int i = 0; i < 4; i++) {
                int headCurrent = gameMap.getMaxSequenceWithoutCurrent(color, point, i);
                int tailCurrent = gameMap.getMaxSequenceWithoutCurrent(color, point, gameMap.getOtherDirect(i));
                boolean headLive = gameMap.getColor(gameMap.getRelatePoint(point, i, headCurrent + 1)) == Color.NULL;
                boolean tailLive = gameMap.getColor(gameMap.getRelatePoint(point, gameMap.getOtherDirect(i), tailCurrent + 1)) == Color.NULL;

                //连5
                if (headCurrent + tailCurrent >= 4) {
                    highPoints.add(point);
                    return;
                }
                //连4
                if (headCurrent + tailCurrent == 3) {
                    if (headLive || tailLive) {
                        highPoints.add(point);
                        return;
                    }
                }
                //断连4
                for (int k = -4; k <= 0; k++) {
                    if (!gameMap.reachable(gameMap.getRelatePoint(point, i, k)) ||
                            !gameMap.reachable(gameMap.getRelatePoint(point, i, k + 4))) {
                        continue;
                    }
                    int count = 0;
                    for (int t = k; t <= k + 4; t++) {
                        Point p = gameMap.getRelatePoint(point, i, t);
                        if (gameMap.getColor(p) == color) {
                            count++;
                        }
                        if (gameMap.getColor(p) == color.getOtherColor()) {
                            count = Integer.MIN_VALUE;
                            break;
                        }
                    }
                    if (count == 3) {
                        highPoints.add(point);
                        return;
                    }
                }

                //连3
                if (headCurrent + tailCurrent == 2) {
                    if (headLive && tailLive) {
                        midPoints.add(point);
                        return;
                    }
                }
                //断连3
                for (int k = -4; k <= -1; k++) {
                    if (!gameMap.reachable(gameMap.getRelatePoint(point, i, k)) ||
                            !gameMap.reachable(gameMap.getRelatePoint(point, i, k + 5))) {
                        continue;
                    }
                    if (gameMap.getColor(gameMap.getRelatePoint(point, i, k)) != Color.NULL ||
                            gameMap.getColor(gameMap.getRelatePoint(point, i, k + 5)) != Color.NULL) {
                        continue;
                    }
                    int count = 0;
                    for (int t = k + 1; t <= k + 4; t++) {
                        Point p = gameMap.getRelatePoint(point, i, t);
                        if (gameMap.getColor(p) == color) {
                            count++;
                        }
                        if (gameMap.getColor(p) == color.getOtherColor()) {
                            count = Integer.MIN_VALUE;
                            break;
                        }
                    }
                    if (count == 2) {
                        midPoints.add(point);
                        return;
                    }
                }
            }
            for (int i = 0; i < 4; i++) {
                int headOther = gameMap.getMaxSequenceWithoutCurrent(color.getOtherColor(), point, i);
                int tailOther = gameMap.getMaxSequenceWithoutCurrent(color.getOtherColor(), point, gameMap.getOtherDirect(i));
                boolean headOtherLive = gameMap.getColor(gameMap.getRelatePoint(point, i, headOther + 1)) == Color.NULL;
                boolean tailOtherLive = gameMap.getColor(gameMap.getRelatePoint(point, gameMap.getOtherDirect(i), tailOther + 1)) == Color.NULL;

                //防4连和断4连
                if (headOther + tailOther >= 4) {
                    highPoints.add(point);
                    return;
                }

                //防断3连
                if (headOther > 0 && tailOther > 0 && headOther + tailOther == 3) {
                    if (headOtherLive && tailOtherLive) {
                        lowPoints.add(point);
                        return;
                    }
                }
                //防3连
                if (headOther == 3 && tailOther == 0 && headOtherLive) {
                    lowPoints.add(point);
                    return;
                }
                if (tailOther == 3 && headOther == 0 && tailOtherLive) {
                    lowPoints.add(point);
                    return;
                }
            }
        });
        result.addAll(highPoints);
        result.addAll(midPoints);
        result.addAll(lowPoints);
        return result;
    }

    static List<Point> getExpandPoints(GameMap gameMap) {
        List<Point> result;
        List<Integer> score = new ArrayList<>();

        int range = 3;
        result = gameMap.getNeighbor(range);
        result.forEach(point -> score.add(getScore(gameMap, point)));

        if (result.isEmpty()) {
            result.add(new Point(7, 7));
            return result;
        }
        sort(0, score.size() - 1, result, score);

        if (debug) {
            printResult(result);
        }
        return result;
    }

    private static int getScore(GameMap gameMap, Point point) {
        int value = 0;
        for (int i = 0; i < 8; i++) {
            Point fresh = gameMap.getRelatePoint(point, i, 1);
            if (gameMap.reachable(fresh)) {
                Color color = gameMap.getColor(fresh);
                if (color != Color.NULL) {
                    int length = gameMap.getMaxSequence(color, fresh, i);
                    if (length > 5) {
                        length = 5;
                    }
                    value += sequenceWeight[length];
                }
            }
        }
        return value;
    }

    private static void sort(int left, int right, List<Point> points, List<Integer> scores) {
        int x = left;
        int y = right;
        int mid = scores.get((x + y) / 2);
        while (x < y) {
            while (scores.get(x) > mid) x++;
            while (scores.get(y) < mid) y--;
            if (x <= y) {
                int temp = scores.get(x);
                scores.set(x, scores.get(y));
                scores.set(y, temp);
                Point point = points.get(x);
                points.set(x, points.get(y));
                points.set(y, point);
                x++;
                y--;
            }
        }
        if (x < right) sort(x, right, points, scores);
        if (left < y) sort(left, y, points, scores);
    }

    private static void printResult(List<Point> result) {
        result.forEach(point -> {
            System.out.println(point.getX() + " " + point.getY());
        });
        System.out.printf(String.valueOf(result.size()));
    }

    public static void main(String[] args) {
        Color[][] map = MapDriver.readMap();
        GameMap gameMap = new GameMap(map);

        List<Point> points = getComboPoints(gameMap, Color.WHITE);
        points.forEach(point -> System.out.println(point.getX() + " " + point.getY()));
    }
}
