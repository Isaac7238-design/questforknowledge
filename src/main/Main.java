package main;

import javax.swing.JFrame;

/**
 * Main - entry point for Lucienne: Quest for Quality Education
 * Structure based on RyiSnow's Blue Boy Adventure tutorial.
 * Course: TMF2954 Java Programming | Theme: SDG 4 - Quality Education
 */
public class Main {

    public static JFrame window;

    public static void main(String[] args) {

        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Lucienne: Quest for Quality Education");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.setupGame();
        gamePanel.gameState = gamePanel.titleState; // Start on title screen
        gamePanel.startGameThread();
    }
}
