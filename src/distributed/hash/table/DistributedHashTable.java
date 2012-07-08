package distributed.hash.table;

import java.rmi.Naming;
import java.rmi.RemoteException;
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

    public void insert(IInsertRequest req) throws RemoteException{
        int machineId = getMachineId(req.getKey());
        if(machineId == this.myId){
            synchronized(this.cache) {
                handelMessage(req, "insert: machine " + this.myId + " - " + req.printRequest() + " is inserted");
                this.cache.put(req.getKey(), req.getValue());
            }
        }
        else{
            try { 
                IDistributedHashTable dhtNextMachine = (IDistributedHashTable) 
                Naming.lookup("rmi://localhost:"+ (Integer)this.fTable.get(machineId) +"/DistributedHashTable");
                handelMessage(req, "insert: machine " + this.myId + " - " + req.printRequest() + " routed to machine " + machineId + "\n");
                dhtNextMachine.insert(req);
            }  catch(Exception e) {
                handelMessage(req, "insert: machine " + this.myId + " - dhtNextMachine: " +  e.getMessage());
            }
        }
    }

    public Object lookup(IQueryRequest req) throws RemoteException{
        int machineId = getMachineId(req.getKey());
        if(machineId == this.myId){
            synchronized(this.cache) {
                if(this.cache.containsKey(req.getKey())){
                    Object value = this.cache.get(req.getKey());
                    handelMessage(req, "lookup: machine " + this.myId + " - value of " + req.printRequest() + " is " + value);
                    return value;
                }
                else{
                    handelMessage(req, "lookup: machine " + this.myId + " - value of " + req.printRequest() + " not found.");				
                    return null;
                }			
            }
        }
        else{
            try {
                IDistributedHashTable dhtNextMachine = (IDistributedHashTable) 
                Naming.lookup("rmi://localhost:"+ (Integer)this.fTable.get(machineId) +"/DistributedHashTable");
                handelMessage(req, "lookup: machine " + this.myId + " - value of " + req.printRequest() + " routed to machine " + machineId + "\n");
                return dhtNextMachine.lookup(req);
            }catch(Exception e) {
                handelMessage(req, "lookup: machine " + this.myId + " - dhtNextMachine: " +  e.getMessage());
            }
        }
        return null;
    }

    public int lookupTrace(IQueryRequest req) throws RemoteException{
        int machineId = getMachineId(req.getKey());
        if(machineId == this.myId){
            synchronized(this.cache) {
                if(this.cache.containsKey(req.getKey())){
                    handelMessage(req, "lookup trace: machine " + this.myId + " - value of " + req.printRequest() + " is found");
                    return 1;
                }
                else{
                    handelMessage(req, "lookup trace: machine " + this.myId + " - value of " + req.printRequest() + " not found.");               
                    return 0;
                }           
            }
        }
        else{
            try {
                IDistributedHashTable dhtNextMachine = (IDistributedHashTable) 
                Naming.lookup("rmi://localhost:"+ (Integer)this.fTable.get(machineId) +"/DistributedHashTable");
                handelMessage(req, "lookup trace: machine " + this.myId + " - value of " + req.printRequest() + " routed to machine " + machineId + "\n");
                return 1 + dhtNextMachine.lookupTrace(req);
            }catch(Exception e) {
                handelMessage(req, "lookup trace: machine " + this.myId + " - dhtNextMachine: " +  e.getMessage());
            }
        }
        return 0;
    }

    public void delete(IQueryRequest req) throws RemoteException{
        int machineId = getMachineId(req.getKey());
        if(machineId == this.myId){
            synchronized(this.cache) {
                if(this.cache.containsKey(req.getKey())){
                    handelMessage(req, "delete: machine " + this.myId + " - value of " + req.printRequest() + " is deleted");					
                    this.cache.remove(req.getKey());
                }
                else{
                    handelMessage(req, "delete: machine " + this.myId + " - value of " + req.printRequest() + " not found");
                }
            }
        }
        else{
            try { 
                IDistributedHashTable dhtNextMachine = (IDistributedHashTable) 
                Naming.lookup("rmi://localhost:"+ (Integer)this.fTable.get(machineId) +"/DistributedHashTable");
                handelMessage(req, "delete: machine " + this.myId + " - value of " + req.printRequest() + " routed to machine " + machineId + "\n");
                dhtNextMachine.delete(req);
            } catch(Exception e) {
                handelMessage(req, "delete: machine " + this.myId + " - dhtNextMachine: " +  e.getMessage());
            }
        }
    }

    public void purge(){
        synchronized(this.cache) {
            System.out.println("purge: machine " + this.myId + "\n");
            this.cache.clear();
        }
    }

    public int count(){
        synchronized(this.cache) {
            int n = this.cache.size();
            System.out.println("count: machine" + this.myId + " is " + n +"\n");
            return n;
        }
    }

    private void handelMessage(IQueryRequest req, String msg){
        try{
            System.out.println(msg);
            req.appendMessage(msg);
        }catch(Exception e){

        }

    }

    private int getUpperBound(int machineId){
        return machineId * MaxSize + 1;
    }

    private int getLowerBound(int machineId){
        return getUpperBound(machineId) - MaxSize;
    }

    private int getMachineId(int key){
        if(getLowerBound(myId) <= key && key < getUpperBound(myId))
            return myId;
        else if(getLowerBound(getNextMachineId()) <= key && key < getUpperBound(getNextMachineId()))
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