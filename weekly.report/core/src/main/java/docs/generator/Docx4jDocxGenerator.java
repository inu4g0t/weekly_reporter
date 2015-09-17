package docs.generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Enumeration;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.docx4j.XmlUtils;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.AltChunkType;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.openpackaging.parts.WordprocessingML.NumberingDefinitionsPart;
import org.docx4j.wml.BooleanDefaultTrue;
import org.docx4j.wml.HpsMeasure;
import org.docx4j.wml.Numbering;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;
import org.docx4j.wml.PPrBase.Ind;
import org.docx4j.wml.PPrBase.Spacing;
import org.docx4j.wml.R;
import org.docx4j.wml.RFonts;
import org.docx4j.wml.RPr;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.PPrBase.NumPr;
import org.docx4j.wml.PPrBase.NumPr.Ilvl;
import org.docx4j.wml.PPrBase.NumPr.NumId;
import org.docx4j.wml.TblPr;
import org.docx4j.wml.TblWidth;

import po.NodeXML;
import po.ReportXML;

public class Docx4jDocxGenerator implements DocxGenerator {

	private static final Logger logger = LogManager.getLogger();

	static org.docx4j.wml.ObjectFactory factory = Context.getWmlObjectFactory();

	static final String defaultXMLPath = "docs_moduler/";

	static final String defaultTitleXML = "title.xml";

	static final String defaultNumberingXML = "numbering.xml";

	static final String defaultParagraphXML = "paragraph.xml";

	private final static String dtd = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\""
			+ " \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";

	private String titleXMLTemplate;
	private String numberingXML;
	private String paragraphXMLTemplate;

	public Docx4jDocxGenerator() {
		try {
			titleXMLTemplate = this.getFileContents(defaultXMLPath
					+ defaultTitleXML);
			numberingXML = this.getFileContents(defaultXMLPath
					+ defaultNumberingXML);
			// paragraphXMLTemplate = this.getFileContents(defaultXMLPath
			// + defaultParagraphXML);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public void exportReportToDocx(ReportXML r, String outputPath) {
		exportReportToDocx(r, new java.io.File(outputPath));
	}

	public void exportReportToDocx(ReportXML r, File outputFile) {

		try {
			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage
					.createPackage();

			// Add numbering part
			NumberingDefinitionsPart ndp = new NumberingDefinitionsPart();
			wordMLPackage.getMainDocumentPart().addTargetPart(ndp);
			ndp.setJaxbElement((Numbering) XmlUtils
					.unmarshalString(numberingXML));

			MainDocumentPart mainDoc = wordMLPackage.getMainDocumentPart();

			parseNode(wordMLPackage, (NodeXML) r.getRootNode());
			// Add title

			wordMLPackage.save(outputFile);

		} catch (JAXBException e) {
			logger.error(e);
		} catch (InvalidFormatException e) {
			logger.error(e);
		} catch (Docx4JException e) {
			logger.error(e);
		}
	}

	private void parseNode(WordprocessingMLPackage wordMLPackage,
			NodeXML node) throws Docx4JException {
		XWPFParagraph tmpPara;
		switch (node.getLevel()) {
		case 0:
			wordMLPackage
					.getMainDocumentPart()
					.getContent()
					.add(createTitleWithTemplate((String) node.getText()));
			break;

		default:
			wordMLPackage
					.getMainDocumentPart()
					.getContent()
					.add(createNumberedParagraph(1, node.getLevel() - 1,
							(String) node.getText()));
			break;
		}
		if (node.getHtmlText() != null
				&& !node.getHtmlText().equals("")
				&& !node.getHtmlText()
						.equals("<html dir=\"ltr\"><head></head><body contenteditable=\"true\"></body></html>")) {
			addHTMLParagraph(wordMLPackage, node.getLevel(), node.getHtmlText());
		}
		for (NodeXML child : node.getChildren()) {
			parseNode(wordMLPackage, child);
		}
	}

	private String getFileContents(String fileName) throws Exception {
		File theFile = new File(fileName);
		byte[] bytes = new byte[(int) theFile.length()];
		InputStream in = new FileInputStream(theFile);
		int m = 0, n = 0;
		while (m < bytes.length) {
			n = in.read(bytes, m, bytes.length - m);
			m += n;
		}
		in.close();

		// using default encoding, this is probably what BufferedReader.readLine
		// does anyway
		return new String(bytes);
	}

	private Tbl createTitleWithTemplate(String titleString) {
		String titleXML = new String(titleXMLTemplate.replaceFirst(
				"TITLE_FOR_REPLACE", titleString));
		Tbl title = null;
		try {
			title = (Tbl) XmlUtils.unmarshalString(titleXML);
		} catch (JAXBException e) {
			logger.error(e);
		}
		return title;
	}

	private P createNumberedParagraph(long numId, long ilvl,
			String paragraphText) {

		P p = factory.createP();

		org.docx4j.wml.Text t = factory.createText();
		t.setValue(paragraphText);

		org.docx4j.wml.R run = factory.createR();
		run.getContent().add(t);

		RPr rpr = factory.createRPr();
		rpr.setB(new BooleanDefaultTrue());

		run.setRPr(rpr);
		HpsMeasure kernVal = new HpsMeasure();
		kernVal.setVal(BigInteger.valueOf(44));
		rpr.setKern(kernVal);
		HpsMeasure szVal = new HpsMeasure();
		szVal.setVal(BigInteger.valueOf(20));
		rpr.setSz(szVal);
		RFonts rFonts = new RFonts();
		rFonts.setAscii("微软雅黑");
		rFonts.setHAnsi("微软雅黑");
		rFonts.setEastAsia("微软雅黑");
		rpr.setRFonts(rFonts);

		p.getContent().add(run);

		org.docx4j.wml.PPr ppr = factory.createPPr();
		
		Spacing sp = new Spacing();
		sp.setBeforeLines(BigInteger.valueOf(50));
		sp.setAfterLines(BigInteger.valueOf(50));
		
		ppr.setSpacing(sp);;
		
		p.setPPr(ppr);

		// Create and add <w:numPr>
		NumPr numPr = factory.createPPrBaseNumPr();
		ppr.setNumPr(numPr);

		// The <w:ilvl> element
		Ilvl ilvlElement = factory.createPPrBaseNumPrIlvl();
		numPr.setIlvl(ilvlElement);
		ilvlElement.setVal(BigInteger.valueOf(ilvl));

		// The <w:numId> element
		NumId numIdElement = factory.createPPrBaseNumPrNumId();
		numPr.setNumId(numIdElement);
		numIdElement.setVal(BigInteger.valueOf(numId));

		return p;

	}

	private void addHTMLParagraph(WordprocessingMLPackage wordMLPackage,
			long ilvl, String htmlParagraph) throws Docx4JException {

		XHTMLImporterImpl XHTMLImporter = new XHTMLImporterImpl(wordMLPackage);

		htmlParagraph = htmlParagraph.replaceAll("<br>", "<br/>");

		List<Object> importedObjects = XHTMLImporter.convert(dtd
				+ htmlParagraph, null);

		for (Object o : importedObjects) {
			if (o instanceof P) {
				PPr ppr = ((P) o).getPPr();
				Ind ind = new Ind();
				ind.setLeft(BigInteger.valueOf(360 * ilvl));
				ppr.setInd(ind);
				wordMLPackage.getMainDocumentPart().addObject(o);
				for (Object c : ((P) o).getContent()) {
					if (c instanceof R) {
						RPr rpr = ((R) c).getRPr();
						if (rpr == null) {
							rpr = factory.createRPr();
							((R) c).setRPr(rpr);
						}
						RFonts rFonts = new RFonts();
						rFonts.setAscii("微软雅黑");
						rFonts.setHAnsi("微软雅黑");
						rFonts.setEastAsia("微软雅黑");
						rpr.setRFonts(rFonts);
						HpsMeasure szVal = new HpsMeasure();
						szVal.setVal(BigInteger.valueOf(20));
						rpr.setSz(szVal);
					}
				}
			} else if (o instanceof Tbl) {
				wordMLPackage.getMainDocumentPart().addObject(o);
			} else {
				logger.error("Unable to deal with object with xml: "
						+ XmlUtils.marshaltoString(o));
			}
		}
	}

}
