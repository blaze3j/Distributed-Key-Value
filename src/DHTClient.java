import java.rmi.Naming;

import distributed.hash.table.*;

public class DHTClient {
	public static void main(String[] args) {
		try { 
	    	IDistributedHashTable dhtClient = (IDistributedHashTable) 
	    			Naming.lookup("rmi://localhost:15558/DistributedHashTable");
	    	dhtClient.purge();
	    	IInsertRequest insReq = new InsertRequest(1, 1, 1, 1);
	    	dhtClient.insert(insReq);
			System.out.println("DHTClient insert: " + insReq.toString());

	    	IQueryRequest queryReq = new QueryRequest(2, 1, 1);
	    	dhtClient.delete(queryReq);
			System.out.println("DHTClient delete: " + queryReq.toString());

			insReq = new InsertRequest(3, 1, 450000, 1);
	    	dhtClient.insert(insReq);
			System.out.println("DHTClient insert: " + insReq.toString());

			insReq = new InsertRequest(4, 1, 785000, 1);
	    	dhtClient.insert(insReq);
			System.out.println("DHTClient insert: " + insReq.toString());
			
			insReq = new InsertRequest(5, 1, 650000, 1);
	    	dhtClient.insert(insReq);
			System.out.println("DHTClient insert: " + insReq.toString());
			
			queryReq = new QueryRequest(6, 1, 650000);
	    	dhtClient.delete(queryReq);
			System.out.println("DHTClient delete: " + queryReq.toString());

			System.out.println("DHTClient get Count on machine 1 is " + dhtClient.count());
	    }  catch(Exception e) {
			System.out.println("dhtClient: " +  e.getMessage());
	    }
	}
}