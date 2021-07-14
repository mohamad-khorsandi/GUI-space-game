package ui;

import game.Game;
import units.Team;

import javax.swing.*;
import java.awt.*;

public class BattleField extends JFrame {
    public static PaintPanel[][] ground = new PaintPanel[Game.rows + 1][Game.cols + 1];
    static public JPanel mainPanel;

    public BattleField() throws HeadlessException {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);
        this.setLayout(new BorderLayout());

        mainPanel = new JPanel();

        GridLayout gridLayout = new GridLayout(Game.rows, Game.cols);
        mainPanel.setLayout(gridLayout);
        for (int i = 1; i <= Game.rows; i++) {
            for (int j = 1; j <= Game.cols; j++) {
                ground[i][j] = new PaintPanel();
                mainPanel.add(ground[i][j]);
            }
        }

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout());
        addButtons(buttonsPanel);

        this.add(mainPanel, BorderLayout.CENTER);
        this.add(buttonsPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    static JLabel lblOne;
    static JLabel lblTwo;
    void addButtons (JPanel panel) {

        JButton startBtn = new JButton("start");
        JButton endBtn = new JButton("end");
        lblOne = new JLabel();
        lblTwo = new JLabel();

        startBtn.addActionListener(e -> {
            Game.isStarted = true;
            synchronized (Game.startMonitor) {
                Game.startMonitor.notify();
            }
        });

        endBtn.addActionListener(e -> {
            System.exit(0);
        });

        startBtn.setFont(new Font("Verdana", Font.PLAIN, 16));

        endBtn.setFont(new Font("Verdana", Font.PLAIN, 16));

        lblOne.setFont(new Font("Verdana", Font.BOLD, 24));
        lblOne.setHorizontalAlignment(SwingConstants.CENTER);

        lblTwo.setFont(new Font("Verdana", Font.BOLD, 24));
        lblTwo.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(lblOne);
        panel.add(startBtn);
        panel.add(endBtn);
        panel.add(lblTwo);

    }
    public static void updateScores() {
        lblOne.setText(String.valueOf(Team.ONE.planet.health));
        lblTwo.setText(String.valueOf(Team.TWO.planet.health));
    }
}