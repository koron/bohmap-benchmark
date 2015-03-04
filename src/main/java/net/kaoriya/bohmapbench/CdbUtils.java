package net.kaoriya.bohmapbench;

import java.nio.ByteBuffer;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import ca.hullabaloo.cdb.Cdb;
import ca.hullabaloo.cdb.CdbMap;

// NOTE: Copyied most from SparkeyUtils.java.  Need refactoring.

public class CdbUtils {

    public static QpsResult runQps(File f, QpsParam p) {
        return runQps(f, p, new Random());
    }

    public static QpsResult runQps(File f, QpsParam p, Random r) {
        List<ByteBuffer> keys = setupMap(f, p, r);
        if (keys == null) {
            System.out.println("Failed to generate keys for CDB");
            return null;
        }
        QpsResult retval = runQpsAll(p, f, r, keys);
        cleanMap(f);
        return retval;
    }

    private static List<ByteBuffer> setupMap(File f, QpsParam p, Random r) {
        File dir = f.getParentFile();
        if (dir != null && !dir.exists()) {
            dir.mkdirs();
        }

        List<ByteBuffer> keys = null;
        try {
            Cdb.Builder b = Cdb.builder(f);
            keys = setup(b, p, r);
            b.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return keys;
    }

    private static QpsResult runQpsAll(
            QpsParam p,
            File f,
            Random r,
            List<ByteBuffer> keys)
    {
        try {
            Map<ByteBuffer, ByteBuffer> m = Cdb.open(f);
            for (int i = 0; i < p.warmUp; ++i) {
                runQpsOnce(p, m, r, keys);
            }
            QpsResult qr = runQpsOnce(p, m, r, keys);
            ((CdbMap)m).close();
            return qr;
        } catch (IOException e) {
            System.out.println("Failed to check QPS of  Sparkey");
            e.printStackTrace();
            return null;
        }

    }

    private static void cleanMap(File f) {
        // delete data file
        f.deleteOnExit();
    }

    private static List<ByteBuffer> setup(Cdb.Builder b, QpsParam p, Random r)
        throws IOException
    {
        // Generate keys.
        ArrayList<ByteBuffer> keys = new ArrayList<>();
        int keySize = (int)(p.numOfItems / p.hitRate + 0.5);
        for (int i = 0; i < keySize; ++i) {
            keys.add(Utils.randomByteBuffer(r, p.keyMaxLen));
        }
        // Generate values.
        for (int i = 0; i < p.numOfItems; ++i) {
            b.put(keys.get(i), Utils.randomByteBuffer(r, p.valueMaxLen));
        }
        return keys;
    }

    private static QpsResult runQpsOnce(
            QpsParam p,
            Map<ByteBuffer, ByteBuffer> m,
            Random r,
            List<ByteBuffer> keys)
        throws IOException
    {
        QpsResult res = new QpsResult();
        int keySize = keys.size();
        long startAt = System.nanoTime();

        // Query to map.
        int query = 0, hit = 0;
        for (int i = 0; i < p.iteration; ++i) {
            ++query;
            ByteBuffer k = keys.get(r.nextInt(keySize));
            k.rewind();
            ByteBuffer v = m.get(k);
            if (v != null) {
                ++hit;
            }
        }

        res.elapasedNanoTime = System.nanoTime() - startAt;
        res.queryCount = query;
        res.hitCount = hit;
        return res;
    }
}
