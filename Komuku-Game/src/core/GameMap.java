package core;

import entity.Point;
import enumeration.Color;
import helper.MapDriver;

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

    List<Point> getNeighbor(Color color) {
        int range = 2;
        List<Point> result = new ArrayList<>();
        int[][] signal = new int[Config.size][Config.size];
        int[][] signalCurrentColorTwo = new int[Config.size][Config.size];
        int[][] signalOtherColorTwo = new int[Config.size][Config.size];
        for (int i = 0; i < Config.size; i++)
            for (int j = 0; j < Config.size; j++) {
                Color pointColor = getColor(i, j);
                if (pointColor != Color.NULL) {
                    int left = i - range >= 0 ? i - range : 0;
                    int right = i + range < Config.size - 1 ? i + range : Config.size - 1;
                    int top = j - range >= 0 ? j - range : 0;
                    int button = j + range < Config.size - 1 ? j + range : Config.size - 1;
                    for (int x = left; x <= right; x++)
                        for (int y = top; y <= button; y++) {
                            if (getColor(x, y) == Color.NULL) {
                                if (Math.max(Math.abs(x - i), Math.abs(y - j)) == 1) {
                                    signal[x][y]++;
                                }
                                if (Math.max(Math.abs(x - i), Math.abs(y - j)) == 2) {
                                    if (pointColor == color) {
                                        signalCurrentColorTwo[x][y]++;
                                    }
                                    if (pointColor == color.getOtherColor()) {
                                        signalOtherColorTwo[x][y]++;
                                    }
                                }
                            }
                        }
                }
            }
        for (int i = 0; i < Config.size; i++)
            for (int j = 0; j < Config.size; j++) {
                if (signal[i][j] > 0) {
                    result.add(new Point(i, j));
                    continue;
                }
                if (signalCurrentColorTwo[i][j] > 0) {
                    result.add(new Point(i, j));
                    continue;
                }
                if (signalOtherColorTwo[i][j] > 1) {
                    result.add(new Point(i, j));
                }
            }
        return result;
    }
}
