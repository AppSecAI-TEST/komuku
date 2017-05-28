package core;

import entity.Point;
import enumeration.Color;

import java.util.ArrayList;
import java.util.List;

class LevelProcessor {

    private static boolean debug = false;

    private static int[] sequenceWeight = {0, 1, 5, 10, 100, 1000};

    static Color win(GameMap gameMap) {
        for (int i = 0; i < Config.size; i++)
            for (int j = 0; j < Config.size; j++) {
                Point point = new Point(i, j);
                if (gameMap.getColor(point) != Color.NULL) {
                    for (int direct = 0; direct < 4; direct++) {
                        if (gameMap.checkColors(gameMap.getColor(point), point, direct, 0, 4)) {
                            return gameMap.getColor(point);
                        }
                    }
                }
            }
        return null;
    }

    static List<Point> getExpandPoints(GameMap gameMap) {
        List<Point> result = new ArrayList<>();
        List<Integer> score = new ArrayList<>();

        boolean[][] signal = new boolean[Config.size][Config.size];
        int range = 3;

        for (int i = 0; i < Config.size; i++)
            for (int j = 0; j < Config.size; j++) {
                if (gameMap.getColor(i, j) != Color.NULL) {
                    findRange(gameMap, new Point(i, j), range, signal);
                }
            }

        for (int i = 0; i < Config.size; i++)
            for (int j = 0; j < Config.size; j++) {
                if (gameMap.getColor(i, j) == Color.NULL && signal[i][j]) {
                    Point point = new Point(i, j);
                    result.add(point);
                    score.add(getScore(gameMap, point));
                }
            }

        sort(0, score.size() - 1, result, score);

        if (debug) {
            printResult(result);
        }
        return result;
    }

    private static void findRange(GameMap gameMap, Point point, int step, boolean[][] signal) {
        if (step == 0) {
            return;
        }
        if (signal[point.getX()][point.getY()]) {
            return;
        }
        signal[point.getX()][point.getY()] = true;
        for (int i = 0; i < 8; i++) {
            Point fresh = gameMap.getRelatePoint(point, i, 1);
            if (gameMap.reachable(fresh))
                if (gameMap.getColor(fresh) == Color.NULL) {
                    findRange(gameMap, fresh, step - 1, signal);
                }
        }
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
}
