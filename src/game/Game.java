package game;

import ui.BattleField;
import units.Planet;
import units.Ship;
import units.Team;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Game {
    public static final int rows = 10;
    public static final int cols = 10;
    public static final double APS = 1;//game speed
    public static final File imagesDir = new File("images");
    public static final Object startMonitor = new Object();
    public static boolean isStarted = false;
    static BattleField battleField;
    public static ArrayList<Ship> ships = new ArrayList<>();
    public static boolean isEnd = false;
    public static void main(String[] args) throws InterruptedException {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                battleField = new BattleField();
            }
        });

        synchronized (startMonitor) {
            while ( !isStarted) {
                startMonitor.wait();
            }
        }

        Thread.sleep(100);
        Planet.initPlanets();
        Ship.initShips(Team.ONE);
        Ship.initShips(Team.TWO);
    }

    static public BufferedImage getImg (File parentDir, String imgName) throws IOException {
        BufferedImage img = ImageIO.read(new File(parentDir, imgName + ".png"));
        return img;
    }

    synchronized public static void end(Team winner){
        for (Ship ship : ships){
            ship.isAlive = false;
        }
        Game.isEnd = true;
        new Thread(() -> {
            JOptionPane.showMessageDialog(battleField, "team " + winner.name() + " won");
            System.out.println("team " + winner.name() + " won");
            System.exit(0);
        }).start();

    }
}
