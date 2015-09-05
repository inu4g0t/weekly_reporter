package docs.helper;

import java.io.File;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

public abstract class AbstractDocHelper {
	String fileName;
	File docFile;
	XWPFDocument doc;
	
	public XWPFDocument getDoc() {
		return doc;
	}

	public AbstractDocHelper(String fn) {
	}
	
	public abstract void process();
	
	public abstract void removeTemplate();
	
	
}
