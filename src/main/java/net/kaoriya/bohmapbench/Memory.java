package net.kaoriya.bohmapbench;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.cfelde.bohmap.BOHMap;
import com.cfelde.bohmap.Binary;
import net.openhft.smoothie.SmoothieMap;

public class Memory {

    public static long memoryUsage(String label) {
        Runtime r = Runtime.getRuntime();
        long used = r.totalMemory() - r.freeMemory();
        System.out.println(
                String.format("  MEM:%1$-6s %2$,15d (bytes)", label, used));
        return used;
    }

    // gc execute GC and measure its time.
    public static void gc(String label) {
        long st  = System.nanoTime();
        System.gc();
        long et = System.nanoTime();
        System.out.println(
                String.format("   GC:%1$-6s %2$,15d (nsec)", label, et - st));
    }

    public static void run(MapParam mp, Map<Binary, Binary> m, Random r) {
        String name = m.getClass().getSimpleName();
        System.out.println(name + ":");
        System.gc();
        memoryUsage("before");
        Utils.setupMap(m, r, mp);
        gc("alive1");
        memoryUsage("alive1");
        gc("alive2");
        memoryUsage("alive2");
        try { Thread.sleep(10 * 1000); } catch (InterruptedException e) {}
        m.clear();
        gc("clear1");
        memoryUsage("after1");
        gc("clear2");
        memoryUsage("after2");
    }

    public static void run() {
        MapParam mp = new MapParam();
        mp.numOfItems = 1000 * 1000;
        mp.hitRate = 0.75;
        mp.keyMaxLen = 128;
        mp.valueMaxLen = 128;
        double capacity = 1.5;

        run(mp, new HashMap<Binary, Binary>(), new Random());
        run(mp, new SmoothieMap<Binary, Binary>(), new Random());
        run(mp, new BOHMap((int)(mp.numOfItems * capacity)), new Random());
    }
}
