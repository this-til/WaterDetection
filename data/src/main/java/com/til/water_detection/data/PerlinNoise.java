package com.til.water_detection.data;

import java.util.Random;

public class PerlinNoise {

    // 梯度向量  
    private static final double[][] GRADIENTS = {
            {1, 1}, {-1, 1}, {1, -1}, {-1, -1},
            {1, 0}, {-1, 0}, {0, 1}, {0, -1}
    };

    // 伪随机数生成器  
    private final Random random = new Random();

    // 生成0到1之间的柏林噪声  
    public double noise(double x, double y) {
        int Xint = (int) Math.floor(x) & 255;
        int Yint = (int) Math.floor(y) & 255;

        double Xfrac = x - Math.floor(x);
        double Yfrac = y - Math.floor(y);

        double u = fade(Xfrac);
        double v = fade(Yfrac);

        // Hash coordinates of the 4 corners  
        int A = hash(Xint, Yint);
        int B = hash(Xint + 1, Yint);
        int C = hash(Xint, Yint + 1);
        int D = hash(Xint + 1, Yint + 1);

        // And get the gradients of those 4 corners  
        double[] gradA = GRADIENTS[A % 8];
        double[] gradB = GRADIENTS[B % 8];
        double[] gradC = GRADIENTS[C % 8];
        double[] gradD = GRADIENTS[D % 8];

        // Compute the dot product of the gradient and the vector to the point  
        double dotA = gradA[0] * Xfrac + gradA[1] * Yfrac;
        double dotB = gradB[0] * (Xfrac - 1) + gradB[1] * Yfrac;
        double dotC = gradC[0] * Xfrac + gradC[1] * (Yfrac - 1);
        double dotD = gradD[0] * (Xfrac - 1) + gradD[1] * (Yfrac - 1);

        // Mix final noise value  
        return lerp(v, lerp(u, dotA, dotB), lerp(u, dotC, dotD));
    }

    // Hash function for permuting gradients  
    private int hash(int x, int y) {
        int h = 13 * x + 17 * y;
        h = (h << 13) ^ h;
        return (h * (h * h * 15731 + 789221) + 1376312589) & 0x7fffffff;
    }

    // 3D Hermite curve between two inputs  
    private double lerp(double t, double a, double b) {
        return a + t * (b - a);
    }

    // Fade function as defined by Ken Perlin  
    private double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

}