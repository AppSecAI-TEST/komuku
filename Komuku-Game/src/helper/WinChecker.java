package helper;

import core.Config;
import core.GameMap;
import entity.Point;
import enumeration.Color;

public class WinChecker {

    public static Color win(Color[][] map) {
        GameMap gameMap = new GameMap(map);
        for (int i = 0; i < Config.size; i++)
            for (int j = 0; j < Config.size; j++) {
                Point point = new Point(i, j);
                if (gameMap.getColor(point) != Color.NULL) {
                    for (int direct = 0; direct < 4; direct++) {
                        Color color = gameMap.getColor(point);
                        if (gameMap.checkColors(color, point, direct, 0, 4)) {
                            return gameMap.getColor(point);
                        }
                    }
                }
            }
        return null;
    }

}
