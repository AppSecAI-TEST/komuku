package core;

import entity.AnalyzedData;
import entity.Point;
import enumeration.Color;
import helper.MapDriver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public AnalyzedData getAnalyzedPoints(Color color) {
        AnalyzedData data = new AnalyzedData();

        data.setOrigin(new HashSet<>(getNeighbor(color)));

        data.getOrigin().forEach(point -> {
            for (int i = 0; i < 4; i++) {
                int headCurrent = getMaxSequenceWithoutCurrent(color, point, i);
                int tailCurrent = getMaxSequenceWithoutCurrent(color, point, getOtherDirect(i));
                boolean headLive = getColor(getRelatePoint(point, i, headCurrent + 1)) == Color.NULL;
                boolean tailLive = getColor(getRelatePoint(point, getOtherDirect(i), tailCurrent + 1)) == Color.NULL;

                //连5
                if (headCurrent + tailCurrent >= 4) {
                    data.getFiveAttack().add(point);
                }
                //连4
                if (headCurrent + tailCurrent == 3) {
                    if (headLive || tailLive) {
                        data.getFourAttack().add(point);
                    }
                }
                //断连4
                for (int k = -4; k <= 0; k++) {
                    if (!reachable(getRelatePoint(point, i, k)) ||
                            !reachable(getRelatePoint(point, i, k + 4))) {
                        continue;
                    }
                    int count = 0;
                    for (int t = k; t <= k + 4; t++) {
                        Point p = getRelatePoint(point, i, t);
                        if (getColor(p) == color) {
                            count++;
                        }
                        if (getColor(p) == color.getOtherColor()) {
                            count = Integer.MIN_VALUE;
                            break;
                        }
                    }
                    if (count == 3) {
                        data.getFourAttack().add(point);
                    }
                }

                //连3
                if (headCurrent + tailCurrent == 2) {
                    if (headLive && tailLive) {
                        data.getThreeOpenAttack().add(point);
                    }
                }
                //断连3
                for (int k = -4; k <= -1; k++) {
                    if (!reachable(getRelatePoint(point, i, k)) ||
                            !reachable(getRelatePoint(point, i, k + 5))) {
                        continue;
                    }
                    if (getColor(getRelatePoint(point, i, k)) != Color.NULL ||
                            getColor(getRelatePoint(point, i, k + 5)) != Color.NULL) {
                        continue;
                    }
                    int count = 0;
                    for (int t = k + 1; t <= k + 4; t++) {
                        Point p = getRelatePoint(point, i, t);
                        if (getColor(p) == color) {
                            count++;
                        }
                        if (getColor(p) == color.getOtherColor()) {
                            count = Integer.MIN_VALUE;
                            break;
                        }
                    }
                    if (count == 2) {
                        data.getThreeOpenAttack().add(point);
                    }
                }
            }
            for (int i = 0; i < 4; i++) {
                int headOther = getMaxSequenceWithoutCurrent(color.getOtherColor(), point, i);
                int tailOther = getMaxSequenceWithoutCurrent(color.getOtherColor(), point, getOtherDirect(i));
                boolean headOtherLive = getColor(getRelatePoint(point, i, headOther + 1)) == Color.NULL;
                boolean tailOtherLive = getColor(getRelatePoint(point, getOtherDirect(i), tailOther + 1)) == Color.NULL;

                //防4连和断4连
                if (headOther + tailOther >= 4) {
                    data.getFourDefence().add(point);
                }

                //防断3连
                if (headOther > 0 && tailOther > 0 && headOther + tailOther == 3) {
                    if (headOtherLive && tailOtherLive) {
                        data.getThreeDefence().add(point);
                        data.getThreeDefence().add(getRelatePoint(point, i, headOther + 1));
                        data.getThreeDefence().add(getRelatePoint(point, i, -(tailOther + 1)));
                    }
                }
                //防3连
                if (headOther == 3 && tailOther == 0 && headOtherLive) {
                    data.getThreeDefence().add(point);
                }
                if (tailOther == 3 && headOther == 0 && tailOtherLive) {
                    data.getThreeDefence().add(point);
                }
            }
        });

        Set<Point> otherPoints = data.getOrigin();
        otherPoints.removeAll(data.getFiveAttack());
        otherPoints.removeAll(data.getFourAttack());
        otherPoints.removeAll(data.getFourDefence());
        otherPoints.removeAll(data.getThreeDefence());
        otherPoints.removeAll(data.getThreeOpenAttack());
        data.setNotKey(otherPoints);
        return data;
    }

}
