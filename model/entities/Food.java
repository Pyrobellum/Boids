package boids.model.entities;

import boids.model.boid.Boid;
import java.awt.Color;
import java.awt.Graphics;

public class Food extends AbstractEntity implements Entity {

    private static final int RGB_MAX = 255;
    private static final int INTERACTION_DISTANCE = 8;
    private static final int DRAW_RADIUS = 15;
    private static final int EFFECT_RADIUS = 80;
    private static final int MAXIMUM_VISITS = 100;

    private int red, green, blue, remainingVisits;

    public Food(int x, int y) {
        this(x,y, Color.BLACK);
        double choice = Math.random() * 3;
        if (choice < 1) {
            setColor(Color.RED);
        } else if (choice < 2) {
            setColor(Color.GREEN);
        } else {
            setColor(Color.BLUE);
        }
        remainingVisits = MAXIMUM_VISITS;
    }

    public Food(int x, int y, Color color) {
        setX(x);
        setY(y);
        setColor(color);
        remainingVisits = MAXIMUM_VISITS;
    }
    
    private void setColor(Color color) {
        red = color.getRed();
        green = color.getGreen();
        blue = color.getBlue();
    }

    @Override
    public void interact(Boid b) {
        if (b.getPositionVector().distanceFrom(getPositionVector()) < INTERACTION_DISTANCE) {
            b.setColor(blendColor(b.getColor()));
//            red += (red >= RGB_MAX ? 0 : 1);
//            green += (green >= RGB_MAX ? 0 : 1);
//            blue += (blue >= RGB_MAX ? 0 : 1);
            remainingVisits--;
        }
    }

    @Override
    public boolean canRemove() {
        return remainingVisits <= 0;
    }

    @Override
    public boolean isGradientEffect() {
        return false;
    }

    @Override
    public void drawSelf(Graphics g) {
        g.setColor(new Color(red, green, blue, RGB_MAX * remainingVisits / MAXIMUM_VISITS));
        g.fillOval((int) x - DRAW_RADIUS, (int) y - DRAW_RADIUS, DRAW_RADIUS * 2, DRAW_RADIUS * 2);
    }

    @Override
    public double getEffectRadius() {
        return EFFECT_RADIUS;
    }

    @Override
    public double getAttractionStrength() {
        return .4;
    }

    public Color blendColor(Color baseColor) {
        int baseRed = baseColor.getRed();
        int baseGreen = baseColor.getGreen();
        int baseBlue = baseColor.getBlue();
        baseRed = (baseRed * 3 + red) / 4;
        baseGreen = (baseGreen * 3 + green) / 4;
        baseBlue = (baseBlue * 3 + blue) / 4;
        return new Color(baseRed, baseGreen, baseBlue);
    }
}
