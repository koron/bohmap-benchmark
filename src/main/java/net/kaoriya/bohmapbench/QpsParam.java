package net.kaoriya.bohmapbench;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class QpsParam {
    public int warmUp;
    public int iteration;
    public int numOfItems;
    public double hitRate;
    public int keyMaxLen;
    public int valueMaxLen;

    public int bohmapPartitionCount;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this,
                ToStringStyle.MULTI_LINE_STYLE);
    }
}
