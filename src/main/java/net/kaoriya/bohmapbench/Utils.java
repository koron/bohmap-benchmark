package net.kaoriya.bohmapbench;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.nio.ByteBuffer;

import com.cfelde.bohmap.Binary;

public class Utils
{
    /**
     * Setup random keys and values to map.
     *
     * @return keys generated.
     */
    public static List<Binary> setupMap(
            Map<Binary, Binary> m,
            Random r,
            int num,
            double hitRate,
            int keyLen,
            int valueLen)
    {
        // Generate keys.
        ArrayList<Binary> keys = new ArrayList<>();
        int keySize = (int)(num / hitRate + 0.5);
        for (int i = 0; i < keySize; ++i) {
            keys.add(randomBinary(r, keyLen));
        }
        // Generate values.
        for (int i = 0; i < num; ++i) {
            m.put(keys.get(i), randomBinary(r, valueLen));
        }
        return keys;
    }

    public static List<Binary> setupMap(
            Map<Binary, Binary> m,
            Random r,
            MapParam p)
    {
        return setupMap(m, r, p.numOfItems, p.hitRate, p.keyMaxLen,
                p.valueMaxLen);
    }

    public static Binary randomBinary(Random r, int len) {
        return new Binary(randomBytes(r, len));
    }

    public static ByteBuffer randomByteBuffer(Random r, int len) {
        return ByteBuffer.wrap(randomBytes(r, len));
    }

    public static byte[] randomBytes(Random r, int len) {
        byte[] b = new byte[len];
        r.nextBytes(b);
        return b;
    }
}
