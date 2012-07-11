Distributed-Key-Value
=====================

CS425 MP2 Distributed Key Value Project

javac *.java
rmic distributed.hash.table.DistributedHashTable
rmic distributed.hash.table.InsertRequest
rmic distributed.hash.table.QueryRequest

java -Djava.security.policy=server.policy DHTServer -i 1 -f serverSetting4-2.txt 
java -Djava.security.policy=server.policy DHTServer -i 2 -f serverSetting4-2.txt
java -Djava.security.policy=server.policy DHTServer -i 3 -f serverSetting4-2.txt
java -Djava.security.policy=server.policy DHTServer -i 4 -f serverSetting4-2.txt

running client for experiments

java DHTInteractiveClient -f clientSetting4.txt

clientSetting4.txt is the file that contains address of servers as bellow, In this scenario it is port number of processes  
15551,15552,15553,15554


By modifying bin/serverSeting4-2.txt we can add as many server we want. 
Also we can change the number of peer ids for each server and specify different number of keys for each server.
Format of serverSetting4-2.txt, this is an example of 4 servers, each connected to other 2 servers

Note: assumption is that the successors of each server are ordered 
server id,port,start key,size,address port,max Key number,address port,max Key number,...
1,15551,1,250000,15552,500000,15553,750000
2,15552,250001,250000,15553,750000,15554,1000000
3,15553,500001,250000,15554,1000000,15551,250000
4,15554,750001,250000,15551,250000,15552,500000

There are two more scenarios as bellow:
1) serverSetting6-3.txt: 6 servers each one has 3 successors and size of each server is 250000
	clientSetting6.txt is the client setting of this use case.

2) serverSetting8-3.txt: 8 servers each one has 3 successors and size of each server is variable
	clientSetting8.txt is the client setting of this use case