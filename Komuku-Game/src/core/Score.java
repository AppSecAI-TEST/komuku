package core;

import core.evalution.ScoreFive;
import core.evalution.ScoreMultiple;
import core.evalution.ScoreMultipleClose;
import core.evalution.ScoreOne;
import entity.Point;
import enumeration.Color;

class Score {

    private static ScoreOne scoreOne = new ScoreOne();
    private static ScoreFive scoreFive = new ScoreFive();
    private static ScoreMultiple scoreMultiple = new ScoreMultiple();
    private static ScoreMultipleClose scoreMultipleClose = new ScoreMultipleClose();

    static int getMapScore(GameMap gameMap, Color color) {
        int value = 0;

        for (int i = 0; i < Config.size; i++)
            for (int j = 0; j < Config.size; j++) {
                Point point = new Point(i, j);
                int fiveScore = scoreFive.getScore(gameMap, point, color);
                if (fiveScore > 0) {
                    return Integer.MAX_VALUE;
                }
                if (fiveScore < 0) {
                    return Integer.MIN_VALUE;
                }
                value += scoreOne.getScore(gameMap, point, color);
                value += scoreMultiple.getScore(gameMap, point, color);
                value += scoreFive.getScore(gameMap, point, color);
                value += scoreMultipleClose.getScore(gameMap, point, color);
            }
        return value;
    }
}
