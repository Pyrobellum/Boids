package boids.model.entities;

import boids.model.boid.Vector;

/**
 * Interface for non-boid entities that boids may wish to interact with or avoid.  
 */
public abstract class AbstractEntity {
    
    protected double x;
    protected double y;
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    public Vector getPositionVector() {
        return new Vector(x,y);
    }
}
