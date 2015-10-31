package boids.gui;

import boids.model.boid.Boid;
import boids.model.entities.Entity;
import java.awt.Graphics;
import java.util.Collection;
import javax.swing.JPanel;

/**
 * Draws the simulation
 * @author PrentJ
 */
public class VisualizationPane extends JPanel {

    private final Collection<Boid> boids;
    private final Collection<Entity> entities;

    public VisualizationPane(Collection<Boid> boids, Collection<Entity> entities) {
        this.boids = boids;
        this.entities = entities;

    }

    @Override
    public void paintComponent(Graphics g) {
        for (Entity e : entities) {
            e.drawSelf(g);
        }
        
        for (Boid b : boids) {
            b.drawSelf(g);
        }
    }
}
