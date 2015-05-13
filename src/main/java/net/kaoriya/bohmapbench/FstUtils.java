package net.kaoriya.bohmapbench;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Random;

public class FstUtils {

    public static class Benchmark {
        Random r = new Random();

        public QpsResult runQps(QpsParam p) {
            // TODO:
            return null;
        }
    }

    public static QpsResult runQps(QpsParam p) {
        Benchmark b = new Benchmark();
        return b.runQps(p);
    }

}
