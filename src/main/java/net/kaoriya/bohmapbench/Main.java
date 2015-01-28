package net.kaoriya.bohmapbench;

public class Main {
    public static void main(String[] args) {
        String mode = args.length >= 1 ? args[0] : null;
        if (mode == null) {
            QPS.run();
        } else if ("qps".equals(mode)) {
            QPS.run();
        } else if ("murmur".equals(mode)) {
            QPS.runMurmur();
        } else if ("hitrate".equals(mode)) {
            QPS.runHitrate();
        } else if ("memory".equals(mode)) {
            Memory.run();
        } else if ("capacity".equals(mode)) {
            QPS.runCap();
        } else if ("hash".equals(mode)) {
            Hash.run();
        } else if ("sparkey".equals(mode)) {
            QPS.runSparkey();
        } else {
            QPS.run();
        }
    }
}
