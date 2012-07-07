package distributed.hash.table;

public class InsertRequest extends QueryRequest implements IInsertRequest{
	private static final long serialVersionUID = 1L;
	Object Value; 
	public InsertRequest(int requestId, int machineId, int key, Object value){
		super(requestId, machineId, key);
		this.Value = value;
	}
	public Object getValue(){return this.Value;}
	public String toString() {return "Insert request " + this.getRequestId() + 
			" from machine " + this.getMachineId() + 
			" with <" + this.getKey() + "," + this.getValue() + ">";
	}
}
