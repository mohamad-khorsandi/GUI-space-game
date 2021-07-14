package units;

import game.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public enum Team {
    ONE(1), TWO(-1);

    Team(int front) {
        this.front = front;
    }

    public Planet planet;
    int front;
    public BufferedImage getProperImg (BufferedImage img) {
        if (this.equals(Team.ONE)) return img;

        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage flippedImage = new BufferedImage(w, h, img.getType());
        Graphics2D g = flippedImage.createGraphics();
        g.drawImage(img, 0, 0, w, h, w, 0, 0, h, null);
        g.dispose();
        return flippedImage;
    }
    public static int colForTeamTow (int column) {
        int disFromBeginning = column - 1;
        column = Game.cols - disFromBeginning;
        return column;
    }
    public static <T extends Unit>Team enemyTeam (T unit){
        if (unit.team.equals(Team.ONE)) return Team.TWO;
        else return Team.ONE;
    }
}
