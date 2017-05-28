package core.evalution;

import core.GameMap;
import entity.Point;
import enumeration.Color;

public class ScoreOne implements ScoreBase {

    @Override
    public int getScore(GameMap gameMap, Point point, Color color) {
        int value = 0;
        if (gameMap.getColor(point) == Color.NULL) {
            return value;
        }
        for (int i = 0; i < 8; i++) {
            Point fresh = gameMap.getRelatePoint(point, i, 1);
            if (gameMap.reachable(fresh)) {
                if (gameMap.getColor(fresh) == color)
                    value += 2;
                if (gameMap.getColor(fresh) == color.getOtherColor())
                    value += 2;
                if (gameMap.getColor(fresh) == Color.NULL)
                    value += 1;
            }
        }
        if (gameMap.getColor(point) != color) {
            value = -value;
        }
        return value;
    }

}
