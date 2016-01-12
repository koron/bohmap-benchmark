package net.kaoriya.bohmapbench;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class QpsResult {
    public long elapsedNanoTime;
    public int queryCount;
    public int hitCount;
    public IPSResult ipsResult;

    @Override
    public String toString() {
        ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("elapsedNanoTime",
                    String.format("%1$,3d", elapsedNanoTime))
            .append("QPS", String.format("%1$,3d", getQPS()))
            .append("hitRate", getHitRate());
        if (this.ipsResult != null) {
            b.append("IPS", String.format("%1$,3d", this.ipsResult.getIPS()))
                .append("insertedCount", this.ipsResult.insertCount);
        }
        return b.toString();
    }

    public double getHitRate() {
        return (double)this.hitCount / this.queryCount;
    }

    public long getQPS() {
        return queryCount * (long)1e9 / this.elapsedNanoTime;
    }
}
