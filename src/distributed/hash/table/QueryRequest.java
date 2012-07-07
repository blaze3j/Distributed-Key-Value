package distributed.hash.table;

public class QueryRequest implements IQueryRequest{
	private static final long serialVersionUID = 1L;
	private int RequestId;
	private int MachineId;
	private int Key; 
	public QueryRequest(int requestId, int machineId, int key){
		this.RequestId = requestId;
		this.MachineId = machineId;
		this.Key = key;
	}
	public int getKey(){ return this.Key;}
	public int getRequestId(){ return this.RequestId;}
	public int getMachineId(){ return this.MachineId;}
	public String toString() {return "Query request " + this.getRequestId() + 
			" from machine " + this.getMachineId() + 
			" for key " + this.getKey();
	}
}