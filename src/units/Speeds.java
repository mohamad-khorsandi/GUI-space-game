package units;

import game.Game;

public enum Speeds {
    SLOW(Game.APS / 2), MEDIUM(Game.APS), FAST(Game.APS * 2);

    Speeds(double APS) {
        this.APS = APS;
        this.waitTime = (long)(1000 / APS);
    }
    public final double APS; //Action Per Second
    public final long waitTime;
}
