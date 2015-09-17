package org.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import po.NodeXML;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		NodeXML root = new NodeXML("root");
		NodeXML c1 = new NodeXML("c1");
		NodeXML c2 = new NodeXML("c2");
		root.addChild(c1);
		root.addChild(c2);
		File file = new File("output.xml");
		try {
			JAXBContext context = JAXBContext.newInstance(NodeXML.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			// Marshalling and saving XML to the file.
			m.marshal(root, file);

		} catch (Exception e) { // catches ANY exception
			e.printStackTrace();
		}
	}
}
