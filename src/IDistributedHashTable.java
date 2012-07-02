import java.rmi.Remote;

public interface IDistributedHashTable extends Remote {
	
	public int count() 
			throws java.rmi.RemoteException;
	public void insert(IRequest req)
			throws java.rmi.RemoteException;
	public void delete(IRequest req)
			throws java.rmi.RemoteException;
	public Object lookup(IRequest req)
			throws java.rmi.RemoteException;
	public boolean contains(IRequest req)
			throws java.rmi.RemoteException;
}
