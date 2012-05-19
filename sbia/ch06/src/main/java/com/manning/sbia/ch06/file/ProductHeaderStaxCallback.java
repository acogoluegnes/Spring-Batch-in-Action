/**
 *
 */
package com.manning.sbia.ch06.file;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.springframework.batch.item.xml.StaxWriterCallback;

/**
 * @author bazoud
 *
 */
public class ProductHeaderStaxCallback implements StaxWriterCallback {

    @Override
    public void write(XMLEventWriter writer) throws IOException {
        try {
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();

            XMLEvent event = eventFactory.createStartElement("", "", "header");
            writer.add(event);
            event = eventFactory.createAttribute("generated", DateFormat.getDateTimeInstance().format(new Date()));
            writer.add(event);
            event = eventFactory.createEndElement("", "", "header");
            writer.add(event);
        } catch (XMLStreamException e) {
        }

    }

}
