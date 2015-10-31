package boids.model.boid;

import boids.model.entities.Entity;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Collection;
import java.util.HashSet;

/**
 * Single instance of a flocking entity
 */
public class Boid {

    public static final double MINIMUM_DESIRED_SEPARATION_DISTANCE = 20;
    public static final double SEPARATION_CORRECTION_MAGNITUDE = 10;
    public static final double NEIGHBOR_RADIUS = 35;
    public static final double NEIGHBOR_CORRECTION_MAGNITUDE = .8;
    public static final double MAXIMUM_CORRECTIVE_ACCELERATION = 1.5;
    public static final double NATUAL_ACCELERATION_RATE = 1.05;
    public static final double MAXIMUM_SPEED = 5;

    protected static Boundary boundary;
    protected Vector velocity;
    protected Vector position;
    protected Color color;
    protected boolean canRemove = false;
    protected boolean flockingEnabled = true;
    protected Vector destination;

    public Boid() {
        this(5, 5);
    }

    public Boid(double x, double y) {
        velocity = new Vector();
        position = new Vector(x, y);
        color = Color.BLACK;
    }

    public void updateSelf(Collection<Boid> population, Collection<Entity> entities) {
        Vector acceleration = getAcceleration(population, entities);
        velocity.add(acceleration);
        velocity.multiplyBy(NATUAL_ACCELERATION_RATE); //gradual acceleration up to top speed
        if (velocity.magnitude() > MAXIMUM_SPEED) {
            velocity.setMagnitude(MAXIMUM_SPEED);
        }
        position.add(velocity);
        boundary.wrapToWithinBounds(position);
    }

    private Vector getAcceleration(Collection<Boid> population, Collection<Entity> entities) {
        Vector acceleration = new Vector();
        Collection<Boid> neighbors = getNeighbors(population);
        Collection<Boid> closeNeighbors = getCloseNeighbors(population);

        Vector coherence = cohereTo(neighbors);
        Vector alignment = alignTo(neighbors);
        Vector separation = avoid(closeNeighbors);
        Vector entityInfluence = getEntityInfluence(entities);

        acceleration.add(coherence);
        acceleration.add(alignment);
        acceleration.add(separation);
        acceleration.add(entityInfluence);

        acceleration.setMagnitude(MAXIMUM_CORRECTIVE_ACCELERATION);

        return acceleration;
    }

    public void randomizeBoid() {
        if (boundary == null) {
            throw new IllegalStateException("Error: Boundary not set for Boids");
        }
        setX(Math.random() * boundary.maximumX);
        setY(Math.random() * boundary.maximumY);
        velocity.setX(Math.random() * 2 - 1);
        velocity.setY(Math.random() * 2 - 1);
    }

    public Boid duplicate() {
        Boid result = new Boid();
        result.velocity = velocity.duplicate();
        result.position = position.duplicate();
        return result;
    }

    public static Boundary getBoundary() {
        return boundary;
    }

    public static void setBoundary(Boundary bounds) {
        Boid.boundary = bounds;
    }

    public Vector cohereTo(Collection<Boid> neighbors) {
        HashSet<Vector> positions = new HashSet<>();
        Vector desiredVelocity;
        for (Boid neighbor : neighbors) {
            positions.add(neighbor.position);
        }
        if (positions.isEmpty()) {
            desiredVelocity = this.velocity;
        } else {
            desiredVelocity = Vector.average(positions);
            desiredVelocity.subtract(this.position);
        }
        if (desiredVelocity.magnitude() > NEIGHBOR_CORRECTION_MAGNITUDE) {
            desiredVelocity.setMagnitude(NEIGHBOR_CORRECTION_MAGNITUDE);
        }
        return desiredVelocity;
    }

    public Vector alignTo(Collection<Boid> neighbors) {
        HashSet<Vector> velocities = new HashSet<>();
        Vector desiredVelocity;
        for (Boid neighbor : neighbors) {
            velocities.add(neighbor.velocity);
        }
        if (velocities.isEmpty()) {
            desiredVelocity = this.velocity;
        } else {
            desiredVelocity = Vector.average(velocities);
            desiredVelocity.subtract(this.velocity);
        }
        if (desiredVelocity.magnitude() > NEIGHBOR_CORRECTION_MAGNITUDE) {
            desiredVelocity.setMagnitude(NEIGHBOR_CORRECTION_MAGNITUDE);
        }
        return desiredVelocity;
    }

    public Vector avoid(Collection<Boid> closeNeighbors) {
        if (closeNeighbors.isEmpty()) {
            return new Vector();
        }
        Vector desiredVelocity = new Vector();
        int neighborCount = 0;
        for (Boid neighbor : closeNeighbors) {
            Vector correction = new Vector();
            correction.add(this.position);
            correction.subtract(neighbor.position);
            correction.setMagnitude(MINIMUM_DESIRED_SEPARATION_DISTANCE - correction.magnitude());
            desiredVelocity.add(correction);
            neighborCount++;
        }
        desiredVelocity.divideBy(neighborCount);
        if (desiredVelocity.magnitude() > SEPARATION_CORRECTION_MAGNITUDE) {
            desiredVelocity.setMagnitude(SEPARATION_CORRECTION_MAGNITUDE);
        }
        return desiredVelocity;
    }

    public Vector getEntityInfluence(Collection<Entity> entities) {
        Vector influence = new Vector();
        for (Entity entity : entities) {
            if (position.distanceFrom(entity.getPositionVector()) < entity.getEffectRadius()) {
                Vector attractionVector = entity.getPositionVector();
                attractionVector.subtract(position);
                double attraction = entity.getAttractionStrength();
                if (entity.isGradientEffect()) {
                    attraction *= position.distanceFrom(entity.getPositionVector()) / entity.getEffectRadius();
                }
                attractionVector.multiplyBy(entity.getAttractionStrength());
                influence.add(attractionVector);
                entity.interact(this);
            }
        }
        return influence;
    }

    public Collection<Boid> getNeighbors(Collection<Boid> population) {
        HashSet<Boid> neighbors = new HashSet<>();
        if (!flockingEnabled) {
            return neighbors;
        }
        for (Boid b : population) {
            if (position.distanceFrom(b.position) <= NEIGHBOR_RADIUS) {
                neighbors.add(b);
            }
        }
        for (Boid b : getWrappedBoids(population)) {
            if (position.distanceFrom(b.position) <= NEIGHBOR_RADIUS) {
                neighbors.add(b);
            }
        }
        return neighbors;
    }

    public Collection<Boid> getCloseNeighbors(Collection<Boid> population) {
        HashSet<Boid> neighbors = new HashSet<>();
        for (Boid b : population) {
            if (position.distanceFrom(b.position) <= MINIMUM_DESIRED_SEPARATION_DISTANCE) {
                neighbors.add(b);
            }
        }
        for (Boid b : getWrappedBoids(population)) {
            if (position.distanceFrom(b.position) <= MINIMUM_DESIRED_SEPARATION_DISTANCE) {
                neighbors.add(b);
            }
        }
        return neighbors;
    }

    public Collection<Boid> getWrappedBoids(Collection<Boid> population) {
        if (boundary == null) {
            throw new IllegalStateException("Error: no boundary found");
        }
        HashSet<Boid> wrappedBoids = new HashSet<>();
        for (Boid b : population) {
            if (Math.min(boundary.maximumX - position.getX(), position.getX()) < NEIGHBOR_RADIUS) {
                Boid xWrappedBoid = duplicate();
                xWrappedBoid.setX(boundary.maximumX - xWrappedBoid.getX());
            }
            if (Math.min(boundary.maximumY - position.getY(), position.getY()) < NEIGHBOR_RADIUS) {
                Boid yWrappedBoid = duplicate();
                yWrappedBoid.setY(boundary.maximumY - yWrappedBoid.getY());
            }
            if (Math.min(boundary.maximumX - position.getX(), position.getX()) < NEIGHBOR_RADIUS
                    && Math.min(boundary.maximumY - position.getY(), position.getY()) < NEIGHBOR_RADIUS) {
                Boid cornerWrappedBoid = duplicate();
                cornerWrappedBoid.setX(boundary.maximumX - cornerWrappedBoid.getX());
                cornerWrappedBoid.setY(boundary.maximumY - cornerWrappedBoid.getY());
            }
        }

        return wrappedBoids;
    }

    public double getX() {
        return position.getX();
    }

    public double getY() {
        return position.getY();
    }

    public void setX(double x) {
        position.setX(x);
    }

    public void setY(double y) {
        position.setY(y);
    }

    public Vector getPositionVector() {
        return position.duplicate();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setRemovable(boolean newState) {
        canRemove = newState;
    }

    public boolean canRemove() {
        return canRemove;
    }

    public void setDestination(Vector destination) {
        this.destination = destination.duplicate();
    }

    public Vector getDestination() {
        return destination.duplicate();
    }

    public void drawSelf(Graphics g) {
        g.setColor(color);
        if (flockingEnabled) {
            g.fillOval((int) position.getX() - 5, (int) position.getY() - 5, 10, 10);
        } else {
            g.drawOval((int) position.getX() - 5, (int) position.getY() - 5, 10, 10);
        }
    }

    public void disableFlocking() {
        flockingEnabled = false;
    }

    public void enableFlocking() {
        flockingEnabled = true;
    }
}
