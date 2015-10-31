package boids.model.boid;

import boids.model.ColorDistance;
import static boids.model.boid.Boid.NEIGHBOR_RADIUS;
import java.util.Collection;
import java.util.HashSet;

public class ColorFlockingBoid extends Boid {

    @Override
    public Collection<Boid> getNeighbors(Collection<Boid> population) {
        HashSet<Boid> neighbors = new HashSet<>();
        if (!flockingEnabled) {
            return neighbors;
        }
        for (Boid b : population) {
            if (position.distanceFrom(b.position) <= NEIGHBOR_RADIUS
                    && ColorDistance.isSimilarColor(color, b.color)) {
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
}
