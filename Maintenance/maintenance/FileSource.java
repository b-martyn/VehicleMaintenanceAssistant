package maintenance;

import java.io.File;

public class FileSource {
	public static final String DATA_FOLDER = "Maintenance/maintenance/files/data/";
	protected String fileName;
		
	protected File getDataFile(){
		File file = new File(DATA_FOLDER + fileName);
		return file;
	}
}