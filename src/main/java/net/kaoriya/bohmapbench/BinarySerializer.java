package net.kaoriya.bohmapbench;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

import com.cfelde.bohmap.Binary;
import org.mapdb.Serializer;

public class BinarySerializer implements Serializer<Binary>, Serializable {

    public void serialize(DataOutput out, Binary value) throws IOException {
        byte[] b = value.getValue();
        out.writeInt(b.length);
        out.write(value.getValue());
    }

    public Binary deserialize(DataInput in, int available) throws IOException {
        int len = in.readInt();
        byte[] b = new byte[len];
        in.readFully(b);
        return new Binary(b);
    }

    public int fixedSize() {
        return -1;
    }
}
