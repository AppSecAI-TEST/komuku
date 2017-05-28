package core;

import entity.Point;
import enumeration.Color;

public class GameMap {

    private int directX[] = {0, 1, 1, 1, 0, -1, -1, -1};
    private int directY[] = {1, 1, 0, -1, -1, 1, 0, -1};

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

    public Color getColor(Point point) {
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
}
