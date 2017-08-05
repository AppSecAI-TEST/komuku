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

    static boolean reachable(Point point) {
        if (point.getX() < 0 || point.getX() >= Config.size)
            return false;
        if (point.getY() < 0 || point.getY() >= Config.size)
            return false;
        return true;
    }

    static boolean reachable(int x, int y) {
        if (x < 0 || x >= Config.size)
            return false;
        if (y < 0 || y >= Config.size)
            return false;
        return true;
    }

    public Color[][] getMap() {
        return map;
    }

    Color getColor(Point point) {
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

    boolean checkColors(Color color, Point point, int direct, int start, int end) {
        int x = point.getX() + start * (directX[direct]);
        int y = point.getY() + start * (directY[direct]);
        for (int i = start; i <= end; i++) {
            if (!reachable(x, y)) {
                return false;
            }
            if (getColor(x, y) != color) {
                return false;
            }
            x += directX[direct];
            y += directY[direct];
        }
        return true;
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
                                    if (Math.abs(x - i) != 1 && Math.abs(y - j) != 1) {
                                        //只计算2连隔空下
                                        int tx = i, ty = j;
                                        if (x - i > 0) {
                                            tx = i - 1;
                                        }
                                        if (x - i < 0) {
                                            tx = i + 1;
                                        }
                                        if (y - j > 0) {
                                            ty = j - 1;
                                        }
                                        if (y - j < 0) {
                                            ty = j + 1;
                                        }
                                        if (reachable(tx, ty)) {
                                            if (getColor(tx, ty) == pointColor) {
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
                if (signalOtherColorTwo[i][j] > 0) {
                    result.add(new Point(i, j));
                }
            }
        return result;
    }

    public static void main(String[] args) {
        Color[][] map = MapDriver.readMap();
        GameMap gameMap = new GameMap(map);
        System.out.println(gameMap.getNeighbor(Color.BLACK));
    }
}
