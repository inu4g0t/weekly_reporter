package org.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import docs.tool.XWPFTool;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
    	String htmlString = "<html dir=\"ltr\"><head></head><body contenteditable=\"true\"><p><font face=\"Segoe UI\">213</font></p></body></html>";
    	String fileName = "Sample.docx";
    	XWPFDocument doc = null;
    	FileInputStream fis = null;
    	File docFile = new File(fileName);
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
				}
			}
		}
    	XWPFTool.addHtmlToDoc(doc, htmlString);
        assertTrue( true );
    }
}
