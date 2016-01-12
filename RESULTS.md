# Latest Results

## QPS

**bohmap is about two times slower than HashMap.**

```
:compileJava
:processResources UP-TO-DATE
:classes
:run

QpsParam: net.kaoriya.bohmapbench.QpsParam@2b193f2d[
  warmUp=4
  iteration=1000
  numOfItems=1000000
  hitRate=0.75
  keyMaxLen=128
  valueMaxLen=128
  bohmapPartitionCount=1500000
]

HashMap
net.kaoriya.bohmapbench.QpsResult@3cd1a2f1[
  elapsedNanoTime=555,008
  QPS=1,801,775
  hitRate=0.745
  IPS=705,932
  insertedCount=1000000
]

BOHMap
net.kaoriya.bohmapbench.QpsResult@6d03e736[
  elapsedNanoTime=2,036,882
  QPS=490,946
  hitRate=0.753
  IPS=736,738
  insertedCount=1000000
]

MapDB
net.kaoriya.bohmapbench.QpsResult@4361bd48[
  elapsedNanoTime=2,208,621
  QPS=452,771
  hitRate=0.736
  IPS=143,003
  insertedCount=1000000
]

SmoothieMap
net.kaoriya.bohmapbench.QpsResult@48a242ce[
  elapsedNanoTime=605,915
  QPS=1,650,396
  hitRate=0.742
  IPS=772,012
  insertedCount=1000000
]

BUILD SUCCESSFUL

Total time: 18.888 secs
```

### QPS comparing with Sparkey

```
$ gradle run -Pargs=sparkey
:compileJava UP-TO-DATE
:processResources UP-TO-DATE
:classes UP-TO-DATE
:run

QpsParam: net.kaoriya.bohmapbench.QpsParam@1d44bcfa[
  warmUp=4
  iteration=1000
  numOfItems=1000000
  hitRate=0.75
  keyMaxLen=128
  valueMaxLen=128
  bohmapPartitionCount=1500000
]

BOHMap
net.kaoriya.bohmapbench.QpsResult@214c265e[
  elapasedNanoTime=1,700,411
  QPS=588,093
  hitRate=0.749
]

Sparkey
net.kaoriya.bohmapbench.QpsResult@1a86f2f1[
  elapasedNanoTime=1,986,835
  QPS=503,313
  hitRate=0.725
]

BUILD SUCCESSFUL

Total time: 7.286 secs
```

### QPS comparing with CDB

```
$ gradle run -Pargs=cdb
:compileJava UP-TO-DATE
:processResources UP-TO-DATE
:classes UP-TO-DATE
:run

QpsParam: net.kaoriya.bohmapbench.QpsParam@6f94fa3e[
  warmUp=4
  iteration=1000
  numOfItems=1000000
  hitRate=0.75
  keyMaxLen=128
  valueMaxLen=128
  bohmapPartitionCount=1500000
]

BOHMap
net.kaoriya.bohmapbench.QpsResult@7cca494b[
  elapasedNanoTime=1,415,448
  QPS=706,490
  hitRate=0.718
]

CDB
net.kaoriya.bohmapbench.QpsResult@34a245ab[
  elapasedNanoTime=1,966,648
  QPS=508,479
  hitRate=0.76
]

BUILD SUCCESSFUL

Total time: 12.817 secs
```

### QPS comparing with SmoothieMap

```
:compileJava UP-TO-DATE
:processResources UP-TO-DATE
:classes UP-TO-DATE
:run

QpsParam: net.kaoriya.bohmapbench.QpsParam@2b193f2d[
  warmUp=4
  iteration=1000
  numOfItems=1000000
  hitRate=0.75
  keyMaxLen=128
  valueMaxLen=128
  bohmapPartitionCount=1500000
]

BOHMap
net.kaoriya.bohmapbench.QpsResult@58372a00[
  elapasedNanoTime=1,411,947
  QPS=708,241
  hitRate=0.74
]

SmoothieMap
net.kaoriya.bohmapbench.QpsResult@704a52ec[
  elapasedNanoTime=636,927
  QPS=1,570,038
  hitRate=0.734
]

BUILD SUCCESSFUL

Total time: 7.468 secs
```

## Memory benchmark

```
:compileJava
:processResources UP-TO-DATE
:classes
:run
HashMap:
  MEM:before       3,190,584 (bytes)
   GC:alive1     838,592,781 (nsec)
  MEM:alive1     364,280,504 (bytes)
   GC:alive2     722,059,488 (nsec)
  MEM:alive2     364,280,656 (bytes)
   GC:clear1      13,081,718 (nsec)
  MEM:after1      12,280,880 (bytes)
   GC:clear2       8,216,266 (nsec)
  MEM:after2      12,280,992 (bytes)
SmoothieMap:
  MEM:before       7,107,064 (bytes)
   GC:alive1     616,058,158 (nsec)
  MEM:alive1     348,426,072 (bytes)
   GC:alive2     410,077,485 (nsec)
  MEM:alive2     348,426,072 (bytes)
   GC:clear1      23,350,076 (nsec)
  MEM:after1      28,426,072 (bytes)
   GC:clear2      15,303,209 (nsec)
  MEM:after2      28,426,072 (bytes)
BOHMap:
  MEM:before       6,676,424 (bytes)
   GC:alive1      12,653,688 (nsec)
  MEM:alive1      11,554,024 (bytes)
   GC:alive2      13,196,114 (nsec)
  MEM:alive2      12,597,368 (bytes)
   GC:clear1      14,041,936 (nsec)
  MEM:after1      11,554,160 (bytes)
   GC:clear2      11,760,176 (nsec)
  MEM:after2      11,554,160 (bytes)

BUILD SUCCESSFUL

Total time: 42.962 secs
```

## QPS by Hit rate

Hit rate | BOHMap (QPS) | HashMap (QPS)
---------|-------------:|--------------:
0.25     |1,005,000     |1,710,000
0.50     |  720,000     |1,590,000
0.75     |1,080,000     |1,630,000
1.00     |  970,000     |1,610,000
