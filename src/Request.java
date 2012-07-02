public class Request implements IRequest{
	private static final long serialVersionUID = 1L;
	private int RequestId;
	private int MachineId;
	private int Key;
	Object Value; 
	public Request(int requestId, int machineId, int key, Object value){
		this.RequestId = requestId;
		this.MachineId = machineId;
		this.Key = key;
		this.Value = value;
	}
	public int getKey(){ return this.Key;}
	public int getRequestId(){ return this.RequestId;}
	public int getMachineId(){return this.MachineId ;}
	public Object getValue(){return this.Value;}
	public String toString() {return "RequestId: " + this.getRequestId() + 
			" MachineId: " + this.getMachineId() +
			" Key: " + this.getKey() +  
			" Value: " + this.getValue(); }
}
