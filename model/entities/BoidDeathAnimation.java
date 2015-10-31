package boids.model.entities;

import boids.model.boid.Boid;
import boids.model.boid.Vector;
import java.awt.Color;
import java.awt.Graphics;

public class BoidDeathAnimation extends AbstractEntity implements Entity {

    private static final int NUMBER_OF_PARTICLES = 5;
    private static final int DURATION = 120;
    
    private int remainingFrames;
    private final Color color;
    private final Vector destination;

    public BoidDeathAnimation(Boid b) {
        super.x = b.getPositionVector().getX();
        super.y = b.getPositionVector().getY();
        color = b.getColor();
        remainingFrames = DURATION;
        destination = b.getDestination();
    }

    @Override
    public double getEffectRadius() {
        return 50;
    }

    @Override
    public double getAttractionStrength() {
        return 0;
    }

    @Override
    public void interact(Boid b) {
    }

    @Override
    public boolean canRemove() {
        return remainingFrames < 0;
    }

    @Override
    public boolean isGradientEffect() {
        return true;
    }

    @Override
    public void drawSelf(Graphics g) {
        remainingFrames--;
        g.setColor(color);
        if (remainingFrames > 30) {
            int diameter = 10 * remainingFrames / 120;
            x = (x + destination.getX()) / 2;
            y = (y + destination.getY()) / 2;

            if (remainingFrames % 2 == 1) {
                diameter++;
            }
            g.drawOval((int) x, (int) y, diameter, diameter);
        } else {
            for (int i = 0; i < NUMBER_OF_PARTICLES; i++) {
                int x = (int)(super.x + Math.random() * 10 - 5);
                int y = (int)(super.y + Math.random() * 10 - 5);
                g.drawLine(x, y, x, y);
            }
        }

    }
}
