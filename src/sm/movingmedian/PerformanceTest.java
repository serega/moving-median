package sm.movingmedian;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;

/**
 * Author: Sergejs Melderis
 * Date: 4/2/17
 */
public class PerformanceTest {


    private int windowRangeStart = 4;
    private int windowRangeEnd = 1000;
    private int windowIncrement = 4;
    private int nIter = 500000;


    private long[][] results;

    private void init() {
        results = new long[((windowRangeEnd - windowRangeStart) / windowIncrement) + 1][];
    }


    private void executeTest(int window, int testRow) {
        System.out.format("Executing test window = %d \n", window);
        MovingMedian movingMedians[] = new MovingMedian[3];
        movingMedians[0] = new MovingMedianNaive(window);
        movingMedians[1] = new MovingMedianTreeSet(window);
        movingMedians[2] = new MovingMedianHeap(window);

        long resultRow[] = new long[4];
        resultRow[0] = window;

        Random random = new Random();
        double[] values = new double[nIter];
        for (int i = 0; i < nIter; i++) {
            values[i] = random.nextDouble();
        }

        double medians[] = new double[3];

        for (int i = 0; i < 3; i++) {
            MovingMedian movingMedian = movingMedians[i];
            long t1 = System.nanoTime();
            for (double value : values) {
                movingMedian.update(value);
            }
            long elapsed = System.nanoTime() - t1;
            resultRow[i + 1] =  elapsed / 1000000;
            medians[i] = movingMedian.getMedian();
        }
        results[testRow] = resultRow;

        double eps = 0.000000001;
        if (   Math.abs(medians[0] - medians[1]) > eps
            || Math.abs(medians[1] - medians[2]) > eps
            || Math.abs(medians[0] - medians[2]) > eps) {
            throw new RuntimeException("Inconsistent results for window " + window + ". Medians = " + Arrays.toString(medians));
        }
    }

    private void writeResults(String filename) throws IOException {
        BufferedWriter writer =  Files.newBufferedWriter(Paths.get(filename));
        writer.write("window,naive,treeset,heap");
        writer.newLine();
        for (long[] result : results) {
            for (long value : result) {
                writer.write(String.valueOf(value));
                writer.write(",");
            }
            writer.newLine();
        }
        writer.close();
    }

    public void execute() {
        int row = 0;
        for (int window = windowRangeStart; window <= windowRangeEnd; window += windowIncrement) {
            executeTest(window, row++);
        }
    }

    public static void main(String[] args) throws IOException {
        PerformanceTest test = new PerformanceTest();
        test.init();
        test.execute();
        test.writeResults(args[0]);
    }

}
