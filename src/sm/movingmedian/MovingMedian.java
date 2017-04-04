package sm.movingmedian;

/**
 * Calculates a running median of n double values over a potentially infinite stream.
 * 
 * Author: Sergejs Melderis
 * Date: 4/2/17
 */
public interface MovingMedian {

    /**
     * Adds a value to the window and returns the median of n values after the update. 
     * @param value
     * @return
     */
    public double update(double value);

    /**
     * Returns the current median of the window of double 
     * @return - the current median of n values
     */
    public double getMedian();

    public int getWindow();
}
