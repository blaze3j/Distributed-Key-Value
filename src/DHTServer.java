import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Hashtable;

import com.sun.org.apache.xalan.internal.xsltc.cmdline.getopt.GetOpt;

import distributed.hash.table.DistributedHashTable;

public class DHTServer {
	
	public static int[] sPortMap = {15555,15556,15557,15558};
	public static void main(String[] args) {
		GetOpt getopt = new GetOpt(args, "i:p:g:");
		int serverId = -1;
		int peerId1 = -1;
		int peerId2 = -1;
		try {
			int c;
			while ((c = getopt.getNextOption()) != -1) {
			    switch(c) {
			    case 'i':
			    	serverId = Integer.parseInt(getopt.getOptionArg());
			        break;
			    case 'p':
			    	peerId1 = Integer.parseInt(getopt.getOptionArg());
			        break;
			    case 'g':
			    	peerId2 = Integer.parseInt(getopt.getOptionArg());
			        break;
			    }
			}
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
            System.out.println("Security manager installed.");
        } else {
            System.out.println("Security manager already exists.");
        }
		int serverPort = sPortMap[serverId-1];
		try { //special exception handler for registry creation
			LocateRegistry.createRegistry(serverPort); 
			System.out.println("java RMI registry created.");
		} catch (RemoteException e) {
		    //do nothing, error means registry already exists
			System.out.println("java RMI registry already exists.");
		}
		Hashtable<Integer, Integer> fingerTable = new Hashtable<Integer, Integer>();
		fingerTable.put(peerId1, sPortMap[(peerId1-1)%4]);
		fingerTable.put(peerId2, sPortMap[(peerId2-1)%4]);
		try{
			DistributedHashTable dhtServer = new DistributedHashTable(serverId, fingerTable);
			Naming.rebind("//localhost:"+serverPort+"/DistributedHashTable", dhtServer);
            System.out.println("Distributed Hash server is running.");
		}catch(Exception e){
			System.out.println("dhtServer: " + e.getMessage());
			e.printStackTrace();
		}      
	}
}
