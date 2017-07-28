package core;

import entity.Point;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Analyzer {

    private GameMap gameMap;

    private List<Point> points;

    private final static int SIZE = 128;

    private Set<Point> fiveAttack;

    private Set<Point> fourAttack;

    private Set<Point> threeOpenAttack;

    private Set<Point> fourDefence;

    private Set<Point> threeDefence;

    private Set<Point> origin;

    private Set<Point> notKey;

    public Analyzer(GameMap gameMap, List<Point> points) {
        this.gameMap = gameMap;
        this.points = points;
    }


    public Set<Point> getFiveAttack() {
        if (fiveAttack != null) {
            return fiveAttack;
        }
        fiveAttack = new HashSet<>(SIZE);
        points.forEach(point -> {

        });
        return fiveAttack;
    }

    public Set<Point> getFourAttack() {
        return fourAttack;
    }

    public Set<Point> getThreeOpenAttack() {
        return threeOpenAttack;
    }

    public Set<Point> getFourDefence() {
        return fourDefence;
    }

    public Set<Point> getThreeDefence() {
        return threeDefence;
    }

    public Set<Point> getOrigin() {
        return origin;
    }

    public Set<Point> getNotKey() {
        return notKey;
    }
}
