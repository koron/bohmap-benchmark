package net.kaoriya.bohmapbench;

public class Main {
    public static void main(String[] args) {
        String mode = args.length >= 1 ? args[0] : null;
        if (mode == null) {
            QPS.run();
        } else if ("murmur".equals(mode)) {
            QPS.runMurmur();
        } else if ("memory".equals(mode)) {
            Memory.run();
        } else {
            QPS.run();
        }
    }
}
