import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class DHTServer {
	public static void main(String[] args) {
		if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
            System.out.println("Security manager installed.");
        } else {
            System.out.println("Security manager already exists.");
        }
		
		 try { //special exception handler for registry creation
	            LocateRegistry.createRegistry(1099); 
	            System.out.println("java RMI registry created.");
	        } catch (RemoteException e) {
	            //do nothing, error means registry already exists
	            System.out.println("java RMI registry already exists.");
	        }
		
		try{
			DistributedHashTable dhtServer = new DistributedHashTable();
			Naming.rebind("//localhost/DistributedHashTable", dhtServer);
            System.out.println("Distributed Hash server is running.");
		}catch(Exception e){
			System.out.println("dhtServer: " + e.getMessage());
			e.printStackTrace();
		}      
	}
}
