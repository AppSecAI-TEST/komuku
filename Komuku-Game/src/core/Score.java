package core;

import enumeration.Color;
import helper.MapDriver;

class Score {

    private static int ONE = 4;
    private static int TWO = 10;
    private static int THREE = 100;
    private static int FOUR = 200;

    private static int directX[] = {0, 1, 1, 1};
    private static int directY[] = {1, 1, 0, -1};

    static int getMapScore(GameMap gameMap, Color color) {
        int value = 0;
        value += getSingleValue(gameMap, color);
        value += getMultipleValue(gameMap, color);
        return value;
    }

    static private int getSingleValue(GameMap gameMap, Color color) {
        int value = 0;

        //计算单个的棋子的气
        for (int i = 0; i < Config.size; i++) {
            for (int j = 0; j < Config.size; j++) {
                Color currentColor = gameMap.getColor(i, j);
                if (currentColor == Color.NULL)
                    continue;

                int nullCount = 0;
                boolean signal = false;
                for (int k = 0; k < 4; k++) {
                    int px = i + directX[k];
                    int py = i + directY[k];

                    int bx = i - directX[k];
                    int by = i - directY[k];
                    if (gameMap.reachable(px, py) && gameMap.reachable(bx, by)) {
                        Color nearColor1 = gameMap.getColor(px, py);
                        Color nearColor2 = gameMap.getColor(px, py);
                        if (nearColor1 == currentColor || nearColor2 == currentColor) {
                            signal = true;
                            break;
                        }
                        if (nearColor1 == currentColor.getOtherColor() || nearColor2 == currentColor.getOtherColor()) {
                            continue;
                        }
                        nullCount++;
                    }
                }
                if (signal) {
                    continue;
                }

                if (currentColor == color) {
                    value += nullCount * ONE;
                } else {
                    value -= nullCount * ONE;
                }

            }
        }
        return value;
    }

    static private int getMultipleValue(GameMap gameMap, Color color) {
        int value = 0;
        //大于2个的情形此处使用动态规划
        //定义：    f[i][j][k] 表示以i,j为终点，在方向k上的统计
        //转移方程: f[i][j][k] = a[i][j][k] + f[i-dx[k]][j-dy[k]][k]
        int[][][] sameCount = new int[Config.size][Config.size][4];
        int[][][] otherCount = new int[Config.size][Config.size][4];

        //方向 0-2
        for (int k = 0; k < 3; k++) {
            for (int i = 0; i < Config.size; i++) {
                for (int j = 0; j < Config.size; j++) {
                    Color currentColor = gameMap.getColor(i, j);
                    if (currentColor == color) {
                        sameCount[i][j][k]++;
                    }
                    if (currentColor.getOtherColor() == color) {
                        otherCount[i][j][k]++;
                    }

                    int bx = i - directX[k];
                    int by = j - directY[k];
                    if (gameMap.reachable(bx, by)) {
                        sameCount[i][j][k] += sameCount[bx][by][k];
                        otherCount[i][j][k] += otherCount[bx][by][k];
                    }

                    if (gameMap.reachable(i - directX[k] * 5, j - directY[k] * 5)) {
                        Color forwardColor = gameMap.getColor(i - directX[k] * 5, j - directY[k] * 5);
                        if (forwardColor == color) {
                            sameCount[i][j][k]--;
                        }
                        if (forwardColor.getOtherColor() == color) {
                            otherCount[i][j][k]--;
                        }
                    }

                    value += getValueByCount(sameCount[i][j][k], otherCount[i][j][k]);
                }
            }
        }

        //方向3
        int k = 3;
        for (int i = 0; i < Config.size; i++) {
            for (int j = Config.size - 1; j >= 0; j--) {
                Color currentColor = gameMap.getColor(i, j);
                if (currentColor == color) {
                    sameCount[i][j][k]++;
                }
                if (currentColor.getOtherColor() == color) {
                    otherCount[i][j][k]++;
                }

                int bx = i - directX[k];
                int by = j - directY[k];
                if (gameMap.reachable(bx, by)) {
                    sameCount[i][j][k] += sameCount[bx][by][k];
                    otherCount[i][j][k] += otherCount[bx][by][k];
                }

                if (gameMap.reachable(i - directX[k] * 5, j - directY[k] * 5)) {
                    Color forwardColor = gameMap.getColor(i - directX[k] * 5, j - directY[k] * 5);
                    if (forwardColor == color) {
                        sameCount[i][j][k]--;
                    }
                    if (forwardColor.getOtherColor() == color) {
                        otherCount[i][j][k]--;
                    }
                }

                value += getValueByCount(sameCount[i][j][k], otherCount[i][j][k]);
            }
        }

        return value;
    }

    static private int getValueByCount(int sameCount, int otherCount) {
        int value = 0;
        if (sameCount == 0) {
            if (otherCount == 2)
                value -= TWO;
            if (otherCount == 3)
                value -= THREE;
            if (otherCount == 4)
                value -= FOUR;
        }

        if (otherCount == 0) {
            if (sameCount == 2)
                value += TWO;
            if (sameCount == 3)
                value += THREE;
            if (sameCount == 4)
                value += FOUR;
        }
        return value;
    }

    static public void main(String[] args) {
        Color[][] colors = MapDriver.readMap();
        GameMap gameMap = new GameMap(colors);
        System.out.println(getMapScore(gameMap, Color.BLACK));
    }
}
