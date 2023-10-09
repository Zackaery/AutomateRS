package net.automaters.api.utils;

public class Calculator {

    protected static final java.util.Random random = new java.util.Random();
    
    /**
     * Returns a random double with min as the inclusive lower bound and max as
     * the exclusive upper bound.
     *
     * @param min The inclusive lower bound.
     * @param max The exclusive upper bound.
     * @return Random double min <= n < max.
     */
    public static double random(double min, double max) {
        return Math.min(min, max) + random.nextDouble() * Math.abs(max - min);
    }

    /**
     * Returns a random integer with min as the inclusive lower bound and max as
     * the exclusive upper bound.
     *
     * @param min The inclusive lower bound.
     * @param max The exclusive upper bound.
     * @return Random integer min <= n < max.
     */
    public static int random(int min, int max) {
        int n = Math.abs(max - min);
        return Math.min(min, max) + (n == 0 ? 0 : random.nextInt(n));
    }
}
