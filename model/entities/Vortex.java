package boids.model.entities;

import boids.model.boid.Boid;
import boids.model.boid.Vector;
import java.awt.Graphics;

public class Vortex extends AbstractEntity implements Entity {

    private static final double ANIMATION_ROTATION_INCREMENT = Math.toRadians(-12);
    private static final double INITIAL_SPIRAL_SEGMENT_LENGTH = 1.2;
    private static final double SPIRAL_LINE_SEGMENT_GROWTH_FACTOR = 1.1;
    private static final double EVENT_HORIZON_DISTANCE = 15;
    private static final int MAXIMUM_DURATION_IN_FRAMES = 180;
    private static final double ANGULAR_VELOCITY = Math.PI / 6;
    private static final double EFFECT_RADIUS = 200;
    private static final double ATTRACTION_STRENGTH = 2;

    private double spiralStartingAngle;
    private int duration;

    public Vortex(int x, int y) {
        super.x = x;
        super.y = y;
        spiralStartingAngle = 0;
        duration = MAXIMUM_DURATION_IN_FRAMES;
    }

    @Override
    public double getEffectRadius() {
        return EFFECT_RADIUS;
    }

    @Override
    public double getAttractionStrength() {
        return ATTRACTION_STRENGTH;
    }

    @Override
    public void interact(Boid b) {
        double distance = b.getPositionVector().distanceFrom(getPositionVector());
        if (distance < EFFECT_RADIUS) {
            b.disableFlocking();
            if (distance < EVENT_HORIZON_DISTANCE) {
                b.setDestination(new Vector(x, y));
                b.setRemovable(true);
            }
        } 
    }

    @Override
    public boolean canRemove() {
        return duration < 0;
    }

    @Override
    public boolean isGradientEffect() {
        return true;
    }

    @Override
    public void drawSelf(Graphics g) {
        duration--;
        spiralStartingAngle += ANIMATION_ROTATION_INCREMENT;
        spiralStartingAngle %= 2 * Math.PI;
        double angle = spiralStartingAngle;
        double length = INITIAL_SPIRAL_SEGMENT_LENGTH;
        double angularVelocity = Math.PI / 3;
        double currentX = super.x;
        double currentY = super.y;
        for (int i = 0; i < 20; i++) {
            angle += ANGULAR_VELOCITY;
            g.drawLine((int) currentX, (int) currentY, (int) (Math.cos(angle) * length + currentX), (int) (Math.sin(angle) * length + currentY));
            currentX += length * Math.cos(angle);
            currentY += length * Math.sin(angle);
            length *= SPIRAL_LINE_SEGMENT_GROWTH_FACTOR;
        }
    }

}
