package distributed.hash.table;

/** 
 * Implementation of RMI query request
 */
public class QueryRequest implements IQueryRequest{
	private static final long serialVersionUID = 1L;
	private int RequestId;
	private int MachineId;
	private int Key;
	private String message; 
	public QueryRequest(int requestId, int machineId, int key){
		this.RequestId = requestId;
		this.MachineId = machineId;
		this.Key = key;
		this.message = "";
	}
	public int getKey(){ return this.Key;}
	public int getRequestId(){ return this.RequestId;}
	public int getMachineId(){ return this.MachineId;}
	public void appendMessage(String message){this.message += message;}
	public String getMessage(){return this.message;}
	public String printRequest() {return "request " + this.getRequestId() + 
			" from machine " + this.getMachineId() + 
			" for key " + this.getKey();
	}
}