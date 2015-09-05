package docs.tool;

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
}
