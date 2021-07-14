package units;

import game.Game;
import ui.BattleField;
import ui.PaintPanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Unit {
    public Unit(String name, Team team, int damage, int health, File parentDir, boolean T_planet__F_ship, States state, Speeds speed) {
        this.name = name;

        try {
            this.icon = Game.getImg(parentDir, this.name);
        } catch (IOException e) { }

        this.team = team;
        this.damage = damage;
        this.health = health;

        while( ! this.placeTo(this.suggestPos(T_planet__F_ship))){ }

        this.state = state;
        this.speed = speed;
        isAlive = true;
    }

    public Unit(String name, int damage, Speeds speed, Team team) {
        this.name = name;
        this.damage = damage;
        this.speed = speed;
        this.team = team;
    }

    final String name;
    BufferedImage icon;

    final int damage;
    public int health;
    final Speeds speed;
    public final Team team;

    Point position;

    States state;
    public boolean isAlive;

    final static Object planetMonitor = new Object();
    boolean placeTo (Point newPosition) {
        PaintPanel brick = BattleField.ground [newPosition.x][newPosition.y];

        if ( ! brick.isFilled) {
            this.position = newPosition;
            brick.setImage(this.icon, team);
            brick.isFilled = true;
            brick.filledBy = this;
            return true;
        }
        else {
            return false;
        }
    }

    Point suggestPos(boolean T_planet__F_ship) {

        Random r = new Random();
        int randX = 1 + r.nextInt(Game.rows);
        int randY = 1 + r.nextInt(2);


        if (T_planet__F_ship){
            randX = (Game.rows / 2) + 1;
            randY = 1;
        }
        if (this.team.equals(Team.TWO)){
             randY = Team.colForTeamTow(randY);
        }

        return new Point(randX,randY);
    }

    void notifyIfPlanet (){
        if (this instanceof Planet) {
            ((Planet) this).isThereEnemy = true;
            synchronized (planetMonitor){
                planetMonitor.notifyAll();
            }
        }
    }
    void waitAMoment(){
        try {
            Thread.sleep(this.speed.waitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
