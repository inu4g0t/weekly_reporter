package docs.template;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFAbstractNum;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFNumbering;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;

import components.TextMutableTreeNode;
import po.Report;
import docs.helper.TemplateElementReader;
import docs.tool.XWPFTool;

public class SimpleDocTemplate extends AbstractDocTemplate {

	private static final Logger logger = LogManager.getLogger();
	
	private static String TITLE_STRING = "Title";
	private static String CAPTION_STRING = "Caption";
	private static String SUBCAPTION_STRING = "SubCaption";
	private static String NORMAL_TEXT = "Text";

	protected TextMutableTreeNode rootNode;
	protected Report report;

	private static String IMPORTANT_CHANGSE = "重要项目产出";
	private static String OTHER_PROJECTS = "其他项目";

	private IBodyElement title;

	private boolean inited;

	private void replaceText(IBodyElement elem, String oldString,
			String newString) {
		switch (elem.getElementType()) {
		case TABLE:
			((XWPFTable) elem).getText().replaceAll(oldString, newString);
			break;
		case PARAGRAPH:
			((XWPFParagraph) elem).getText().replaceAll(oldString, newString);
			break;
		default:
			logger.error("Cannot deal with element type:"
					+ elem.getElementType().toString());
		}
	}

	public SimpleDocTemplate(TemplateElementReader templateElementReader)
			throws Exception {
		this.templateReader = templateElementReader;
		
		reset();
		
		init();
		
	}

	@Override
	public void writeToWord(String outputFileName) {
		templateReader.removeTemplate();
		File outputFile = new File(outputFileName);
		try {
			FileOutputStream fos = new FileOutputStream(outputFile);
			doc.write(fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error("Fail to open file for output at " + outputFileName);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Fail to write doc to file at " + outputFileName);
		}
		try {
			reset();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void reset() throws Exception  {
		templateReader.reset();
		this.elemMap = templateReader.elemMap;
		this.paraMap = templateReader.paraMap;
		this.doc = templateReader.getDoc();
		this.inited = false;

		if (elemMap == null) {
			logger.error("Elemet map is NULL");
		}

		if (!elemMap.containsKey(TITLE_STRING)) {
			logger.error("The element map misses ensential element "
					+ TITLE_STRING);
			throw new Exception("The element map misses ensential element "
					+ TITLE_STRING);
		}
		if (!elemMap.containsKey(CAPTION_STRING)) {
			logger.error("The element map misses ensential element"
					+ CAPTION_STRING);
			throw new Exception("The element map misses ensential element "
					+ CAPTION_STRING);
		}
		if (!elemMap.containsKey(SUBCAPTION_STRING)) {
			logger.error("The element map misses ensential element "
					+ SUBCAPTION_STRING);
			throw new Exception("The element map misses ensential element "
					+ SUBCAPTION_STRING);
		}

		if (!elemMap.containsKey(NORMAL_TEXT)) {
			logger.error("The element map misses ensential element "
					+ SUBCAPTION_STRING);
			throw new Exception("The element map misses ensential element "
					+ SUBCAPTION_STRING);
		}
	}

	@Override
	public void buildSampleDoc(String outputFile) {
		// TODO Auto-generated method stub
		title = elemMap.get(TITLE_STRING);
		replaceText(title, TITLE_STRING, "Sample Title");
		XWPFParagraph tmpPara;

		// XWPFNumbering numbering = doc.getNumbering();
		InputStream in;

		tmpPara = doc.createParagraph();
		XWPFTool.cloneParagraphAndChangeText(tmpPara,
				paraMap.get(CAPTION_STRING), "Sample Caption 1");
		System.out.println(tmpPara.getNumFmt());
		System.out.println(tmpPara.getNumLevelText());
		System.out.println(tmpPara.getNumID());
		System.out.println(tmpPara.getNumIlvl());
		tmpPara = doc.createParagraph();
		XWPFTool.cloneParagraphAndChangeText(tmpPara,
				paraMap.get(SUBCAPTION_STRING), "Sample SubCaption 1");
		System.out.println(tmpPara.getNumFmt());
		System.out.println(tmpPara.getNumLevelText());
		System.out.println(tmpPara.getNumID());
		System.out.println(tmpPara.getNumIlvl());
		tmpPara = doc.createParagraph();
		XWPFTool.cloneParagraphAndChangeText(tmpPara,
				paraMap.get(SUBCAPTION_STRING), "Sample SubCaption 1");
		System.out.println(tmpPara.getNumFmt());
		System.out.println(tmpPara.getNumLevelText());
		System.out.println(tmpPara.getNumID());
		System.out.println(tmpPara.getNumIlvl());

		tmpPara = doc.createParagraph();
		XWPFTool.cloneParagraphAndChangeText(tmpPara,
				paraMap.get(CAPTION_STRING), "Sample Caption 2");
		System.out.println(tmpPara.getNumFmt());
		System.out.println(tmpPara.getNumLevelText());
		System.out.println(tmpPara.getNumID());
		System.out.println(tmpPara.getNumIlvl());
		tmpPara = doc.createParagraph();
		XWPFTool.cloneParagraphAndChangeText(tmpPara,
				paraMap.get(SUBCAPTION_STRING), "Sample SubCaption 2");
		System.out.println(tmpPara.getNumFmt());
		System.out.println(tmpPara.getNumLevelText());
		System.out.println(tmpPara.getNumID());
		System.out.println(tmpPara.getNumIlvl());

		this.templateReader.removeTemplate();
		writeToWord(outputFile);

	}

	private void parseNode(TextMutableTreeNode node) {
		XWPFParagraph tmpPara;
		switch (node.getLevel()) {
		case 0:
			title = elemMap.get(TITLE_STRING);
			replaceText(title, TITLE_STRING, "周报");
			break;
		case 1:
			tmpPara = doc.createParagraph();
			XWPFTool.cloneParagraphAndChangeText(tmpPara,
					paraMap.get(CAPTION_STRING), (String) node.getUserObject());
			break;
		case 2:
			tmpPara = doc.createParagraph();
			XWPFTool.cloneParagraphAndChangeText(tmpPara,
					paraMap.get(SUBCAPTION_STRING), (String) node.getUserObject());
			break;
		default:
			tmpPara = doc.createParagraph();
			XWPFTool.cloneParagraphAndChangeText(tmpPara,
					paraMap.get(NORMAL_TEXT), (String) node.getUserObject());
			break;
		}
		tmpPara = doc.createParagraph();
		if (node.getText() != null && !node.getText().equals("")) {
			XWPFTool.cloneParagraphAndChangeText(tmpPara,
					paraMap.get(NORMAL_TEXT), node.getText());
		}
		Enumeration<TextMutableTreeNode> childrenEnum = node.children();
		while (childrenEnum.hasMoreElements()) {
			TextMutableTreeNode child = (TextMutableTreeNode) childrenEnum
					.nextElement();
			parseNode(child);
		}
	}

	public void parseReport(Report reportTree) {
		parseNode((TextMutableTreeNode) reportTree.getRoot());
	}

	private TextMutableTreeNode insertEssentialChildToParent(
			TextMutableTreeNode parent, Object child) {
		TextMutableTreeNode childNode = new TextMutableTreeNode(child, true);

		if (parent == null) {
			parent = rootNode;
		}

		// It is key to invoke this on the TreeModel, and NOT
		// TextMutableTreeNode
		report.insertNodeInto(childNode, parent, parent.getChildCount());

		return childNode;
	}

	private void init() {
		if (!inited) {
			rootNode = new TextMutableTreeNode("Weekly Report");
			report = new Report(rootNode);

			TextMutableTreeNode ic = insertEssentialChildToParent(null,
					IMPORTANT_CHANGSE);
			TextMutableTreeNode op = insertEssentialChildToParent(null,
					OTHER_PROJECTS);

			inited = true;
		}
	}

	public Report getReport() {
		if (!inited) {
			init();
		}
		return report;
	}

	public TextMutableTreeNode getRootNode() {
		// TODO Auto-generated method stub
		if (!inited) {
			init();
		}
		return rootNode;
	}

}
