package maintenance;

import java.io.File;

public class WriteToFile extends FileSource {
	
	@SuppressWarnings("unused")
	private File file;
	
	public WriteToFile(int customerId){
		String stringId = String.valueOf(customerId);
		this.fileName = stringId + ".txt";
		this.file = getDataFile();
	}
}
