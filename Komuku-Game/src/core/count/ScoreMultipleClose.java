package core.count;

import core.Config;
import core.GameMap;
import entity.Point;
import enumeration.Color;

public class ScoreMultipleClose implements ScoreBase {

    @Override
    public int getScore(GameMap gameMap, Point point, Color color) {
        int value = 0;
        Color pointColor = gameMap.getColor(point);
        if (color == Color.NULL) {
            return value;
        }
        for (int i = 0; i < 8; i++) {
            for (int k = 1; k <= 2; k++) {
                Point tail = gameMap.getRelatePoint(point, i, k + 1);
                if (!gameMap.reachable(tail)) {
                    break;
                }
                if (gameMap.getColor(tail) == Color.NULL) {
                    if (gameMap.checkColors(pointColor.getOtherColor(), point, i, 1, k)) {
                        if (pointColor == color)
                            value += Config.scoreClose[k];
                        else
                            value -= Config.scoreClose[k];
                    }
                    break;
                }
            }
        }
        return value;
    }
}
