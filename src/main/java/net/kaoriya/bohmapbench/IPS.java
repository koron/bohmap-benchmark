package net.kaoriya.bohmapbench;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.cfelde.bohmap.BOHMap;
import com.cfelde.bohmap.Binary;
import net.openhft.smoothie.SmoothieMap;

public class IPS {

    public static class Param {
        public int valueCount;
        public int valueLen;
        public Param(int count, int len) {
            this.valueCount = count;
            this.valueLen = len;
        }
    }

    public static void run(String label, Param p, Map<Binary, Binary> m, Random r) {
        System.gc();
        // Generate values.
        ArrayList<Binary> values = new ArrayList<>();
        for (int i = 0; i < p.valueCount; ++i) {
            values.add(Utils.randomBinary(r, p.valueLen));
        }
        long startAt = System.nanoTime();
        for (int i = 0; i < p.valueCount; ++i) {
            Binary v = values.get(i);
            m.put(v, v);
        }
        long elapsed = System.nanoTime() - startAt;
        System.gc();
        // Result.
        IPSResult result = new IPSResult(p.valueCount, elapsed);
        System.out.println(label + ":");
        System.out.println(result.toString());
    }

    public static void run() {
        Param p = new Param(1000000, 128);
        run("HashMap", p, new HashMap<Binary, Binary>(), new Random());
        run("SmoothieMap", p, new SmoothieMap<Binary, Binary>(), new Random());
        run("SmoothieMap (initialized)", p,
                new SmoothieMap<Binary, Binary>(p.valueCount), new Random());
        run("BOHMap", p, new BOHMap(p.valueCount), new Random());
    }
}
