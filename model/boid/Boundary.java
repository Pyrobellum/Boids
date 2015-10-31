package boids.model.boid;

public class Boundary {
    
    public int maximumX;
    public int maximumY;
    
    public Boundary(int x, int y) {
        this.maximumX = x;
        this.maximumY = y;
    }
    
    public void wrapToWithinBounds(Vector position) {
        if (!isOutOfBounds(position)) {
            return;
        } 
        if (position.getX() < 0) {
            position.add(new Vector(maximumX,0,0));
        } 
        if (position.getX() > maximumX) {
            position.add(new Vector(-maximumX,0,0));
        } 
        if (position.getY() < 0) {
            position.add(new Vector(0,maximumY,0));
        } 
        if (position.getY() > maximumY) {
            position.add(new Vector(0,-maximumY,0));
        }
    }
    
    public boolean isOutOfBounds(Vector position) {
        if (position.getX() > maximumX || position.getX() < 0 || 
                position.getY() > this.maximumY || position.getY() < 0) {
            return true;
        } else  {
            return false;
        }
    }
    
    
}
