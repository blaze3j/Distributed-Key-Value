Distributed-Key-Value
=====================

CS425 MP2 Distributed Key Value Project

javac *.java
rmic distributed.hash.table.DistributedHashTable
rmic distributed.hash.table.InsertRequest
rmic distributed.hash.table.QueryRequest

java -Djava.security.policy=server.policy DHTServer -i 1
java -Djava.security.policy=server.policy DHTServer -i 2
java -Djava.security.policy=server.policy DHTServer -i 3
java -Djava.security.policy=server.policy DHTServer -i 4

running client for experiments
java DHTInteractiveClient

By modifying bin/serverSeting.txt we can add as many server we want. 
Also we can change the number of peer ids for each server.
Format of serverSetting.txt
line 1: port numbers
line2: serverid,peer1,peer2,...
line3: serverid,peer1,peer2,...
line4: serverid,peer1,peer2,...
.....

