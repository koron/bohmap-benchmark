package net.kaoriya.bohmapbench;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.cfelde.bohmap.Binary;
import com.spotify.sparkey.Sparkey;
import com.spotify.sparkey.SparkeyReader;
import com.spotify.sparkey.SparkeyWriter;

public class SparkeyUtils {

    public static QpsResult runQps(File f, QpsParam p) {
        return runQps(f, p, new Random());
    }

    public static QpsResult runQps(File f, QpsParam p, Random r) {
        List<Binary> keys = setupMap(f, p, r);
        if (keys == null) {
            System.out.println("Failed to generate keys for Sparkey");
            return null;
        }
        QpsResult retval = runQpsAll(p, f, r, keys);
        cleanMap(f);
        return retval;
    }

    private static List<Binary> setupMap(File f, QpsParam p, Random r) {
        File dir = f.getParentFile();
        if (dir != null && !dir.exists()) {
            dir.mkdirs();
        }
        List<Binary> keys = null;
        try {
            SparkeyWriter w = Sparkey.createNew(f);
            keys = setup(w, p, r);
            w.writeHash();
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return keys;
    }

    private static List<Binary> setup(SparkeyWriter w, QpsParam p, Random r)
        throws IOException
    {
        // Generate keys.
        ArrayList<Binary> keys = new ArrayList<>();
        int keySize = (int)(p.numOfItems / p.hitRate + 0.5);
        for (int i = 0; i < keySize; ++i) {
            keys.add(Utils.randomBinary(r, p.keyMaxLen));
        }
        // Generate values.
        for (int i = 0; i < p.numOfItems; ++i) {
            w.put(keys.get(i).getValue(),
                Utils.randomBinary(r, p.valueMaxLen).getValue());
        }
        return keys;
    }

    private static QpsResult runQpsAll(
            QpsParam p,
            File f,
            Random rand,
            List<Binary> keys)
    {
        try {
            SparkeyReader r = Sparkey.open(f);
            for (int i = 0; i < p.warmUp; ++i) {
                runQpsOnce(p, r, rand, keys);
            }
            QpsResult qr = runQpsOnce(p, r, rand, keys);
            r.close();
            return qr;
        } catch (IOException e) {
            System.out.println("Failed to check QPS of  Sparkey");
            e.printStackTrace();
            return null;
        }

    }

    private static QpsResult runQpsOnce(
            QpsParam p,
            SparkeyReader r,
            Random rand,
            List<Binary> keys)
        throws IOException
    {
        QpsResult res = new QpsResult();
        int keySize = keys.size();
        long startAt = System.nanoTime();

        // Query to map.
        int query = 0, hit = 0;
        for (int i = 0; i < p.iteration; ++i) {
            ++query;
            byte[] v = r.getAsByteArray(
                    keys.get(rand.nextInt(keySize)).getValue());
            if (v != null) {
                ++hit;
            }
        }

        res.elapasedNanoTime = System.nanoTime() - startAt;
        res.queryCount = query;
        res.hitCount = hit;
        return res;
    }

    private static void cleanMap(File f) {
        // delete data file
        Sparkey.getIndexFile(f).deleteOnExit();
        Sparkey.getLogFile(f).deleteOnExit();
    }
}
