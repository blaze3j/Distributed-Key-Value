import java.util.Hashtable;

public class DistributedHashTable extends java.rmi.server.UnicastRemoteObject implements IDistributedHashTable{
	private static final long serialVersionUID = 1L;
	private Hashtable<Integer, Object> cache;
	
	public DistributedHashTable() throws java.rmi.RemoteException {
        super(); 
		this.cache = new Hashtable<Integer, Object>();	}

	public void insert(IRequest req){
		this.cache.put(req.getKey(), req.getValue());
		System.out.println(req.toString() + " inserted");
	}
	
	public int count(){
		return this.cache.size();
	}
	
	public boolean contains(IRequest req){
		return this.cache.containsKey(req.getKey());
	}
	
	public Object lookup(IRequest req){
		if(this.cache.containsKey(req.getKey())){
			return this.cache.get(req.getKey());
		}
		return null;
	}
	
	public void delete(IRequest req){
		if(this.cache.containsKey(req.getKey()))	{
			this.cache.remove(req.getKey());
			System.out.println(req.toString() + " deleted");
		}
	}
}
