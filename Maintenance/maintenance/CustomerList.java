package maintenance;

import java.io.File;
import java.util.ArrayList;

public class CustomerList {
	private static int customerId = 0;
	private ArrayList<Customer> customers = new ArrayList<Customer>();
	
	public CustomerList(){
		getCustomersFromFiles();
	}
	
	public void addCustomer(Customer customer){
		customer.setId(customerId);
		customerId++;
		customers.add(customer);
	}
	
	public void addCustomer(Customer customer, int id){
		if(id > customerId){
			customerId = id + 1;
		}
		customers.add(customer);
	}
	
	public ArrayList<Customer> getCustomers(){
		return customers;
	}
	
	private void getCustomersFromFiles(){
		ArrayList<String> customerStrings = new ArrayList<String>();
		File folder = new File(FileSource.DATA_FOLDER);
		File[] listOfFiles = folder.listFiles();
		for(File file : listOfFiles){
			/*Cut .* from File Name*/
			String fullFileName = file.getName();
			int indexOfPeriod = fullFileName.indexOf(".");
			String fileName = fullFileName.substring(0, indexOfPeriod);
			
			ReadFromFile rff = new ReadFromFile(Integer.valueOf(fileName));
			customerStrings.addAll(rff.getLinesFromFile("customer"));
		}
		for(String customerString : customerStrings){
			Customer customer = new Customer(customerString);
			addCustomer(customer, customer.getId());
		}
	}
}
