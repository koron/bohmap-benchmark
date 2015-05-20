package net.kaoriya.bohmapbench;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class FstUtils {

    public static class Benchmark {
        QpsParam param;
        Random random = new Random();

        List<byte[]> keys;
        FstMap map;

        public Benchmark(QpsParam param) {
            this.param = param;
        }

        void setupKeys() throws Exception {
            ArrayList<byte[]> keys = new ArrayList<>();
            int keySize =
                (int)(this.param.numOfItems / this.param.hitRate + 0.5);
            for (int i = 0; i < keySize; ++i) {
                keys.add(Utils.randomBytes(this.random, this.param.keyMaxLen));
            }
            this.keys = keys;
        }

        void setupMap() throws Exception {
            FstMap.Factory f = new FstMap.Factory();
            for (int i = 0; i < this.param.numOfItems; ++i) {
                byte[] k = this.keys.get(i);
                byte[] v = Utils.randomBytes(this.random,
                        this.param.valueMaxLen);
                f.add(k, v);
            }
            long startAt = System.nanoTime();
            this.map = f.create();
            long elapased = System.nanoTime() - startAt;
            System.out.println("FST#create wasted nanotime: " + String.format("%1$,3d", elapased));
        }

        QpsResult runQpsAll() throws Exception {
            for (int i = 0; i < this.param.warmUp; ++i) {
                runQpsOnce();
            }
            QpsResult result = runQpsOnce();
            return result;
        }

        QpsResult runQpsOnce() throws Exception {
            QpsResult res = new QpsResult();
            int keySize = this.keys.size();
            long startAt = System.nanoTime();

            // Query to map.
            int query = 0, hit = 0;
            for (int i = 0; i < this.param.iteration; ++i) {
                ++query;
                byte[] k = this.keys.get(this.random.nextInt(keySize));
                byte[] v = this.map.get(k);
                if (v != null) {
                    ++hit;
                }
            }

            res.elapasedNanoTime = System.nanoTime() - startAt;
            res.queryCount = query;
            res.hitCount = hit;
            return res;
        }

        public QpsResult runQps() throws Exception {
            setupKeys();
            setupMap();
            System.gc();
            QpsResult result = runQpsAll();
            return result;
        }
    }

    public static QpsResult runQps(QpsParam p) throws Exception {
        Benchmark b = new Benchmark(p);
        return b.runQps();
    }
}
