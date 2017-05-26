package core.count;

import core.GameMap;
import entity.Point;
import enumeration.Color;

public interface ScoreBase {

    int getScore(GameMap gameMap, Point point, Color color);

}
