package ru.geek_brains.Catch_the_drop;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class GameWindow  extends JFrame {

    private static GameWindow game_wind;
    private static Image BackGround;
    private static Image GameOver;
    private static Image Drop;
    private static float dropLeft = 200;
    private static float dropTop = -100;
    private static float dropV = 200;
    private static long lastFrameTime;
    private static int score = 0;

    public static void main(String[] args) throws IOException {
        BackGround = ImageIO.read(GameWindow.class.getResourceAsStream("background.png"));
        GameOver = ImageIO.read(GameWindow.class.getResourceAsStream("game_over.png"));
        Drop = ImageIO.read(GameWindow.class.getResourceAsStream("drop.png"));
        game_wind = new GameWindow();
        game_wind.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game_wind.setLocation(400, 200);
        game_wind.setSize(906, 478);
        game_wind.setResizable(false);
        lastFrameTime=System.nanoTime();
        GameField gf = new GameField();
        gf.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                float dropRight = dropLeft + Drop.getWidth(null);
                float dropBottom = dropTop + Drop.getHeight(null);
                boolean is_drop = x>=dropLeft && x<=dropRight && y>=dropTop && y<=dropBottom;
                if (is_drop) {
                    dropTop = -100;
                    dropLeft = (int) (Math.random() * ( game_wind.getWidth() - Drop.getWidth(null)));
                    dropV += 10;
                    score++;
                    game_wind.setTitle("Score:  " + score);
                }
            }
        });
        game_wind.add(gf);
        game_wind.setVisible(true);
    }

    private static void onRepaint(Graphics gr) {
        long currentTime = System.nanoTime();
        float deltaTime = (currentTime-lastFrameTime) * 0.000000001f;
        lastFrameTime = currentTime;

        dropTop += (dropV * deltaTime);
        //dropLeft += (dropV * deltaTime);
        gr.drawImage(BackGround, 0,0, null);
        gr.drawImage(Drop, (int) dropLeft, (int) dropTop, null);
        if (dropTop > game_wind.getHeight()) {
            gr.drawImage(GameOver, 280, 120, null);
        }
    }

    private static class GameField extends JPanel {

        @Override
        protected void  paintComponent(Graphics g) {
            super.paintComponent(g);
            onRepaint(g);
            repaint();
        }
    }
}
