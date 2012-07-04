Distributed-Key-Value
=====================

CS425 MP2 Distributed Key Value Project

javac *.java
rmic distributed.hash.table.DistributedHashTable
java -Djava.security.policy=server.policy DHTServer -i 1 -p 2 -g 3
java -Djava.security.policy=server.policy DHTServer -i 2 -p 3 -g 4
java -Djava.security.policy=server.policy DHTServer -i 3 -p 4 -g 1
java -Djava.security.policy=server.policy DHTServer -i 4 -p 1 -g 2

running client for experiments
java DHTClinet

