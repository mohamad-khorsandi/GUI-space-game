package units;

import game.Game;
import ui.BattleField;
import ui.PaintPanel;

import java.awt.*;
import java.io.File;
import java.util.Random;

public class Ship extends Unit {
    public Ship(String name, Team team, int damage, int health, NatureElements type, Speeds speed) {
        super(name, team, damage, health, shipsDir, false, States.MOVING, speed);
        this.type = type;

        new Thread(() -> {
            do {
                switch (state){
                    //default case
                    case MOVING:
                        waitAMoment();
                        move();
                        PaintPanel frontPanel = BattleField.ground[position.x][position.y + team.front];

                        try {
                            if (frontPanel.isFilled) {
                                Unit enemy = frontPanel.filledBy;
                                if (enemy.team.equals(team)) {
                                    state = States.WAITING;
                                }else {
                                    state = States.FIGHTING;
                                    enemy.state = States.FIGHTING;
                                    enemy.notifyIfPlanet();
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("out of bound");
                        }
                        break;

                    //in case ship faced an enemy in front panel
                    case FIGHTING:
                        waitAMoment();
                        Unit enemy = BattleField.ground[position.x][position.y + team.front].filledBy;
                        enemy.health -= damage;
                        if (enemy instanceof Planet) BattleField.updateScores();
                        if (enemy.health <= 0){
                            enemy.isAlive = false;
                            state = States.MOVING;
                        }
                        break;

                    //in case ship stock behind a unit that cant be attacked
                    case WAITING:
                        waitAMoment();
                        state = States.MOVING;
                        break;
                }
            }while (isAlive);
            BattleField.ground[position.x][position.y].removeImage();
        }).start();
    }

    final NatureElements type;

    static File shipsDir = new File(Game.imagesDir, "ships");

    public static void initShips(Team team){
        for (int i = 0; i < Game.rows / 4 ; i++) {
            randShipChoose(team);
        }

        new Thread(() -> {
            do {
                randShipChoose(team);
                try {
                    Thread.sleep(Speeds.MEDIUM.waitTime * 5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }while (! Game.isEnd);
        }).start();
    }

    static Ship randShipChoose  (Team team){
        Random r = new Random();
        Unit newShip = new Unit(null, 0,null, null);
        switch (1 + r.nextInt(9)){
            case  1:
                newShip = new Fire1(team);
                break;

            case  2:
                newShip = new Fire2(team);
                break;

            case  3:
                newShip = new Fire3(team);
                break;

            case  4:
                newShip = new Soil1(team);
                break;

            case  5:
                newShip = new Soil2(team);
                break;

            case  6:
                newShip = new Soil3(team);
                break;

            case  7:
                newShip = new Wind1(team);
                break;

            case  8:
                newShip = new Wind2(team);
                break;

            case  9:
                newShip = new Wind3(team);
                break;

            case  10:
                newShip = new Ice1(team);
                break;

            case  11:
                newShip = new Ice2(team);
                break;

            case  12:
                newShip = new Ice3(team);
                break;
        }
        Game.ships.add((Ship)newShip);
        return (Ship)newShip;
    }
    public void move () {
        Planet enemyPlanet;
        if (this.team.equals(Team.ONE)) enemyPlanet = Team.TWO.planet;
        else enemyPlanet = Team.ONE.planet;

        int disY = enemyPlanet.position.y - this.position.y;
        int disX = enemyPlanet.position.x - this.position.x;
        Point newPos;

        if (Math.abs(disX) >= Math.abs(disY)) newPos = new Point(position.x + (disX / Math.abs(disX)), position.y);
        else newPos = new Point(position.x, position.y + (disY / Math.abs(disY)));

        Point oldPos = this.position;
        if (placeTo(newPos)) BattleField.ground[oldPos.x][oldPos.y].removeImage();
    }


}

class Fire1 extends Ship {

    public Fire1(Team team) {
        super("Fire1", team, 100, 100, NatureElements.FIRE, Speeds.MEDIUM);
    }
}

class Fire2 extends Ship {

    public Fire2(Team team) {
        super("Fire2", team, 80, 300, NatureElements.FIRE, Speeds.SLOW);
    }
}
class Fire3 extends Ship {

    public Fire3(Team team) {
        super("Fire3", team, 90, 250, NatureElements.FIRE, Speeds.SLOW);
    }
}
class Wind1 extends Ship {

    public Wind1(Team team) {
        super("Wind1", team, 30,  250, NatureElements.WIND, Speeds.FAST);
    }
}
class Wind2 extends Ship {

    public Wind2(Team team) {
        super("Wind2", team, 35, 200, NatureElements.WIND, Speeds.FAST);
    }
}
class Wind3 extends Ship {

    public Wind3(Team team) {
        super("Wind3", team, 25, 300, NatureElements.WIND, Speeds.FAST);
    }
}
class Ice1 extends Ship {

    public Ice1(Team team) {
        super("Ice1", team, 30, 500, NatureElements.ICE, Speeds.SLOW);
    }
}
class Ice2 extends Ship {

    public Ice2(Team team) {
        super("Ice2", team, 25, 550, NatureElements.ICE, Speeds.SLOW);
    }
}
class Ice3 extends Ship {

    public Ice3(Team team) {
        super("Ice3", team, 35, 400, NatureElements.ICE, Speeds.MEDIUM);
    }
}
class Soil1 extends Ship {
    public Soil1(Team team) {
        super("Soil1", team, 40, 350, NatureElements.SOIL, Speeds.MEDIUM);
    }
}
class Soil2 extends Ship {
    public Soil2(Team team) {
        super("Soil2", team, 35, 300, NatureElements.SOIL, Speeds.MEDIUM);
    }
}
class Soil3 extends Ship {
    public Soil3(Team team) {
        super("Soil3", team, 25, 400, NatureElements.SOIL, Speeds.MEDIUM);
    }
}
