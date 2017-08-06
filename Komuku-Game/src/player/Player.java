package player;

import core.Config;
import core.Game;
import core.Result;
import entity.CountData;
import enumeration.Color;
import enumeration.Level;

public class Player {

    Game game;

    public Player(Color[][] map, Level level) {
        game = new Game();
        Config config = new Config();
        if (level == Level.EASY) {
            config.comboDeep = 0;
            config.searchDeep = 4;
        }
        if (level == Level.NORMAL) {
            config.comboDeep = 0;
            config.searchDeep = 6;
        }
        if (level == Level.HIGH) {
            config.comboDeep = 7;
            config.searchDeep = 6;
        }
        if (level == Level.VERY_HIGH) {
            config.comboDeep = 11;
            config.searchDeep = 8;
        }
        game.init(map, config);
    }

    public CountData getCountData() {
        return game.getCountData();
    }

    public Result play(Color color) {
        return game.search(color);
    }

}
