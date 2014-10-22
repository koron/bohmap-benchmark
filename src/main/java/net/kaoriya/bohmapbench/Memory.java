package net.kaoriya.bohmapbench;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.cfelde.bohmap.BOHMap;
import com.cfelde.bohmap.Binary;

public class Memory {
    public static long memoryUsage(String label) {
        Runtime r = Runtime.getRuntime();
        long used = r.totalMemory() - r.freeMemory();
        System.out.println(
                String.format("  %0$-6s %2$,12d", label, used));
        return used;
    }

    public static void run(MapParam mp, Map<Binary, Binary> m, Random r) {
        String name = m.getClass().getSimpleName();
        System.out.println(name + ":");
        System.gc();
        memoryUsage("before");
        Utils.setupMap(m, r, mp);
        System.gc();
        memoryUsage("doing");
        try { Thread.sleep(15 * 1000); } catch (InterruptedException e) {}
        m.clear();
        System.gc();
        memoryUsage("after");
    }

    public static void run() {
        MapParam mp = new MapParam();
        mp.numOfItems = 1000 * 1000;
        mp.hitRate = 0.75;
        mp.keyMaxLen = 128;
        mp.valueMaxLen = 128;
        double capacity = 1.5;

        run(mp, new HashMap<Binary, Binary>(), new Random());
        run(mp, new BOHMap((int)(mp.numOfItems * capacity)), new Random());
    }
}
