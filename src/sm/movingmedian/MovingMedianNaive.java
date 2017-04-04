package sm.movingmedian;

import java.util.Arrays;

/**
 * Naive implementation of the MovingMedian algorithm. On every update the array representing the window
 * is copied into another array and sorted using Arrays.sort(). 
 * 
 * Author: Sergejs Melderis
 * Date: 4/2/17
 */
public class MovingMedianNaive implements MovingMedian {
    private double median;
    private double[] window;
    private double[] sorted;
    
    private int pos;
    private int half;


    public MovingMedianNaive(int window) {
        this.window = new double[window];
        half = window / 2;
    }

    @Override
    public double update(double value) {
        window[pos] = value;
        if (++pos == window.length) {
            pos = 0;
        }

        sorted = Arrays.copyOf(window, window.length);
        Arrays.sort(sorted);
        median = (sorted[half - 1] + sorted[half]) / 2;
        return median;
    }

    @Override
    public double getMedian() {
        return median;
    }

    @Override
    public int getWindow() {
        return window.length;
    }
}
