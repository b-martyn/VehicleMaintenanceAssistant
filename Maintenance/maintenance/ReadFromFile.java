package maintenance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReadFromFile extends FileSource{
	
	File file;
	
	public ReadFromFile(int customerId){
		String stringId = String.valueOf(customerId);
		this.fileName = stringId + ".txt";
		this.file = getDataFile();
	}
	
	public ArrayList<String> getLinesFromFile(){
		ArrayList<String> lines = getLinesFromFile("all");
		return lines;
	}
	
	public ArrayList<String> getLinesFromFile(String parameter){
		ArrayList<String> lines = new ArrayList<String>();
		
		String line = null;
		try {
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null){
				if(parameter.toLowerCase().equals("all")){
					lines.add(line);
				}else if(line.toLowerCase().startsWith(parameter.toLowerCase())){
					lines.add(line);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return lines;
	}
}
