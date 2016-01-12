package net.kaoriya.bohmapbench;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

// IPSResult means result of Insert Per Second.
public class IPSResult {
    public int insertCount;
    public long elapsedNanoTime;

    public IPSResult(int insertCount, long elapsedNanoTime) {
        this.insertCount = insertCount;
        this.elapsedNanoTime = elapsedNanoTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("elapsedNanoTime",
                    String.format("%1$,3d", elapsedNanoTime))
            .append("IPS", String.format("%1$,3d", getIPS()))
            .toString();
    }

    public long getIPS() {
        return this.insertCount * (long)1e9 / this.elapsedNanoTime;
    }
}
