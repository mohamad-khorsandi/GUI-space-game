package units;

import game.Game;
import ui.BattleField;

import java.io.File;

public class Planet extends Unit {
    public Planet (String name, Team team, Speeds damageSpeed) {
        super(name, team,100, 1000, planetsDir, true, States.WAITING, damageSpeed);
        Thread t = new Thread(() -> {
            do {
                switch (state){
                    //in case planet faced an enemy in front panel
                    case FIGHTING:
                        try {
                            Thread.sleep(speed.waitTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Unit enemy = BattleField.ground[position.x][position.y + team.front].filledBy;
                        enemy.health -= damage;
                        if (enemy.health <= 0){
                            enemy.isAlive = false;
                            state = States.WAITING;
                        }
                        break;

                    //default case
                    case WAITING:
                        isThereEnemy = false;
                        while ( ! isThereEnemy) {
                            try {
                                synchronized (planetMonitor){
                                    planetMonitor.wait();
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                }
            } while (isAlive);
            BattleField.ground[position.x][position.y].removeImage();
            Game.end(Team.enemyTeam(this));
        });
        t.start();
    }

    boolean isThereEnemy;
    public static final File planetsDir = new File(Game.imagesDir,"planets");

    public static void initPlanets (){
        Team.ONE.planet = new Planet("one", Team.ONE, Speeds.MEDIUM);
        Team.TWO.planet = new Planet("two", Team.TWO, Speeds.MEDIUM);
    }
}
