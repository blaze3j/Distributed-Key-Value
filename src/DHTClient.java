import java.rmi.Naming;

import distributed.hash.table.*;

public class DHTClient {
	public static void main(String[] args) {
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
	    }
	}
}