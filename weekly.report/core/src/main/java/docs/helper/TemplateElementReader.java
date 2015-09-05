package docs.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;

// Class to read in template doc file
public class TemplateElementReader extends AbstractDocHelper {

	private static final Logger logger = LogManager.getLogger();

	public Map<String, IBodyElement> elemMap;
	public Map<String, XWPFParagraph> paraMap;
	private int templateElementNum;

	public TemplateElementReader(String fn) throws FileNotFoundException {
		super(fn);
		fileName = fn;

	}

	private void addKeyToMap(IBodyElement elem) {
		String key = "";
		switch (elem.getElementType()) {
		case TABLE:
			key = ((XWPFTable) elem).getText().trim();
			break;
		case PARAGRAPH:
			key = ((XWPFParagraph) elem).getText().trim();
			break;
		default:
			logger.error("Cannot deal with element type:"
					+ elem.getElementType().toString());
			break;
		}
		if (key.equals("")) {
			return;
		}
		if (elemMap.containsKey(key)) {
			logger.warn("The template for " + key + " is already exist and"
					+ "this one will be ignore");
			return;
		}
		elemMap.put(key, elem);
		logger.debug("Put element " + key + " into elem map.");
		if (elem.getElementType().equals(BodyElementType.PARAGRAPH)) {
			paraMap.put(key, (XWPFParagraph) elem);
			logger.debug("Put element " + key + " into para map.");
		}
	}

	public void process() {
		logger.debug("Start processing");
		Iterator<IBodyElement> iter = doc.getBodyElementsIterator();
		IBodyElement tmpElem;
		while (iter.hasNext()) {
			tmpElem = iter.next();
			addKeyToMap(tmpElem);

		}
		this.templateElementNum = doc.getBodyElements().size();

	}

	@Override
	public void removeTemplate() {
		// TO-DO need to re do here as now relies on assumption that title is
		// the first element
		for (int i = getTemplateElementNum() - 1; i >= 1; i--) {
			doc.removeBodyElement(i);
		}
	}

	public int getTemplateElementNum() {
		return templateElementNum;
	}

	public void reset() throws FileNotFoundException {
		// TODO Auto-generated method stub
		FileInputStream fis = null;
		docFile = new File(fileName);
		if (!docFile.exists()) {
			throw new FileNotFoundException("The Word dcoument " + fileName
					+ " does not exist.");
		}
		try {
			// Open the Word document file and instantiate the XWPFDocument
			// class.
			fis = new FileInputStream(docFile);
			doc = new XWPFDocument(fis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
					fis = null;
				} catch (IOException ioEx) {
					logger.error("IOException caught trying to close "
							+ "FileInputStream in the constructor of "
							+ "UpdateEmbeddedDoc.");
				}
			}
		}
		logger.info("Initiate ModulerReader finish.");
		elemMap = new HashMap<String, IBodyElement>();
		paraMap = new HashMap<String, XWPFParagraph>();
		this.templateElementNum = 0;
		process();
	}

}
