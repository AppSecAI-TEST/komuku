package core;

import core.count.ScoreFive;
import core.count.ScoreMultiple;
import core.count.ScoreMultipleClose;
import core.count.ScoreOne;
import entity.Point;
import enumeration.Color;

public class Score {

    public static int getMapScore(GameMap gameMap, Color color) {
        int value = 0;
        ScoreOne scoreOne = new ScoreOne();
        ScoreFive scoreFive = new ScoreFive();
        ScoreMultiple scoreMultiple = new ScoreMultiple();
        ScoreMultipleClose scoreMultipleClose = new ScoreMultipleClose();

        for (int i = 0; i < Config.size; i++)
            for (int j = 0; j < Config.size; j++) {
                Point point = new Point(i, j);
                value += scoreOne.getScore(gameMap, point, color);
                value += scoreMultiple.getScore(gameMap, point, color);
                value += scoreFive.getScore(gameMap, point, color);
                value += scoreMultipleClose.getScore(gameMap, point, color);
            }
        return value;
    }
}
