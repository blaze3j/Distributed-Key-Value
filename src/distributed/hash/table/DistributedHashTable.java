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

	public void insert(IRequest req){
		int machineId = getMachineIdToInsert(req.getKey());
		if(machineId == this.myId){
			this.cache.put(req.getKey(), req.getValue());
			System.out.println( "Machine " + this.myId + " inserted: " + req.toString());
		}
		else{
			try { 
				IDistributedHashTable dhtNextMachine = (IDistributedHashTable) 
	    			Naming.lookup("rmi://localhost:"+ (Integer)this.fTable.get(machineId) +"/DistributedHashTable");
				dhtNextMachine.insert(req);
			}  catch(Exception e) {
				System.out.println("MachineId " + this.myId + " : dhtNextMachine: " +  e.getMessage());
			}
		}
	}
	
	public Object lookup(IRequest req){
		int machineId = getMachineIdToInsert(req.getKey());
		if(machineId == this.myId){
			System.out.println( "Machine " + this.myId + " lookup: " + req.toString());
			if(this.cache.containsKey(req.getKey())){
				return this.cache.get(req.getKey());
			}
			return null;
		}
		else{
			try { 
				IDistributedHashTable dhtNextMachine = (IDistributedHashTable) 
	    			Naming.lookup("rmi://localhost:"+ (Integer)this.fTable.get(machineId) +"/DistributedHashTable");
				return dhtNextMachine.lookup(req);
			}  catch(Exception e) {
				System.out.println("MachineId " + this.myId + " : dhtNextMachine: " +  e.getMessage());
			}
		}
		return null;
	}
	
	public void delete(IRequest req){
		int machineId = getMachineIdToInsert(req.getKey());
		if(machineId == this.myId){
			System.out.println( "Machine " + this.myId + " delete: " + req.toString());
			if(this.cache.containsKey(req.getKey())){
				this.cache.remove(req.getKey());
			}
		}
		else{
			try { 
				IDistributedHashTable dhtNextMachine = (IDistributedHashTable) 
	    			Naming.lookup("rmi://localhost:"+ (Integer)this.fTable.get(machineId) +"/DistributedHashTable");
				dhtNextMachine.delete(req);
			}  catch(Exception e) {
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
	
	public boolean contains(IRequest req){
		return this.cache.containsKey(req.getKey());
	}
	
	private int getUpperBound(int machineId){
		return machineId * MaxSize + 1;
	}
	
	private int getLowerBound(int machineId){
		return getUpperBound(machineId) - MaxSize;
	}
	
	private int getMachineIdToInsert(int key){
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
