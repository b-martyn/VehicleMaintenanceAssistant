package maintenance;

import java.util.Date;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class PDFHeader {
	private static final String LOGO = "bin/maintenance/files/images/car-logo.png";
	private PdfPTable headerTable;
	private String message;
	
	public PDFHeader(String headerMessage){
		this.message = headerMessage;
		try{
			createHeader();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void createHeader() throws Exception{
		Image logo = Image.getInstance(LOGO);
		PdfPCell c1 = new PdfPCell(logo);
		c1.setFixedHeight(100);
		c1.setBorder(Rectangle.BOTTOM);
		
		Paragraph paragraph = new Paragraph();
		Chunk chunkHeader = new Chunk(message + "\n\n");
		String dateString = new Date().toString();
		StringBuilder sb = new StringBuilder();
		sb.append(dateString.substring(0, 10));
		sb.append(", ");
		sb.append(dateString.substring(24, 28));
		Chunk chunkDate = new Chunk(sb.toString());
		paragraph.add(chunkHeader);
		paragraph.add(chunkDate);
		
		PdfPCell c2 = new PdfPCell(paragraph);
		c2.setColspan(2);
		c2.setVerticalAlignment(Element.ALIGN_MIDDLE);
		c2.setHorizontalAlignment(Element.ALIGN_CENTER);
		c2.setBorder(Rectangle.BOTTOM);
		
		PdfPTable table = new PdfPTable(3);
		table.addCell(c1);
		table.addCell(c2);
		
		headerTable = table;
	}
	
	public PdfPTable getHeaderTable(){
		return headerTable;
	}
}
