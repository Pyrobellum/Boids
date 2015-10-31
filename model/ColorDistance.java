package boids.model;

import java.awt.Color;

public class ColorDistance {

    public static double distanceBetweenColorsViaLab(Color firstRGB, Color secondRGB) {
        double[] firstXYZ = getXYZ(firstRGB);
        double[] secondXYZ = getXYZ(secondRGB);
        double[] firstLab = getCIELab(firstXYZ);
        double[] secondLab = getCIELab(secondXYZ);
        double distance = Math.sqrt(
                Math.pow(firstLab[0] - secondLab[0], 2)
                + Math.pow(firstLab[0] - secondLab[0], 2)
                + Math.pow(firstLab[0] - secondLab[0], 2));
        return distance;
    }

    /* source: http://stackoverflow.com/questions/2103368/color-logic-algorithm  */
    public static double distanceBetweenColors(Color firstColor, Color secondColor) {
        double rmean = (firstColor.getRed() + secondColor.getRed()) / 2;
        int r = firstColor.getRed() - secondColor.getRed();
        int g = firstColor.getGreen() - secondColor.getGreen();
        int b = firstColor.getBlue() - secondColor.getBlue();
        double weightR = 2 + rmean / 256;
        double weightG = 4.0;
        double weightB = 2 + (255 - rmean) / 256;
        return Math.sqrt(weightR * r * r + weightG * g * g + weightB * b * b);
    }

    public static boolean isSimilarColor(Color first, Color second) {
        return distanceBetweenColors(first, second) < 200;
    }
    
    public static double getColorSeparationModifier(Color first, Color second) {
        if (distanceBetweenColors(first, second) < 200) {
            return 1;
        } else {
            return 3;
        }
    }

    public static double[] getXYZ(Color color) {
        double[] result = new double[3];
        result[0] = .412453 * color.getRed() + .357580 * color.getGreen() + .180423 * color.getBlue();
        result[1] = .212671 * color.getRed() + .715160 * color.getGreen() + .072169 * color.getBlue();
        result[2] = .019334 * color.getRed() + .119193 * color.getGreen() + .950227 * color.getBlue();
        return result;
    }

    public static double[] getCIELab(double[] XYZ) {
        double[] result = new double[3];
        result[0] = 116 * LabSubFunction(XYZ[1] - 16);
        result[1] = 500 * (LabSubFunction(XYZ[0] / 0.9642) - LabSubFunction(XYZ[1]));
        result[2] = 200 * (LabSubFunction(XYZ[1]) - LabSubFunction(XYZ[2] / .8249));
        return result;
    }

    public static double LabSubFunction(double x) {
        if (x > Math.pow(6.0 / 29, 3)) {
            return Math.pow(x, 1.0 / 3);
        } else {
            return 29 * 29 / 3 / 6 / 6 * x + 4 / 29;
        }
    }
}
