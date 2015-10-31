package boids.model.entities;

import boids.model.boid.Boid;
import java.awt.Graphics;

public class MouseCursor extends AbstractEntity implements Entity {

    private static final int EFFECT_RADIUS = 70;
    
    private boolean active = true;

    @Override
    public void drawSelf(Graphics g) {

    }

    @Override
    public double getEffectRadius() {
        return active?EFFECT_RADIUS:0;
    }

    @Override
    public double getAttractionStrength() {
        return -10;
    }

    @Override
    public void interact(Boid b) {
    }

    @Override
    public boolean canRemove() {
        return false;
    }

    @Override
    public boolean isGradientEffect() {
        return true;
    }

    public void activate() {
        active = true;
    }

    public void deActivate() {
        active = false;
    }

}
