package core;

import enumeration.Color;

import java.util.HashMap;
import java.util.Random;

class CacheMap {

    private HashMap<Long, Integer> scoreMap = new HashMap<>();

    private static long[][] weightBlack = new long[Config.size][Config.size];
    private static long[][] weightWhite = new long[Config.size][Config.size];
    private static long[][] weightNull = new long[Config.size][Config.size];

    static {
        Random random = new Random();
        for (int i = 0; i < Config.size; i++)
            for (int j = 0; j < Config.size; j++) {
                weightBlack[i][j] = random.nextLong();
                weightWhite[i][j] = random.nextLong();
                weightNull[i][j] = random.nextLong();
            }
    }

    void recordScore(Color[][] map, int score) {
        scoreMap.put(getHashCode(map), score);
    }

    Integer getCacheScore(Color[][] map) {
        return scoreMap.get(getHashCode(map));
    }

    void clear() {
        scoreMap.clear();
    }

    int getCacheScore(Color color, GameMap gameMap, Counter counter) {
        //分数缓存表
        Integer cacheValue = getCacheScore(gameMap.getMap());
        if (cacheValue != null) {
            return cacheValue;
        }
        counter.plusCount();
        int score = Score.getMapScore(gameMap, color);
        recordScore(gameMap.getMap(), score);
        return score;
    }

    private long getHashCode(Color[][] map) {
        long value = 0;
        for (int i = 0; i < Config.size; i++)
            for (int j = 0; j < Config.size; j++) {
                long[][] array = weightNull;
                if (map[i][j] == Color.BLACK) {
                    array = weightBlack;
                }
                if (map[i][j] == Color.WHITE) {
                    array = weightWhite;
                }
                value = value ^ array[i][j];
            }
        return value;
    }


}
