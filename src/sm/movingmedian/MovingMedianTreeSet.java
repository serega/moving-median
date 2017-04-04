package sm.movingmedian;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.TreeSet;

/**
 * Moving Median implementation that uses a pair of java.util.TreeSet to maintain 
 * two halves of the window in sorted order. The median is thus either the 
 * largest value of the left half, or the smallest of the right half, 
 * or the mean of the largest of the left and smallest of the right half. 
 * 
 * Author: Sergejs Melderis
 * Date: 4/2/17
 */
public class MovingMedianTreeSet implements MovingMedian {

    private static final class WindowElement implements Comparable {
        Double value;
        TreeSet<WindowElement> holder;

        @Override
        public int compareTo(Object o) {
            return value.compareTo(((WindowElement) o).value);
        }
    }

    private Queue<WindowElement> window;
    private TreeSet<WindowElement> left;
    private TreeSet<WindowElement> right;
    private int treeCap;
    private int windowLen;
    private double median;

    public MovingMedianTreeSet(int window) {
        if (window % 2 != 0) {
            throw new IllegalArgumentException("queue size must be even");
        }
        left = new TreeSet<>();
        right = new TreeSet<>();
        treeCap = window / 2;
        this.windowLen = window;
        this.window = new ArrayDeque<>(window);

        // Add dummy values to both trees to simplify the implementation of the insert().
        // There is no need to check for  empty tree!
        WindowElement dummy1 = new WindowElement();
        dummy1.holder = left;
        dummy1.value = 0.0;
        left.add(dummy1);
        this.window.add(dummy1);

        WindowElement dummy2 = new WindowElement();
        dummy2.holder = right;
        dummy2.value = 5.0;
        right.add(dummy2);
        this.window.add(dummy2);
    }

    public double update(double value) {
        if (window.size() >= windowLen) {
            WindowElement element = window.poll();
            element.holder.remove(element);
        }
        insert(value);
        calcMedian();
        return median;
    }


    private void insert(double value) {
        if (value <= left.last().value) {
            if (left.size() == treeCap || left.size() - right.size() >= 1) {
                WindowElement max = left.pollLast();
                right.add(max);
                max.holder = right;
            }
            WindowElement element = new WindowElement();
            element.holder = left;
            element.value = value;
            left.add(element);
            window.add(element);
        } else {
            if (right.size() == treeCap || right.size() - left.size() >= 1) {
                WindowElement min = right.pollFirst();
                left.add(min);
                min.holder = left;
            }
            WindowElement element = new WindowElement();
            element.holder = right;
            element.value = value;
            right.add(element);
            window.add(element);
        }
        maybeSwap();
    }

    private void maybeSwap() {
        if (left.last().value > right.first().value) {
            WindowElement leftEl = left.pollLast();
            WindowElement rightEl = right.pollFirst();
            left.add(rightEl);
            right.add(leftEl);
            leftEl.holder = right;
            rightEl.holder = left;
        }
    }

    private void calcMedian() {
        median = (left.last().value + right.first().value) / 2;
    }

    public double getMedian() {
        return median;
    }

    @Override
    public int getWindow() {
        return windowLen;
    }
}
