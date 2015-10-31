package boids.model.boid;

import java.util.Collection;

public class Vector {

    private double x, y, z;

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector(double x, double y) {
        this(x, y, 0);
    }

    public Vector() {
        this(0, 0, 0);
    }

    public Vector duplicate() {
        return new Vector(x, y, z);
    }

    public void normalize() {
        if (magnitude() != 0) {

            divideBy(magnitude());
        }
    }

    public void add(Vector argument) {
        x += argument.x;
        y += argument.y;
        z += argument.z;
    }

    public void subtract(Vector argument) {
        x -= argument.x;
        y -= argument.y;
        z -= argument.z;
    }

    public void multiplyBy(double multiplicand) {
        x *= multiplicand;
        y *= multiplicand;
        z *= multiplicand;
    }

    public void divideBy(double divisor) {
        x /= divisor;
        y /= divisor;
        z /= divisor;
    }

    public double dotProduct(Vector argument) {
        return x * argument.x + y * argument.y + z * argument.z;
    }

    public double distanceFrom(Vector other) {
        return Math.sqrt(Math.pow(other.x - x, 2) + Math.pow(other.y - y, 2) + Math.pow(other.z - z, 2));
    }

    public double magnitude() {
        return Math.sqrt(dotProduct(this));
    }

    public static Vector average(Collection<Vector> vectors) {
        int vectorCount = 0;
        Vector average = new Vector();
        for (Vector v : vectors) {
            vectorCount++;
            average.x += v.x;
            average.y += v.y;
            average.z += v.z;
        }
        average.divideBy(vectorCount);
        return average;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void rotateInDegrees(double degrees) {
        rotateInRadians(Math.toRadians(degrees));
    }

    public void rotateInRadians(double radians) {
        double angle = getPolarAngleInRadians();
        angle += radians;
        double magnitude = magnitude();
        x = Math.cos(angle) * magnitude;
        y = Math.sin(angle) * magnitude;
    }

    public double getPolarAngleInRadians() {
        return Math.atan2(y, x);
    }

    public void setMagnitude(double magnitude) {
        normalize();
        multiplyBy(magnitude);
    }

    public String toString() {
        return "" + x + ", " + y;
    }
}
