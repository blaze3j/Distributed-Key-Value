import java.rmi.Naming;

import Stopwatch.Stopwatch;

import distributed.hash.table.IDistributedHashTable;
import distributed.hash.table.IRequest;
import distributed.hash.table.Request;


public class DHTExperiment1 {
    public static void main(String[] args) {
        Stopwatch timer = new Stopwatch().start();
        try { 
            IDistributedHashTable dhtClient = (IDistributedHashTable) 
            Naming.lookup("rmi://localhost:15555/DistributedHashTable");

            IRequest req = new Request(1, 1, 1, 1);
            dhtClient.insert(req);
            System.out.println("DHTClient insert: " + req.toString());

            req = new Request(1, 1, 1, 1);
            dhtClient.delete(req);
            System.out.println("DHTClient delete: " + req.toString());

            req = new Request(1, 1, 450000, 1);
            dhtClient.insert(req);
            System.out.println("DHTClient insert: " + req.toString());

            req = new Request(1, 1, 785000, 1);
            dhtClient.insert(req);
            System.out.println("DHTClient insert: " + req.toString());

            req = new Request(1, 1, 650000, 1);
            dhtClient.insert(req);
            System.out.println("DHTClient insert: " + req.toString());

            req = new Request(1, 1, 650000, 1);
            dhtClient.delete(req);
            System.out.println("DHTClient delete: " + req.toString());

            System.out.println("DHTClient get Count on machine id 1 is " + dhtClient.count());
        }  catch(Exception e) {
            System.out.println("dhtClient: " +  e.getMessage());

            timer.stop();
            System.out.println(timer.getElapsedTime());
        }
    }
}