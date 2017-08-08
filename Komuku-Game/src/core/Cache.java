package core;

import java.util.LinkedHashMap;
import java.util.Map;

class Cache {

    Config config;

    GameMap gameMap;

    private QueueMap<Long, Boolean> cacheCombo = new QueueMap<>();

    private QueueMap<Long, Integer> cacheScore = new QueueMap<>();


    Cache(Config config, GameMap gameMap) {
        this.config = config;
        this.gameMap = gameMap;
    }

    void recordComboResult(boolean value) {
        if (config.cacheSize > 0) {
            cacheCombo.put(gameMap.getHashCode(), value);
        }
    }

    void recordScoreResult(int value) {
        if (config.cacheSize > 0) {
            cacheScore.put(gameMap.getHashCode(), value);
        }
    }

    Boolean getComboResult() {
        if (config.cacheSize > 0) {
            if (cacheCombo.containsKey(gameMap.getHashCode())) {
                return cacheCombo.get(gameMap.getHashCode());
            }
        }
        return null;
    }

    Integer getScoreResult() {
        if (config.cacheSize > 0) {
            if (cacheScore.containsKey(gameMap.getHashCode())) {
                return cacheScore.get(gameMap.getHashCode());
            }
        }
        return null;
    }

    class QueueMap<K, V> extends LinkedHashMap<K, V> {
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size() > config.cacheSize;
        }
    }
}
