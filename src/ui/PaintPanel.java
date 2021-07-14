package ui;

import game.Game;
import units.Planet;
import units.Team;
import units.Unit;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class PaintPanel extends JPanel {

    public BufferedImage image;
    public boolean isFilled = false;
    public Unit filledBy;
    static BufferedImage backGround;
    static {
        try {
            backGround = ImageIO.read(new File(Game.imagesDir, "background.jpeg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setImage(BufferedImage image, Team team) {
        this.image = team.getProperImg(image);
        this.repaint();
    }

    public void removeImage() {
        this.isFilled = false;
        this.repaint();
    }

    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        g.drawImage(backGround,0, 0, this);
        if (this.isFilled){
            int fitWidth, fitHeight;
            int x = 0;
            if (this.filledBy instanceof Planet){
                fitHeight = this.getHeight();
                fitWidth = (int)(image.getWidth() * ((double)fitHeight / image.getHeight()));
                if (filledBy.team.equals(Team.ONE)) x = this.getWidth() - fitWidth;
            }
            else {
                fitWidth = this.getWidth();
                fitHeight = (int)(image.getHeight() * ((double)fitWidth / image.getWidth()));

            }
            g.drawImage(image, x, 0, fitWidth, fitHeight, this);
        }
    }
}
