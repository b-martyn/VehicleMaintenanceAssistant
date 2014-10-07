package maintenance;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

@SuppressWarnings("serial")
public class PDFFileChooser extends JPanel {
	private JFileChooser fc;
	private String filePath;
	
	public PDFFileChooser(){
		fc = new JFileChooser();
		fc.addChoosableFileFilter(new FileFilter(){
			@Override
			public boolean accept(File f) {
				if(f.isDirectory()){
					return true;
				}else{
					return f.getName().toLowerCase().endsWith(".pdf");
				}
			}

			@Override
			public String getDescription() {
				return "PDF Documents (*.pdf)";
			}
		});
		int returnVal = fc.showSaveDialog(PDFFileChooser.this);
		if(returnVal == JFileChooser.APPROVE_OPTION){
			String path = fc.getSelectedFile().getPath();
			if(path.endsWith(".pdf")){
				filePath = path;
			}else{
				filePath = path + ".pdf";
			}
		}
	}
	
	public String getFilePath(){
		return filePath;
	}
}
