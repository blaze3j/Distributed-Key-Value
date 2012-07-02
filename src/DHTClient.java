import java.rmi.Naming;

public class DHTClient {
	public static void main(String[] args) {
		try { 
	    	IDistributedHashTable dhtClient = (IDistributedHashTable) 
	    			Naming.lookup("rmi://localhost/DistributedHashTable");
	    	
	    	IRequest req = new Request(1, 1, 1, 1);
	    	dhtClient.insert(req);
			System.out.println("DHTClient insert" + req.toString());

	    	req = new Request(1, 1, 2, 1);
	    	dhtClient.insert(req);
			System.out.println("DHTClient insert" + req.toString());

	    	req = new Request(1, 1, 3, 1);
	    	dhtClient.insert(req);
			System.out.println("DHTClient insert" + req.toString());

	    	System.out.println("DHTClient get Count" + dhtClient.count());
	    }  catch(Exception e) {
			System.out.println("dhtClient: " +  e.getMessage());
	    }
	}
}