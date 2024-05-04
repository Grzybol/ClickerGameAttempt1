package com.example.clickergameattempt1;

import java.awt.*;

class Particle {
    Point position;
    int lifeSpan;
    int dx, dy; // Add velocity components for particles

    public Particle(Point position, int dx, int dy) {
        this.position = position;
        this.dx = dx;
        this.dy = dy;
        this.lifeSpan = 30; // lifespan in frames (30 frames = 1 second at 30 FPS)
    }

    public boolean update() {
        position.translate(dx, dy); // Update position based on velocity
        lifeSpan--;
        return lifeSpan > 0;
    }

    public void draw(Graphics g) {
        int alpha = (int) (255 * (lifeSpan / 30.0)); // Fade out effect
        int colorValue = Math.max(0, 255 - alpha); // Color fades to red
        g.setColor(new Color(colorValue, 0, 0, alpha));
        g.fillRect(position.x, position.y, 5, 5);
    }
}


