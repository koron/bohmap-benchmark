package net.kaoriya.bohmapbench;

import com.cfelde.bohmap.BOHMap;
import com.cfelde.bohmap.Binary;
import java.util.Map;
import java.util.HashMap;

public class Main {

    public static class Param {
        public int iteration;
        public int numOfItems;
        public double hitRate;
        public int keyMaxLen;
        public int valueMaxLen;

        public int bohmapPartitionCount;
    }

    public static class Result {
        public long elapasedNanoTime;
        public double hitRate;
    }

    public static void main(String[] args) {
        Param p = new Param();
        p.iteration = 1000;
        p.numOfItems = 1000 * 1000;
        p.hitRate = 0.75;
        p.keyMaxLen = 128;
        p.valueMaxLen = 128;
        p.bohmapPartitionCount = (int)(p.numOfItems * 1.5);

        System.out.println("HashMap");
        Result r1 = run(p, new HashMap<Binary, Binary>());
        System.gc();

        System.out.println("BOHMap");
        Result r2 = run(p, new BOHMap(p.bohmapPartitionCount));
        System.gc();
    }

    public static Result run(Param p, Map<Binary, Binary> m) {
        Result r = new Result();
        long startAt = System.nanoTime();

        // TODO:

        r.elapasedNanoTime = System.nanoTime() - startAt;
        return r;
    }
}
