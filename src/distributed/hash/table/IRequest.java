package distributed.hash.table;

public interface IRequest extends java.io.Serializable{
	public int getKey();
	public int getRequestId();
	public int getMachineId();
	public Object getValue();
	public String toString();
}
