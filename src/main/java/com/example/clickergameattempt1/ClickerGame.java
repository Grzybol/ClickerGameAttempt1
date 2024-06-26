package com.example.clickergameattempt1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ClickerGame extends JFrame {
    private int health = 1000;
    private int startHealth = 1000;
    private int deathCount =0;
    private JButton clickButton;
    private JLabel healthLabel;
    private JPanel gamePanel;
    private List<Point> smallCircles;
    private List<Point> projectiles;
    private final Random random = new Random();
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private final Point bossCenter = new Point(WIDTH / 2, HEIGHT / 2); // Updated to exact center point for the boss
    private final int bossRadius = 50; // Boss radius
    private List<Particle> particles;


    public ClickerGame() {
        setTitle("Clicker Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        particles = new ArrayList<>();


        clickButton = new JButton("Spawn Small Circle");
        clickButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int x, y;
                do {
                    x = random.nextInt(WIDTH - 50);
                    y = random.nextInt(HEIGHT - 50);
                } while (Math.sqrt(Math.pow(x - bossCenter.x, 2) + Math.pow(y - bossCenter.y, 2)) < 2 * bossRadius);
                smallCircles.add(new Point(x, y));
            }
        });

        healthLabel = new JLabel("Health: " + health);
        healthLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.RED);
                g.fillOval(bossCenter.x - 50, bossCenter.y - 50, bossRadius*2, bossRadius*2);
                g.setColor(Color.BLUE);
                smallCircles.forEach(point -> g.fillOval(point.x, point.y, 10, 10));
                g.setColor(Color.GREEN);
                projectiles.forEach(point -> g.fillOval(point.x, point.y, 5, 5));

                // Draw particles
                particles.forEach(particle -> particle.draw(g));
            }


        };
        gamePanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        add(healthLabel, BorderLayout.NORTH);
        add(clickButton, BorderLayout.SOUTH);
        add(gamePanel, BorderLayout.CENTER);

        smallCircles = new ArrayList<>();
        projectiles = new ArrayList<>();

        // Timer to move projectiles
        new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveProjectiles();
                gamePanel.repaint();
            }
        }).start();

        // Timer to spawn new projectiles every second
        new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Point smallCircle : smallCircles) {
                    projectiles.add(new Point(smallCircle.x, smallCircle.y)); // Spawn projectile from each minion
                }
            }
        }).start();

        // Timer to handle health reduction and game state
        new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (health > 0) {
                    health -= smallCircles.size();
                    healthLabel.setText("Health: " + Math.max(0, health));
                } else {
                    deathCount++;
                    health = (int) ((int) startHealth*Math.pow(2, deathCount));
                    projectiles.clear();
                    smallCircles.clear();
                    healthLabel.setText("Health: " + health);

                }
                gamePanel.repaint();
            }
        }).start();
        // Timer to update and redraw particles
        new Timer(33, new ActionListener() { // Assuming ~30 FPS
            @Override
            public void actionPerformed(ActionEvent e) {
                Iterator<Particle> particleIterator = particles.iterator();
                while (particleIterator.hasNext()) {
                    Particle particle = particleIterator.next();
                    if (!particle.update()) {
                        particleIterator.remove();
                    }
                }
                gamePanel.repaint();
            }
        }).start();


        setVisible(true);
    }


    private void moveProjectiles() {
        Iterator<Point> iterator = projectiles.iterator();
        Random rand = new Random();
        while (iterator.hasNext()) {
            Point p = iterator.next();
            int dx = bossCenter.x - p.x;
            int dy = bossCenter.y - p.y;
            double dist = Math.sqrt(dx * dx + dy * dy);
            dx = (int) (dx / dist * 5); // Move speed of 5 pixels per tick
            dy = (int) (dy / dist * 5);

            p.translate(dx, dy);

            if (Math.sqrt(Math.pow(p.x - bossCenter.x, 2) + Math.pow(p.y - bossCenter.y, 2)) < 50) {
                health -= 1; // Damage the boss
                iterator.remove(); // Remove the projectile

                // Spawn particles at the hit location
                for (int i = 0; i < 10; i++) {
                    int velX = rand.nextInt(10) - 5; // Random velocity X between -5 and 5
                    int velY = rand.nextInt(10) - 5; // Random velocity Y between -5 and 5
                    particles.add(new Particle(new Point(p.x, p.y), velX, velY));
                }

                healthLabel.setText("Health: " + health);
            }
        }
    }



    public static void main(String[] args) {
        new ClickerGame();
    }
}
