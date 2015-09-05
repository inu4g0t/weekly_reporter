package docs.template;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import docs.helper.TemplateElementReader;

public abstract class AbstractDocTemplate {
	
	private static final Logger logger = LogManager.getLogger();

	XWPFDocument doc;
	TemplateElementReader templateReader;
	Map<String, IBodyElement> elemMap;
	Map<String, XWPFParagraph> paraMap;

	public abstract void writeToWord(String outputFileName);

	public abstract void reset() throws Exception;
	
	public abstract void buildSampleDoc(String outputFile);
	
}
