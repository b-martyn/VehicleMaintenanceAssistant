package maintenance;

public class ServiceRecord extends Record{
	private float expenses;
	private RecordType type;
	
	public ServiceRecord(){
		
	}
	
	public ServiceRecord(String serviceRecordString){
		super(serviceRecordString);
		String[] fields = serviceRecordString.split("\\|");
		addExpenses(Float.valueOf(fields[6]));
		setType(RecordType.valueOf(fields[7]));
	}
	
	public void addExpenses(float cost){
		expenses += cost;
	}
	
	public float getExpenses(){
		return expenses;
	}
	
	public void setType(RecordType recordType){
		type = recordType;
	}
	
	public RecordType getType(){
		return type;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder(super.toString());
		
		sb.replace(0, 6, "serviceRecord");
		sb.append(DELIMITER);
		sb.append(String.valueOf(this.getExpenses()));
		sb.append(DELIMITER);
		sb.append(this.getType().toString());
		
		return sb.toString();
	}
}
