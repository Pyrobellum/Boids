package boids.model.entities;

import boids.model.boid.Boid;
import boids.model.boid.Vector;
import java.awt.Graphics;

public interface Entity {
    public double getEffectRadius();
    public double getAttractionStrength();
    public abstract void interact(Boid b);
    public abstract boolean canRemove();
    public boolean isGradientEffect();
    public double getX();
    public double getY();
    public void setX(double x);
    public void setY(double y);
    public Vector getPositionVector();
    public void drawSelf(Graphics g);
}
