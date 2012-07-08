package distributed.hash.table;

public interface IInsertRequest extends IQueryRequest{
	public Object getValue()
			throws java.rmi.RemoteException;;
}
