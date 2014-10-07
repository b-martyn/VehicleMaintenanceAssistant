package maintenance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.draw.LineSeparator;

public class PDFRecordSection {
	private String[] header = {"Date", "Mileage"};
	private ArrayList<ServiceRecord> records;
	private PdfPTable table;
	private RecordType recordType;
	private static final SimpleDateFormat SDF = new SimpleDateFormat("MM/dd/yy");
	
	public PDFRecordSection(ArrayList<ServiceRecord> serviceRecords, RecordType recordType){
		this.records = serviceRecords;
		this.recordType = recordType;
		createSection();
	}
	
	private void createSection(){
		table = new PdfPTable(header.length);
		table.setHeaderRows(2);
		table.addCell(setTopHeader());
		for(String string : header){
			table.addCell(setHeaderCell(string));
		}
		for(ServiceRecord serviceRecord : records){
			table.addCell(setBodyCell(SDF.format(serviceRecord.getDate())));
			table.addCell(setBodyCell(String.valueOf(serviceRecord.getMileage())));
		}
	}
	
	private PdfPCell setTopHeader(){
		PdfPCell newCell = new PdfPCell();
		newCell.setColspan(2);
		newCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		if(recordType.toString().equals("OILCHANGE")){
			newCell.setPhrase(new Phrase("Oil Changes"));
		}
		return newCell;
	}
	
	private PdfPCell setHeaderCell(String string){
		PdfPCell newCell = new PdfPCell(new Phrase(string));
		newCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		return newCell;
	}
	
	private PdfPCell setBodyCell(String string){
		PdfPCell newCell = new PdfPCell(new Phrase(string));
		return newCell;
	}
	
	public PdfPTable getTable(){
		return table;
	}
}
