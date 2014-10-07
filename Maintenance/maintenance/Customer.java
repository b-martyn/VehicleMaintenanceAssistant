package maintenance;

import java.util.ArrayList;

public class Customer {
	private static final String DELIMITER = "|";
	private int carIdSetter = 0;
	private int id;
	private ArrayList<Car> cars = new ArrayList<Car>();
	private String firstName;
	private String lastName;
	private String email;
	private boolean active;
	
	public Customer(){
		setActive(true);
	}
	
	public Customer(String customerString){
		String[] fields = customerString.split("\\|");
		this.setId(Integer.valueOf(fields[1]));
		this.setFirstName(fields[2]);
		this.setLastName(fields[3]);
		this.setEmail(fields[4]);
		if(fields[5].equals("y")){
			this.setActive(true);
		}else{
			this.setActive(false);
		}
	}
	
	public void addCar(Car car){
		car.setId(carIdSetter);
		carIdSetter++;
		car.setCustomerId(id);
		cars.add(car);
	}
	
	public ArrayList<Car> getCars(){
		return cars;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		sb.append("customer");
		sb.append(DELIMITER);
		sb.append(String.valueOf(this.getId()));
		sb.append(DELIMITER);
		sb.append(this.getFirstName());
		sb.append(DELIMITER);
		sb.append(this.getLastName());
		sb.append(DELIMITER);
		sb.append(this.getEmail());
		sb.append(DELIMITER);
		if(isActive()){
			sb.append("y");
		}else{
			sb.append("n");
		}
		
		return sb.toString();
	}
	
	public RecordBook createCustomerData(){
		setCarsFromFile();
		RecordBook recordBook = setRecordBook();
		return recordBook;
	}
	
	private RecordBook setRecordBook(){
		ReadFromFile rff = new ReadFromFile(id);
		RecordBook recordBook = new RecordBook();
		recordBook.setCustomer(this);
		ArrayList<String> serviceRecords = rff.getLinesFromFile("serviceRecord");
		for(String serviceRecordString : serviceRecords){
			ServiceRecord serviceRecord = new ServiceRecord(serviceRecordString);
			if(serviceRecord != null){
				recordBook.addServiceRecord(serviceRecord, serviceRecord.getDate());
			}
		}
		return recordBook;
	}
	
	private void setCarsFromFile(){
		ReadFromFile rff = new ReadFromFile(id);
		ArrayList<String> cars = rff.getLinesFromFile("car");
		for(String carString : cars){
			Car car = new Car(carString);
			addCar(car);
		}
	}
}
