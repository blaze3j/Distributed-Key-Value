package distributed.hash.table;

import java.rmi.Naming;
import java.util.*; 

public class DistributedHashTable extends java.rmi.server.UnicastRemoteObject implements IDistributedHashTable{
	private static final int MaxSize = 250000;
	private static final long serialVersionUID = 1L;
	private Hashtable<Integer, Object> cache;
	private Hashtable<Integer, Integer> fTable;
	int myId;
	
	public DistributedHashTable(int id, Hashtable<Integer, Integer> fingerTable) throws java.rmi.RemoteException {
        super(); 
		this.cache = new Hashtable<Integer, Object>();
		this.fTable = fingerTable;
		this.myId = id;
		
		Set<Integer> set = this.fTable.keySet();
		Iterator<Integer> itr = set.iterator();
		System.out.print("DHT id: " + this.myId + " is created.");
		while(itr.hasNext()) {
			int peer = itr.next();
			System.out.print(" peerId: " + peer  + " in port: " + this.fTable.get(peer) + ".");
		}
		System.out.println();
	}

	public void insert(IInsertRequest req){
		int machineId = getMachineId(req.getKey());
		if(machineId == this.myId){
			this.cache.put(req.getKey(), req.getValue());
			System.out.println( "insert: machine " + this.myId + " - " + req.toString() + " is inserted");
		}
		else{
			try { 
				IDistributedHashTable dhtNextMachine = (IDistributedHashTable) 
	    			Naming.lookup("rmi://localhost:"+ (Integer)this.fTable.get(machineId) +"/DistributedHashTable");
				System.out.println( "insert: machine " + this.myId + " - " + req.toString() + " routed to machine " + machineId);
				dhtNextMachine.insert(req);
			}  catch(Exception e) {
				System.out.println("MachineId " + this.myId + " : dhtNextMachine: " +  e.getMessage());
			}
		}
	}
	
	public Object lookup(IQueryRequest req){
		int machineId = getMachineId(req.getKey());
		if(machineId == this.myId){
			if(this.cache.containsKey(req.getKey())){
				Object value = this.cache.get(req.getKey());
				System.out.println( "lookup: machine " + this.myId + " - value of " + req.toString() + " is " + value);
				return value;
			}
			System.out.println( "lookup: machine " + this.myId + " - value of " + req.toString() + " not found.");	
			return null;
		}
		else{
			try { 
				IDistributedHashTable dhtNextMachine = (IDistributedHashTable) 
	    			Naming.lookup("rmi://localhost:"+ (Integer)this.fTable.get(machineId) +"/DistributedHashTable");
				System.out.println( "lookup: machine " + this.myId + " - value of " + req.toString() + " routed to machine " + machineId);
				return dhtNextMachine.lookup(req);
			}catch(Exception e) {
				System.out.println("MachineId " + this.myId + " : dhtNextMachine: " +  e.getMessage());
			}
		}
		return null;
	}
	
	public void delete(IQueryRequest req){
		int machineId = getMachineId(req.getKey());
		if(machineId == this.myId){
			if(this.cache.containsKey(req.getKey())){
				System.out.println( "delete: machine " + this.myId + " - value of " + req.toString() + " is deleted");
				this.cache.remove(req.getKey());
			}
			else
			{
				System.out.println( "delete: machine " + this.myId + " - value of " + req.toString() + " not found");
			}
		}
		else{
			try { 
				IDistributedHashTable dhtNextMachine = (IDistributedHashTable) 
	    			Naming.lookup("rmi://localhost:"+ (Integer)this.fTable.get(machineId) +"/DistributedHashTable");
				System.out.println( "delete: machine " + this.myId + " - value of " + req.toString() + " routed to machine " + machineId);
				dhtNextMachine.delete(req);
			} catch(Exception e) {
				System.out.println("MachineId " + this.myId + " : dhtNextMachine: " +  e.getMessage());
			}
		}
	}
	
	public void purge(){
	    this.cache.clear();
	}
	
	public int count(){
		return this.cache.size();
	}
	
	private int getUpperBound(int machineId){
		return machineId * MaxSize + 1;
	}
	
	private int getLowerBound(int machineId){
		return getUpperBound(machineId) - MaxSize;
	}
	
	private int getMachineId(int key){
		if(getLowerBound(myId) <= key && key <= getUpperBound(myId))
			return myId;
		else if(getLowerBound(getNextMachineId()) <= key && key <= getUpperBound(getNextMachineId()))
			return getNextMachineId();
		return getLastMachineId();
	}
	
	private int getNextMachineId(){
		Set<Integer> set = this.fTable.keySet();
		Iterator<Integer> itr = set.iterator();
		int min = Integer.MAX_VALUE;
		while(itr.hasNext()) { 
			int id = itr.next();
			min = Math.min(id, min);
		}
		return min;
	}
	
	private int getLastMachineId(){
		Set<Integer> set = this.fTable.keySet();
		Iterator<Integer> itr = set.iterator();
		int max = Integer.MIN_VALUE;
		while(itr.hasNext()) { 
			int id = itr.next();
			max = Math.max(id, max);
		}
		return max;
	}
}