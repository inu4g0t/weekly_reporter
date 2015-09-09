package docs.tool;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;

public class XWPFTool {
	public static void cloneParagraph(XWPFParagraph clone, XWPFParagraph source) {
	    CTPPr pPr = clone.getCTP().isSetPPr() ? clone.getCTP().getPPr() : clone.getCTP().addNewPPr();
	    pPr.set(source.getCTP().getPPr());
	    for (XWPFRun r : source.getRuns()) {
	        XWPFRun nr = clone.createRun();
	        cloneRun(nr, r);
	    }
	}

	public static void cloneRun(XWPFRun clone, XWPFRun source) {
	    CTRPr rPr = clone.getCTR().isSetRPr() ? clone.getCTR().getRPr() : clone.getCTR().addNewRPr();
	    rPr.set(source.getCTR().getRPr());
	    clone.setText(source.getText(0));
	}
	
	public static void cloneParagraphAndChangeText(XWPFParagraph clone, XWPFParagraph source, String newText) {
	    CTPPr pPr = clone.getCTP().isSetPPr() ? clone.getCTP().getPPr() : clone.getCTP().addNewPPr();
	    pPr.set(source.getCTP().getPPr());
	    for (XWPFRun r : source.getRuns()) {
	        XWPFRun nr = clone.createRun();
	        cloneRunAndChangeText(nr, r, newText);
	        break;
	    }
	}

	public static void cloneRunAndChangeText(XWPFRun clone, XWPFRun source, String newText) {
	    CTRPr rPr = clone.getCTR().isSetRPr() ? clone.getCTR().getRPr() : clone.getCTR().addNewRPr();
	    rPr.set(source.getCTR().getRPr());
	    clone.setText(newText);
	}
	
	public static void addHtmlToDoc(XWPFDocument doc, String htmlString){
		ByteArrayInputStream is = new ByteArrayInputStream(htmlString.getBytes());
		try {
			XWPFDocument tmpDoc = new XWPFDocument(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File outputFile = new File("sample_op.docx");
		try {
			FileOutputStream fos = new FileOutputStream(outputFile);
			doc.write(fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
