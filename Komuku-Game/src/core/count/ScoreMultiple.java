package core.count;

import core.Config;
import core.GameMap;
import entity.Point;
import enumeration.Color;

public class ScoreMultiple implements ScoreBase {

    @Override
    public int getScore(GameMap gameMap, Point point, Color color) {
        int value = 0;
        if (gameMap.getColor(point) != Color.NULL) {
            return value;
        }
        for (int i = 0; i < 4; i++) {
            for (int k = 2; k <= 4; k++) {
                Point tail = gameMap.getRelatePoint(point, i, k + 1);
                if (!gameMap.reachable(tail)) {
                    break;
                }
                if (gameMap.getColor(tail) == Color.NULL) {
                    if (gameMap.checkColors(color, point, i, 1, k)) {
                        value += Config.scoreOpen[k];
                    } else if (gameMap.checkColors(color.getOtherColor(), point, i, 1, k)) {
                        value -= Config.scoreOpen[k];
                    }
                    break;
                }
            }
        }
        return value;
    }
}
