package distributed.hash.table;

public interface IQueryRequest extends java.io.Serializable{
	public int getKey();
	public int getRequestId();
	public int getMachineId();
	public String toString();
}