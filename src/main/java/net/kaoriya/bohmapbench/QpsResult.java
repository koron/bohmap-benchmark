package net.kaoriya.bohmapbench;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class QpsResult {
    public long elapasedNanoTime;
    public int queryCount;
    public int hitCount;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("elapasedNanoTime",
                    String.format("%1$,3d", elapasedNanoTime))
            .append("QPS", String.format("%1$,3d", getQPS()))
            .append("hitRate", getHitRate())
            .toString();
    }

    public double getHitRate() {
        return (double)this.hitCount / this.queryCount;
    }

    public long getQPS() {
        return queryCount * (long)1e9 / this.elapasedNanoTime;
    }
}
