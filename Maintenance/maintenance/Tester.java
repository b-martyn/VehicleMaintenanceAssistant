package maintenance;

import java.util.ArrayList;
import java.util.Date;

import javax.swing.JFrame;

public class Tester {

	public static void main(String[] args) {
		
		/*CustomerList cList = new CustomerList();
		ArrayList<Customer> customers = cList.getCustomers();
		
		RecordBook rBook = customers.get(0).createCustomerData();
		PDFMain fc = new PDFMain(rBook);
		
		/*CustomerList customers = new CustomerList();
		customers.getCustomersFromFiles();
		ArrayList<Customer> customerList = customers.getCustomers();
		for(Customer customer : customerList){
			System.out.println(customer.getFirstName());
		}
		
		Customer customer1 = customerList.get(0);
		RecordBook recordBook = customer1.createCustomerData();
		ArrayList<Record> recordList = recordBook.getAllRecords();
		for(Record record : recordList){
			System.out.println(record.getMileage());
		}*/
		
		
		
		/*ReadFromFile rff = new ReadFromFile(customer1.getId());
		ArrayList<String> cars = rff.getLinesFromFile("car");
		for(String car : cars){
			System.out.println(car);
		}*/
		
		//RecordBook rBook = setNewRecordBook();
		//rBook.write();
		
		
		/*Write new file
		String filesFolder = "Maintenance/maintenance/files/";
		String testFile = "test2.txt";
		
		File file = new File(filesFolder + testFile);
		String pathString = file.getAbsolutePath();
		
		Path path = FileSystems.getDefault().getPath(pathString);
		//System.out.println(path);
		String string = "";
		byte[] b = string.getBytes();
		
		try {
			Files.write(path, b, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			PrintWriter writer = new PrintWriter(pathString, "UTF-8");
			writer.println("line 1");
			writer.println("line 2");
			writer.println("line 3");
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}*/
		
		//String path = Tester.class.getClassLoader().getResource(filesFolder + testFile).getPath();
		
		
		
		/*Write to file
		String filesFolder = "Maintenance/maintenance/files/";
		String testFile = "test.txt";
		
		File file = new File(filesFolder + testFile);
		
		try {
			FileWriter fw = new FileWriter(file.getAbsolutePath());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("line 1");
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		/*Read File
		String filesFolder = "Maintenance/maintenance/files/";
		String testFile = "test.txt";
		String path = Tester.class.getClassLoader().getResource(filesFolder + testFile).getPath();
				
		String line = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null){
				System.out.println(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(file.getTotalSpace());*/
	}

	public static RecordBook setNewRecordBook(){
		RecordBook recordBook = new RecordBook();
		CustomerList cList = new CustomerList();
		
		Customer brian = setCustomer();
		
		cList.addCustomer(brian);
		Car brianCar = setBrianCar();
		brianCar.setCustomerId(brian.getId());
		Car samCar = setSamCar();
		samCar.setCustomerId(brian.getId());
		
		recordBook.setCustomer(brian);
		
		brian.addCar(brianCar);
		brian.addCar(samCar);
		
		ServiceRecord serviceRecord1 = new ServiceRecord();
		serviceRecord1.setCarId(brianCar.getId());
		serviceRecord1.setType(RecordType.OILCHANGE);
		serviceRecord1.setMileage(150000);
		serviceRecord1.addExpenses(25.5f);
		recordBook.addServiceRecord(serviceRecord1);
		serviceRecord1.setDate(new Date());
		
		ServiceRecord serviceRecord2 = new ServiceRecord();
		serviceRecord2.setCarId(samCar.getId());
		serviceRecord2.setType(RecordType.OILCHANGE);
		serviceRecord2.setMileage(75000);
		serviceRecord2.addExpenses(10.0f);
		serviceRecord2.addExpenses(17.25f);
		recordBook.addServiceRecord(serviceRecord2);
		serviceRecord2.setDate(new Date(System.currentTimeMillis() - 3600 * 28000));
		
		ServiceRecord serviceRecord3 = new ServiceRecord();
		serviceRecord3.setCarId(brianCar.getId());
		serviceRecord3.setType(RecordType.OILCHANGE);
		serviceRecord3.setMileage(153000);
		serviceRecord3.addExpenses(17.25f);
		recordBook.addServiceRecord(serviceRecord3);
		serviceRecord3.setDate(new Date(System.currentTimeMillis() - 3600 * 56000));
		
		return recordBook;
	}
	
	public static Car setBrianCar(){
		Car car = new Car();
		car.setMake("Jeep");
		car.setModel("Wrangler");
		car.setYear((short)2002);
		car.setOilChangeInterval((short)3000);
		car.setOilFilter("Bosch 3402");
		car.setOilType("10W-30");
		return car;
	}
	
	public static Car setSamCar(){
		Car car = new Car();
		car.setMake("Toyota");
		car.setModel("Rav4");
		car.setYear((short)2010);
		car.setOilChangeInterval((short)5000);
		car.setOilFilter("Bosch 3972");
		car.setOilType("5W-20");
		return car;
	}
	
	public static Customer setCustomer(){
		Customer newCustomer = new Customer();
		newCustomer.setEmail("brianmartyn84@gmail.com");
		newCustomer.setFirstName("Brian");
		newCustomer.setLastName("Martyn");
		//newCustomer.setId(0);
		return newCustomer;
	}
	
	/*public static RecordBook setNewRecordBook(){
		RecordBook recordBook = new RecordBook();
		//CustomerList cList = new CustomerList();
		
		Customer brian = setCustomer();
		
		//cList.addCustomer(brian);
		Car samCar = setSamCar();
		samCar.setCustomerId(brian.getId());
		
		recordBook.setCustomer(brian);
		
		brian.addCar(samCar);
		
		ServiceRecord serviceRecord1 = new ServiceRecord();
		serviceRecord1.setCarId(samCar.getId());
		serviceRecord1.setType(RecordType.OILCHANGE);
		serviceRecord1.setMileage(1500);
		serviceRecord1.addExpenses(25.5f);
		recordBook.addRecord(serviceRecord1);
		serviceRecord1.setDate(new Date());
		
		ServiceRecord serviceRecord2 = new ServiceRecord();
		serviceRecord2.setCarId(samCar.getId());
		serviceRecord2.setType(RecordType.OILCHANGE);
		serviceRecord2.setMileage(2500);
		serviceRecord2.addExpenses(10.0f);
		serviceRecord2.addExpenses(17.25f);
		recordBook.addRecord(serviceRecord2);
		serviceRecord2.setDate(new Date(System.currentTimeMillis() - 3600 * 28000));
		
		ServiceRecord serviceRecord3 = new ServiceRecord();
		serviceRecord3.setCarId(samCar.getId());
		serviceRecord3.setType(RecordType.OILCHANGE);
		serviceRecord3.setMileage(3200);
		serviceRecord3.addExpenses(17.25f);
		recordBook.addRecord(serviceRecord3);
		serviceRecord3.setDate(new Date(System.currentTimeMillis() - 3600 * 56000));
		
		return recordBook;
	}
	
	public static Car setSamCar(){
		Car car = new Car();
		car.setMake("Chevrolet");
		car.setModel("Corvette");
		car.setYear((short)2015);
		car.setOilChangeInterval((short)7000);
		car.setOilFilter("1234");
		car.setOilType("5W-20");
		return car;
	}
	
	public static Customer setCustomer(){
		Customer newCustomer = new Customer();
		newCustomer.setEmail("bigZ@gmail.com");
		newCustomer.setFirstName("Zdeno");
		newCustomer.setLastName("Chara");
		newCustomer.setId(1);
		return newCustomer;
	}*/
	
}