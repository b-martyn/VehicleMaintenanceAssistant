package maintenance;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Date;

public class RecordBook implements Writeable{
	private Customer customer;
	private int recordId = 0;
	private ArrayList<ServiceRecord> recordHistory = new ArrayList<ServiceRecord>();
	
	public void addServiceRecord(ServiceRecord serviceRecord){
		serviceRecord.setId(recordId);
		serviceRecord.setDate(new Date());
		recordId++;
		recordHistory.add(serviceRecord);
	}
	
	public void addServiceRecord(ServiceRecord serviceRecord, Date date){
		serviceRecord.setId(recordId);
		serviceRecord.setDate(date);
		recordId++;
		recordHistory.add(serviceRecord);
	}
	
	public ArrayList<ServiceRecord> getAllRecords(){
		return recordHistory;
	}
	
	public ArrayList<ServiceRecord> getServiceRecords(RecordType type){
		ArrayList<ServiceRecord> serviceRecords = new ArrayList<ServiceRecord>();
		ArrayList<ServiceRecord> allRecords = getAllRecords();
		for(Record record : allRecords){
			if(record.getClass().toString().contains("ServiceRecord")){
				ServiceRecord serviceRecord = (ServiceRecord)record;
				if(serviceRecord.getType() == type){
					serviceRecords.add(serviceRecord);
				}
			}
		}
		return serviceRecords;
	}
	
	public void setCustomer(Customer owner){
		customer = owner;
	}
	
	public Customer getCustomer(){
		return customer;
	}

	@Override
	public void write() {
		WriteToFile wtf = new WriteToFile(customer.getId());
		File file = wtf.getDataFile();
		String pathString = file.getAbsolutePath();
		Path path = FileSystems.getDefault().getPath(pathString);
		String string = "";
		byte[] b = string.getBytes();
		
		try {
			Files.write(path, b, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try{
			PrintWriter writer = new PrintWriter(pathString, "UTF-8");
			writer.println(customer.toString());
			//System.out.println(customerString());
			for(Car car : customer.getCars()){
				writer.println(car.toString());
				//System.out.println(carString(car));
			}
			for(RecordType recordType : RecordType.values()){
				for(ServiceRecord serviceRecord : getServiceRecords(recordType)){
					writer.println(serviceRecord.toString());
					//System.out.println(serviceRecordString(serviceRecord));
				}
			}
			writer.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
}
