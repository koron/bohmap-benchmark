package net.kaoriya.bohmapbench;

import util.hash.MurmurHash3;

public class Hash {

    public static int murmurHash3(byte[] b) {
        return MurmurHash3.murmurhash3_x86_32(b, 0, b.length, 123456789);
    }

}
