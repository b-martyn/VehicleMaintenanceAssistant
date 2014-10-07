package maintenance;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

public class PDFMain {
	private Document document;
	private RecordBook recordBook;
	
	public PDFMain(RecordBook recordBook){
		this.recordBook = recordBook;
		this.document = new Document();
	}
	
	public void createPDF(){
		PDFFileChooser fc = new PDFFileChooser();
		try {
			PdfWriter.getInstance(document, new FileOutputStream(fc.getFilePath()));
			document.open();
			addMetaData(document);
			addHeader(document);
			addRecords(document);
			document.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	private void addMetaData(Document doc){
		doc.addTitle("Maintenance Records");
	}
	
	private void addHeader(Document doc) throws DocumentException{
		Customer customer = recordBook.getCustomer();
		StringBuilder headerMessage = new StringBuilder();
		headerMessage.append(customer.getFirstName());
		headerMessage.append(" ");
		headerMessage.append(customer.getLastName());
		headerMessage.append(" Record History");
		
		Paragraph headerParagraph = new Paragraph();
		PDFHeader header = new PDFHeader(headerMessage.toString());
		headerParagraph.add(header.getHeaderTable());
		addEmptyLine(headerParagraph, 1);
		doc.add(headerParagraph);
	}
	
	private void addRecords(Document doc) throws DocumentException{
		ArrayList<Car> cars = recordBook.getCustomer().getCars();
		for(Car car : cars){
			Paragraph paragraph = new Paragraph(car.getMake() + " " + car.getModel() + " " + car.getYear());
			paragraph.setAlignment(Paragraph.ALIGN_CENTER);
			for(RecordType recordType : RecordType.values()){
				ArrayList<ServiceRecord> typeRecords = recordBook.getServiceRecords(recordType);
				ArrayList<ServiceRecord> records = new ArrayList<ServiceRecord>();
				for(ServiceRecord serviceRecord : typeRecords){
					if(serviceRecord.getCarId() == car.getId()){
						records.add(serviceRecord);
					}
				}
				PDFRecordSection section = new PDFRecordSection(records, recordType);
				paragraph.add(section.getTable());
				paragraph.add(new LineSeparator());
				addEmptyLine(paragraph, 1);
			}
			doc.add(paragraph);
		}
	}
	
	private static void addEmptyLine(Paragraph paragraph, int number){
		for(int i = 0; i < number; i++){
			paragraph.add(new Paragraph(" "));
		}
	}
}
