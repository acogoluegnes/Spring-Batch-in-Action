/**
 * 
 */
package com.manning.sbia.ch11.integration;

import java.io.StringReader;

import javax.xml.transform.sax.SAXSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;

import org.xml.sax.InputSource;

/**
 * @author acogoluegnes
 *
 */
public class ProductImportUtils {

	public static String extractImportId(String xmlContent) {
		String importId;
		try {
			XPathFactory xpathFactory = XPathFactory.newInstance(XPathFactory.DEFAULT_OBJECT_MODEL_URI);
			XPath xpath = xpathFactory.newXPath();
			SAXSource saxSource = new SAXSource(new InputSource(new StringReader(xmlContent)));
			importId = (String) xpath.evaluate("//@import-id", saxSource.getInputSource(), XPathConstants.STRING);
			return importId;
		} catch (XPathFactoryConfigurationException e) {
			throw new RuntimeException(e);
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}
	}
	
}
