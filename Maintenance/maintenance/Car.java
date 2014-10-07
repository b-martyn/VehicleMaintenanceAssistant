package maintenance;

public class Car {
	private static final String DELIMITER = "|";
	private int id;
	private int customerId;
	private String make;
	private String model;
	private short year;
	private short oilChangeInterval;
	private String oilType;
	private String oilFilter;
	private boolean active;
	
	public Car(){
		
	}
	
	public Car(String carString){
		String[] fields = carString.split("\\|");
		setId(Integer.valueOf(fields[1]));
		setCustomerId(Integer.valueOf(fields[2]));
		setMake(fields[3]);
		setModel(fields[4]);
		setYear(Short.valueOf(fields[5]));
		setOilFilter(fields[6]);
		setOilType(fields[7]);
		setOilChangeInterval(Short.valueOf(fields[8]));
		if(fields[9].equals("y")){
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

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public short getYear() {
		return year;
	}

	public void setYear(short year) {
		this.year = year;
	}

	public short getOilChangeInterval() {
		return oilChangeInterval;
	}

	public void setOilChangeInterval(short oilChangeInterval) {
		this.oilChangeInterval = oilChangeInterval;
	}

	public String getOilType() {
		return oilType;
	}

	public void setOilType(String oilType) {
		this.oilType = oilType;
	}

	public String getOilFilter() {
		return oilFilter;
	}

	public void setOilFilter(String oilFilter) {
		this.oilFilter = oilFilter;
	}
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		sb.append("car");
		sb.append(DELIMITER);
		sb.append(String.valueOf(this.getId()));
		sb.append(DELIMITER);
		sb.append(String.valueOf(this.getCustomerId()));
		sb.append(DELIMITER);
		sb.append(this.getMake());
		sb.append(DELIMITER);
		sb.append(this.getModel());
		sb.append(DELIMITER);
		sb.append(String.valueOf(this.getYear()));
		sb.append(DELIMITER);
		sb.append(this.getOilFilter());
		sb.append(DELIMITER);
		sb.append(this.getOilType());
		sb.append(DELIMITER);
		sb.append(String.valueOf(this.getOilChangeInterval()));
		sb.append(DELIMITER);
		if(active){
			sb.append("y");
		}else{
			sb.append("n");
		}
		
		return sb.toString();
	}
}
