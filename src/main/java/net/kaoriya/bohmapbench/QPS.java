package net.kaoriya.bohmapbench;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.cfelde.bohmap.BOHMap;
import com.cfelde.bohmap.Binary;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.mapdb.DBMaker;
import org.mapdb.DB;
import org.mapdb.HTreeMap;

public class QPS {

    public static class Param {
        public int warmUp;
        public int iteration;
        public int numOfItems;
        public double hitRate;
        public int keyMaxLen;
        public int valueMaxLen;

        public int bohmapPartitionCount;

        @Override
        public String toString() {
            return ReflectionToStringBuilder.toString(this,
                    ToStringStyle.MULTI_LINE_STYLE);
        }
    }

    public static class Result {
        public long elapasedNanoTime;
        public int queryCount;
        public int hitCount;

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("elapasedNanoTime",
                        String.format("%1$,3d", elapasedNanoTime))
                .append("QPS", String.format("%1$,3d", getQPS()))
                .append("hitRate", getHitRate())
                .toString();
        }

        public double getHitRate() {
            return (double)this.hitCount / this.queryCount;
        }

        public long getQPS() {
            return queryCount * (long)1e9 / this.elapasedNanoTime;
        }
    }

    public static Result runHashMap(Param p) {
        System.gc();
        System.out.println();
        System.out.println("HashMap");
        Result r = run(p, new HashMap<Binary, Binary>());
        System.out.println(r.toString());
        System.gc();
        return r;
    }

    public static Result runBOHMap(Param p) {
        System.gc();
        System.out.println();
        System.out.println("BOHMap");
        Result r = run(p, new BOHMap(p.bohmapPartitionCount));
        System.out.println(r.toString());
        System.gc();
        return r;
    }

    public static Result runBOHMapMurmurHash3(Param p) {
        System.gc();
        System.out.println();
        System.out.println("BOHMap+MurmurHash3");
        Result r = run(p, new BOHMap(p.bohmapPartitionCount,
                    Hash::murmurHash3));
        System.out.println(r.toString());
        System.gc();
        return r;
    }

    public static Result runMapDB(Param p) {
        System.out.println();
        System.out.println("MapDB");
        // Prepare DB.
        DB db = DBMaker
            .newMemoryDirectDB()
            .transactionDisable()
            .make();
        BinarySerializer sk = new BinarySerializer();
        BinarySerializer sv = new BinarySerializer();
        HTreeMap<Binary, Binary> map = db
            .createHashMap("bench")
            .keySerializer(sk)
            .valueSerializer(sv)
            .make();
        System.gc();
        // Run benchmark.
        Result r = run(p, map);
        System.out.println(r.toString());
        // Close DB.
        db.close();
        System.gc();
        return r;
    }

    public static Result run(Param p, Map<Binary, Binary> m) {
        return run(p, m, new Random());
    }

    public static Result run(Param p, Map<Binary, Binary> m, Random r) {
        List<Binary> keys = Utils.setupMap(m, r, p.numOfItems, p.hitRate,
                p.keyMaxLen, p.valueMaxLen);
        // Warm up.
        for (int i = 0; i < p.warmUp; ++i) {
            run(p, m, r, keys);
        }
        // Query to map.
        return run(p, m, r, keys);
    }

    public static Result run(Param p, Map<Binary, Binary> m, Random r,
            List<Binary> keys) {
        Result res = new Result();
        int keySize = keys.size();
        long startAt = System.nanoTime();

        // Query to map.
        int query = 0, hit = 0;
        for (int i = 0; i < p.iteration; ++i) {
            ++query;
            Binary v = m.get(keys.get(r.nextInt(keySize)));
            if (v != null) {
                ++hit;
            }
        }

        res.elapasedNanoTime = System.nanoTime() - startAt;
        res.queryCount = query;
        res.hitCount = hit;
        return res;
    }

    public static Param defaultParam() {
        Param p = new Param();
        p.warmUp = 4;
        p.iteration = 1000;
        p.numOfItems = 1000 * 1000;
        p.hitRate = 0.75;
        p.keyMaxLen = 128;
        p.valueMaxLen = 128;
        p.bohmapPartitionCount = (int)(p.numOfItems * 1.5);
        return p;
    }

    public static void run() {
        Param p = defaultParam();
        System.out.println();
        System.out.println("Param: " + p.toString());

        runHashMap(p);
        runBOHMap(p);
        runMapDB(p);
    }

    public static void runMurmur() {
        Param p = defaultParam();
        System.out.println();
        System.out.println("Param: " + p.toString());

        runBOHMap(p);
        runBOHMapMurmurHash3(p);
    }

    public static void runHitrate() {
        Param p = defaultParam();
        double[] rates = new double[]{ 0.25, 0.5, 0.75, 1.0 };

        System.out.println("hitrate");

        for (double r : rates) {
            p.hitRate = r;
            runBOHMap(p);
        }

        for (double r : rates) {
            p.hitRate = r;
            runHashMap(p);
        }
    }

}
