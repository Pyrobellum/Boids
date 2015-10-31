package boids.gui;

import boids.model.boid.Boid;
import boids.model.boid.Boundary;
import boids.model.boid.ColorFlockingBoid;
import boids.model.entities.BoidDeathAnimation;
import boids.model.entities.Entity;
import boids.model.entities.Food;
import boids.model.entities.MouseCursor;
import boids.model.entities.Vortex;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.event.MouseInputAdapter;

/**
 *
 * @author PrentJ
 */
public final class Main extends JFrame {

    VisualizationPane visualization;
    HashSet<Boid> population;
    HashSet<Entity> entities;
    MouseCursor mouse;

    public static void main(String[] args) {
        Main app = new Main();
        Timer ticker = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.update();
            }
        });
        ticker.start();
    }

    public Main() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        population = new HashSet<>();
        entities = new HashSet<>();
        Boid.setBoundary(new Boundary(400, 400));
        createBoids(50);

        visualization = new VisualizationPane(population, entities);
        setLayout(new BorderLayout());
        visualization.setPreferredSize(new Dimension(400, 400));
        add(visualization);
        pack();
        setVisible(true);

        addMouseListeners();

        addComponentListener(new ComponentListener() {

            @Override
            public void componentResized(ComponentEvent e) {
                Boid.setBoundary(new Boundary(visualization.getWidth(), visualization.getHeight()));
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {
            }
        });
    }

    public void addMouseListeners() {

        mouse = new MouseCursor();
        entities.add(mouse);
        mouse.setX(100);
        mouse.setY(100);
        MouseListener listener = new MouseListener();
        visualization.addMouseMotionListener(listener);
        visualization.addMouseListener(listener);
    }

    private class MouseListener extends MouseInputAdapter {

        @Override
        public void mouseMoved(MouseEvent e) {
            mouse.setX(e.getX());
            mouse.setY(e.getY());
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                Food food = new Food(e.getX(), e.getY());
                entities.add(food);
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                entities.add(new Vortex(e.getX(), e.getY()));
            } else if (e.getButton() == MouseEvent.BUTTON2) {
                for (int i = 0; i < 10; i++) {
                    population.add(new Boid(e.getX() + Math.random() * 4 - 2, e.getY() + Math.random() * 4 - 2));
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            mouse.activate();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            mouse.deActivate();
        }
    }

    public final void createBoids(int populationTarget) {
        for (int i = 0; i < populationTarget; i++) {
            ColorFlockingBoid b = new ColorFlockingBoid();
            b.randomizeBoid();
            population.add(b);
        }
    }

    public void update() {
        for (Iterator<Boid> it = population.iterator(); it.hasNext();) {
            Boid boid = it.next();
            if (boid.canRemove()) {
                entities.add(new BoidDeathAnimation(boid));
                it.remove();
            }
            boid.updateSelf(population, entities);
        }
        Iterator<Entity> entityIterator = entities.iterator();
        while (entityIterator.hasNext()) {
            Entity e = entityIterator.next();
            if (e.canRemove()) {
                entityIterator.remove();
            }
        }
        repaint();
    }

}
