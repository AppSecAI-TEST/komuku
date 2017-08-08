package core;

import entity.Point;
import enumeration.Color;
import helper.MapDriver;

import java.util.*;

public class GameMap {

    private int directX[] = {0, 1, 1, 1, 0, -1, -1, -1};
    private int directY[] = {1, 1, 0, -1, -1, -1, 0, 1};

    private Color[][] map;

    private Set<Point> neighbor = new HashSet<>();
    private Map<Point, List<Point>> historyAdd = new HashMap<>();

    private long hashCode = 0;
    private static long[][] weightBlack = new long[Config.size][Config.size];
    private static long[][] weightWhite = new long[Config.size][Config.size];

    static {
        Random random = new Random();
        for (int i = 0; i < Config.size; i++)
            for (int j = 0; j < Config.size; j++) {
                weightBlack[i][j] = random.nextLong();
                weightWhite[i][j] = random.nextLong();
            }
    }

    public GameMap(Color[][] map) {
        this.map = map;
        for (int i = 0; i < Config.size; i++)
            for (int j = 0; j < Config.size; j++) {
                Color color = getColor(i, j);
                if (color != Color.NULL) {
                    updateNeighbor(new Point(i, j), color);
                    updateHashCode(i, j, color, Color.NULL);
                }
            }
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
        updateNeighbor(point, color);
    }

    void updateNeighbor(Point point, Color pointColor) {
        if (pointColor != Color.NULL) {
            List<Point> points = new ArrayList<>();
            neighbor.remove(point);
            for (int i = 0; i < 8; i++) {
                int x = point.getX() + directX[i];
                int y = point.getY() + directY[i];
                if (reachable(x, y)) {
                    Color color = getColor(x, y);
                    if (getColor(x, y) == Color.NULL) {
                        Point newPoint = new Point(x, y);
                        if (!neighbor.contains(newPoint)) {
                            neighbor.add(newPoint);
                            points.add(newPoint);
                        }
                    } else {
                        if (color == pointColor) {
                            int x1 = point.getX() + directX[i] * 3;
                            int y1 = point.getY() + directY[i] * 3;
                            int x2 = point.getX() - directX[i] * 2;
                            int y2 = point.getY() - directY[i] * 2;
                            Point point1 = new Point(x1, y1);
                            Point point2 = new Point(x2, y2);
                            if (reachable(x1, y1)) {
                                if (getColor(x1, y1) == Color.NULL) {
                                    if (!neighbor.contains(point1)) {
                                        neighbor.add(point1);
                                        points.add(point1);
                                    }
                                }
                            }
                            if (reachable(x2, y2)) {
                                if (getColor(x2, y2) == Color.NULL) {
                                    if (!neighbor.contains(point2)) {
                                        neighbor.add(point2);
                                        points.add(point2);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            historyAdd.put(point, points);
        } else {
            List<Point> points = historyAdd.get(point);
            neighbor.removeAll(points);
            neighbor.add(point);
            historyAdd.remove(point);
        }
    }

    public boolean checkColors(Color color, Point point, int direct, int start, int end) {
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

    List<Point> getNeighbor() {
        return new ArrayList<>(neighbor);
    }

    public long getHashCode() {
        return hashCode;
    }

    private void updateHashCode(int x, int y, Color color, Color forwardColor) {
        if (forwardColor != Color.NULL) {
            if (forwardColor == Color.BLACK) {
                hashCode ^= weightBlack[x][y];
            }
            if (forwardColor == Color.WHITE) {
                hashCode ^= weightWhite[x][y];
            }
        }
        if (color != Color.NULL) {
            if (color == Color.BLACK) {
                hashCode ^= weightBlack[x][y];
            }
            if (color == Color.WHITE) {
                hashCode ^= weightWhite[x][y];
            }
        }
    }

    public static void main(String[] args) {
        Color[][] map = MapDriver.readMap();
        GameMap gameMap = new GameMap(map);
        List<Point> points = gameMap.getNeighbor();
        gameMap.setColor(points.get(0), Color.WHITE);
        gameMap.setColor(points.get(0), Color.NULL);
        gameMap.getNeighbor();
    }

}
