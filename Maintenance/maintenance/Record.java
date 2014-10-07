package maintenance;

import java.util.Date;

public class Record {
	protected static final String DELIMITER = "|";
	private int id;
	private Date date;
	private int carId;
	private int mileage;
	private boolean active;
	
	public Record(){
		
	}
	
	public Record(String recordString){
		String[] fields = recordString.split("\\|");
		setId(Integer.valueOf(fields[1]));
		setCarId(Integer.valueOf(fields[2]));
		setDate(new Date(Long.valueOf(fields[3])));
		setMileage(Integer.valueOf(fields[4]));
		if(fields[5].equals("y")){
			setActive(true);
		}else{
			setActive(false);
		}
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getCarId() {
		return carId;
	}

	public void setCarId(int carId) {
		this.carId = carId;
	}

	public int getMileage() {
		return mileage;
	}

	public void setMileage(int mileage) {
		this.mileage = mileage;
	}
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();		
		
		sb.append("record");
		sb.append(DELIMITER);
		sb.append(String.valueOf(this.getId()));
		sb.append(DELIMITER);
		sb.append(String.valueOf(this.getCarId()));
		sb.append(DELIMITER);
		sb.append(String.valueOf(this.getDate().getTime()));
		sb.append(DELIMITER);
		sb.append(String.valueOf(this.getMileage()));
		sb.append(DELIMITER);
		if(isActive()){
			sb.append("y");
		}else{
			sb.append("n");
		}
		
		return sb.toString();
	}
}
