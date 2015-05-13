package net.kaoriya.bohmapbench;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.IntsRef;
import org.apache.lucene.util.IntsRefBuilder;
import org.apache.lucene.util.fst.Builder;
import org.apache.lucene.util.fst.ByteSequenceOutputs;
import org.apache.lucene.util.fst.FST;
import org.apache.lucene.util.fst.Util;

public class FstMap {

    public static class Factory {
        TreeMap<BytesRef, BytesRef> map = new TreeMap<>();

        public Factory() {
        }

        public void add(byte[] key, byte[] value) {
            this.map.put(new BytesRef(key), new BytesRef(value));
        }

        public FstMap create() throws IOException {
            Builder<BytesRef> b = new Builder<>(
                    FST.INPUT_TYPE.BYTE1,
                    ByteSequenceOutputs.getSingleton());
            for (Map.Entry<BytesRef, BytesRef> e : this.map.entrySet()) {
                b.add(Util.toIntsRef(e.getKey(), new IntsRefBuilder()),
                        e.getValue());
            }
            return new FstMap(b.finish());
        }
    }

    FST<BytesRef> fst;

    private FstMap(FST<BytesRef> fst) {
        this.fst = fst;
    }

    public byte[] get(byte[] key) throws IOException {
        BytesRef v = Util.get(this.fst,
                Util.toIntsRef(new BytesRef(key), new IntsRefBuilder()));
        return v != null ? v.bytes : null;
    }
}
