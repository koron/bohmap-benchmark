package net.kaoriya.bohmapbench;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.cfelde.bohmap.BOHMap;
import com.cfelde.bohmap.Binary;
import org.mapdb.DBMaker;
import org.mapdb.DB;
import org.mapdb.HTreeMap;

public class QPS {

    public static QpsResult runHashMap(QpsParam p) {
        System.gc();
        System.out.println();
        System.out.println("HashMap");
        QpsResult r = run(p, new HashMap<Binary, Binary>());
        System.out.println(r.toString());
        System.gc();
        return r;
    }

    public static QpsResult runBOHMap(QpsParam p) {
        System.gc();
        System.out.println();
        System.out.println("BOHMap");
        QpsResult r = run(p, new BOHMap(p.bohmapPartitionCount));
        System.out.println(r.toString());
        System.gc();
        return r;
    }

    public static QpsResult runBOHMapMurmurHash3(QpsParam p) {
        System.gc();
        System.out.println();
        System.out.println("BOHMap+MurmurHash3");
        QpsResult r = run(p, new BOHMap(p.bohmapPartitionCount,
                    Hash::murmurHash3));
        System.out.println(r.toString());
        System.gc();
        return r;
    }

    public static QpsResult runMapDB(QpsParam p) {
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
        QpsResult r = run(p, map);
        System.out.println(r.toString());
        // Close DB.
        db.close();
        System.gc();
        return r;
    }

    public static QpsResult runSparkey(QpsParam p) {
        System.gc();
        System.out.println();
        System.out.println("Sparkey");
        File f = new File("var/sparkey-qps/benchmark");
        QpsResult r = SparkeyUtils.runQps(f, p);
        System.out.println(r.toString());
        System.gc();
        return r;
    }

    public static QpsResult runCDB(QpsParam p) {
        System.gc();
        System.out.println();
        System.out.println("CDB");
        File f = new File("var/cdb-qps/benchmark.cdb");
        QpsResult r = CdbUtils.runQps(f, p);
        System.out.println(r.toString());
        System.gc();
        return r;
    }

    public static QpsResult runFST(QpsParam p) {
        System.gc();
        System.out.println();
        System.out.println("CDB");
        QpsResult r = FstUtils.runQps(p);
        System.out.println(r.toString());
        System.gc();
        return r;
    }

    public static QpsResult run(QpsParam p, Map<Binary, Binary> m) {
        return run(p, m, new Random());
    }

    public static QpsResult run(QpsParam p, Map<Binary, Binary> m, Random r) {
        List<Binary> keys = Utils.setupMap(m, r, p.numOfItems, p.hitRate,
                p.keyMaxLen, p.valueMaxLen);
        // Warm up.
        for (int i = 0; i < p.warmUp; ++i) {
            run(p, m, r, keys);
        }
        // Query to map.
        return run(p, m, r, keys);
    }

    public static QpsResult run(QpsParam p, Map<Binary, Binary> m, Random r,
            List<Binary> keys) {
        QpsResult res = new QpsResult();
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

    public static QpsParam defaultParam() {
        QpsParam p = new QpsParam();
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
        QpsParam p = defaultParam();
        System.out.println();
        System.out.println("QpsParam: " + p.toString());

        runHashMap(p);
        runBOHMap(p);
        runMapDB(p);
    }

    public static void runMurmur() {
        QpsParam p = defaultParam();
        System.out.println();
        System.out.println("QpsParam: " + p.toString());

        runBOHMap(p);
        runBOHMapMurmurHash3(p);
    }

    public static void runHitrate() {
        QpsParam p = defaultParam();
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

    public static void runCap() {
        QpsParam p = defaultParam();
        double[] rates = new double[]{
            0.25, 0.5, 0.75, 1.0, 1.25, 1.5, 1.75, 2.0
        };

        System.out.println("capacity");

        for (double r : rates) {
            p.bohmapPartitionCount = (int)(p.numOfItems * r);
            runBOHMap(p);
            System.out.println(String.format("%,3d/%,3d (%.2f)",
                        p.bohmapPartitionCount, p.numOfItems, r));
        }
    }

    public static void runSparkey() {
        QpsParam p = defaultParam();
        System.out.println();
        System.out.println("QpsParam: " + p.toString());

        runBOHMap(p);
        runSparkey(p);
    }

    public static void runCDB() {
        QpsParam p = defaultParam();
        System.out.println();
        System.out.println("QpsParam: " + p.toString());

        runBOHMap(p);
        runCDB(p);
    }

    public static void runFST() {
        QpsParam p = defaultParam();
        System.out.println();
        System.out.println("QpsParam: " + p.toString());

        runBOHMap(p);
        runFST(p);
    }
}
