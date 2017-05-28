package core.evalution;

import core.Config;
import core.GameMap;
import entity.Point;
import enumeration.Color;

public class ScoreFive implements ScoreBase {

    @Override
    public int getScore(GameMap gameMap, Point point, Color color) {
        int value = 0;
        int length = 5;
        for (int i = 0; i < 4; i++) {
            if (gameMap.checkColors(color, point, i, 0, length - 1)) {
                value += Config.scoreOpen[length];
            } else if (gameMap.checkColors(color.getOtherColor(), point, i, 0, length - 1)) {
                value -= Config.scoreOpen[length];
            }
        }
        return value;
    }
}
