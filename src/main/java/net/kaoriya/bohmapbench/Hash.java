package net.kaoriya.bohmapbench;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;

import com.cfelde.bohmap.Binary;
import util.hash.MurmurHash3;

public class Hash {

    public static int KEY_LEN = 128;
    public static int ITERATION = 1000000;

    public static int murmurHash3(byte[] b) {
        return MurmurHash3.murmurhash3_x86_32(b, 0, b.length, 123456789);
    }

    public static void run(
            String label,
            Function<byte[], Integer> hashFunc,
            int iteration,
            int keyLen,
            Random r)
    {
        int countMinus = 0;
        for (int i = 0; i < iteration; ++i) {
            Binary bin = Utils.randomBinary(r, keyLen);
            int v = hashFunc.apply(bin.getValue());
            if (v < 0) {
                ++countMinus;
            }
        }
        System.out.println(String.format("%s : %d/%d (%.2f%%)", label,
                    countMinus, iteration,
                    (double)countMinus * 100 / iteration));
    }

    public static void run() {
        System.out.println(
                "Format: {LABEL} : {MINUS HASH COUNT}/{ITERATION COUNT}");
        run("MurmurHash3", Hash::murmurHash3,
                ITERATION, KEY_LEN, new Random());
        run("Arrays#hashCode", Arrays::hashCode,
                ITERATION, KEY_LEN, new Random());
    }
}
