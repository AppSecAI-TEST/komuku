package core;

import entity.Point;
import enumeration.Color;
import helper.MapDriver;

class Score {

    private static int ONE = 1;
    private static int TWO = 10;
    private static int THREE = 20;
    private static int FOUR = 50;

    private static int directX[] = {0, 1, 1, 1};
    private static int directY[] = {1, 1, 0, -1};

    //大于2个的情形此处使用动态规划
    //定义：    f[i][j][k] 表示以i,j为终点，在方向k上的统计
    //转移方程: f[i][j][k] = a[i][j][k] + f[i-dx[k]][j-dy[k]][k]
    private int[][][] blackCount = new int[Config.size][Config.size][4];
    private int[][][] whiteCount = new int[Config.size][Config.size][4];

    private int value = 0;

    void init(GameMap gameMap, Color aiColor) {
        for (int i = 0; i < Config.size; i++) {
            for (int j = 0; j < Config.size; j++) {
                Point point = new Point(i, j);
                Color color = gameMap.getColor(point);
                if (color != Color.NULL) {
                    setColor(point, color, Color.NULL, aiColor);
                }
            }
        }
    }

    int getMapScore() {
        return value;
    }

    void setColor(Point point, Color color, Color forwardColor, Color aiColor) {
        for (int i = 0; i < 4; i++) {
            int x = point.getX();
            int y = point.getY();
            for (int k = 0; k < 5; k++) {
                if (forwardColor == Color.NULL) {
                    value -= getValueByCount(blackCount[x][y][i], whiteCount[x][y][i], aiColor);
                    if (color == Color.BLACK) {
                        blackCount[x][y][i]++;
                    }
                    if (color == Color.WHITE) {
                        whiteCount[x][y][i]++;
                    }
                    value += getValueByCount(blackCount[x][y][i], whiteCount[x][y][i], aiColor);
                }
                if (forwardColor != Color.NULL) {
                    value -= getValueByCount(blackCount[x][y][i], whiteCount[x][y][i], aiColor);
                    if (color == Color.NULL) {
                        if (forwardColor == Color.BLACK) {
                            blackCount[x][y][i]--;
                        }
                        if (forwardColor == Color.WHITE) {
                            whiteCount[x][y][i]--;
                        }
                    }
                    value += getValueByCount(blackCount[x][y][i], whiteCount[x][y][i], aiColor);
                }
                x += directX[i];
                y += directY[i];
                if (!GameMap.reachable(x, y)) {
                    break;
                }
            }
        }
    }

    public int getValueByCount(int blackCount, int whiteCount, Color color) {
        int value = 0;
        if (blackCount == 0) {
            if (whiteCount == 1)
                value -= ONE;
            if (whiteCount == 2)
                value -= TWO;
            if (whiteCount == 3)
                value -= THREE;
            if (whiteCount == 4)
                value -= FOUR;
        }

        if (whiteCount == 0) {
            if (blackCount == 1)
                value += ONE;
            if (blackCount == 2)
                value += TWO;
            if (blackCount == 3)
                value += THREE;
            if (blackCount == 4)
                value += FOUR;
        }
        if (color == Color.WHITE) {
            value = -value;
        }
        return value;
    }

    public int[][][] getBlackCount() {
        return blackCount;
    }

    public int[][][] getWhiteCount() {
        return whiteCount;
    }

    int[][][] getColorCount(Color color) {
        if (color == Color.BLACK) {
            return blackCount;
        }
        if (color == Color.WHITE) {
            return whiteCount;
        }
        return null;
    }
}
