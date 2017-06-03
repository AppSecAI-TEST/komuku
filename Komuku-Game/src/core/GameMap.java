package core;

import entity.Point;
import enumeration.Color;

import java.util.ArrayList;
import java.util.List;

public class GameMap {

    private int directX[] = {0, 1, 1, 1, 0, -1, -1, -1};
    private int directY[] = {1, 1, 0, -1, -1, -1, 0, 1};

    private Color[][] map;

    GameMap(Color[][] map) {
        this.map = map;
    }

    public boolean reachable(Point point) {
        if (point.getX() < 0 || point.getX() >= map.length)
            return false;
        if (point.getY() < 0 || point.getY() >= map.length)
            return false;
        return true;
    }

    public Color[][] getMap() {
        return map;
    }

    public Color getColor(Point point) {
        if (!reachable(point)) {
            return null;
        }
        return map[point.getX()][point.getY()];
    }

    Color getColor(int x, int y) {
        return map[x][y];
    }

    void setColor(Point point, Color color) {
        map[point.getX()][point.getY()] = color;
    }

    public Point getRelatePoint(Point point, int direct, int distance) {
        return new Point(point.getX() + directX[direct] * distance, point.getY() + directY[direct] * distance);
    }

    public boolean checkColors(Color color, Point point, int direct, int start, int end) {
        for (int i = start; i <= end; i++) {
            Point fresh = getRelatePoint(point, direct, i);
            if (!reachable(fresh)) {
                return false;
            }
            if (getColor(fresh) != color) {
                return false;
            }
        }
        return true;
    }

    int getMaxSequenceWithoutCurrent(Color color, Point point, int direct) {
        int value = 0;
        for (int i = 1; i < 5; i++) {
            Point fresh = getRelatePoint(point, direct, i);
            if (!reachable(fresh)) {
                return value;
            }
            if (getColor(fresh) == color) {
                value += 1;
            } else {
                return value;
            }
        }
        return value;
    }

    int getMaxSequence(Color color, Point point, int direct) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            Point fresh = getRelatePoint(point, direct, i);
            if (!reachable(fresh)) {
                return value;
            }
            if (getColor(fresh) == color) {
                value += 1;
            } else {
                return value;
            }
        }
        return value;
    }

    int getOtherDirect(int direct) {
        return (direct + 4) % 8;
    }

    List<Point> getNeighbor(int range) {
        List<Point> result = new ArrayList<>();
        boolean[][] signal = new boolean[Config.size][Config.size];
        for (int i = 0; i < Config.size; i++)
            for (int j = 0; j < Config.size; j++) {
                if (getColor(i, j) != Color.NULL) {
                    findNeighbor(new Point(i, j), range, signal);
                }
            }

        for (int i = 0; i < Config.size; i++)
            for (int j = 0; j < Config.size; j++) {
                if (getColor(i, j) == Color.NULL && signal[i][j]) {
                    Point point = new Point(i, j);
                    result.add(point);
                }
            }
        return result;
    }

    private void findNeighbor(Point point, int step, boolean[][] signal) {
        if (step == 0) {
            return;
        }
        if (signal[point.getX()][point.getY()]) {
            return;
        }
        signal[point.getX()][point.getY()] = true;
        for (int i = 0; i < 8; i++) {
            Point fresh = getRelatePoint(point, i, 1);
            if (reachable(fresh))
                if (getColor(fresh) == Color.NULL) {
                    findNeighbor(fresh, step - 1, signal);
                }
        }
    }
}
