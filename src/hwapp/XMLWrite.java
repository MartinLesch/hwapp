/*
 * (C) VIF3 - Email: info@bbs2-leer.de
 * Diese Software kann frei genutzt werden. Auch gegen die Bearbeitung und
 * Aenderung des Quellcodes spricht nichts. Wer Verbesserungen vornimmt oder  
 * Fehler findet: Wir freuen uns ueber Feedback! Gerne auch in Form von Kritik.
 * Bitte diesen Vermerk nicht loeschen!
 */
package hwapp;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 *
 * @author Martin Lesch
 */
public class XMLWrite {
    XMLOutputFactory factory = XMLOutputFactory.newInstance();
    XMLStreamWriter writer;
    private String fileName;

    private hwapp.Logger logger;
    private Tools tools;
    
    
    public XMLWrite(hwapp.Logger logger, Tools tools) {
    this.logger = logger;
    this.tools = tools;
    }
    
    public void writeTestXML (String fileName) {
    this.fileName = fileName;
        try {

            this.writer = factory.createXMLStreamWriter( new FileOutputStream( this.fileName ) );
            // Der XML-Header wird erzeugt
            writer.writeStartDocument();
                // Zuerst wird das Wurzelelement mit Attribut geschrieben
                writer.writeStartElement( "StartTest" );
                writer.writeAttribute( "datum", "31.12.17" );
                // Unter dieses Element das Element gast mit einem Attribut erzeugen
                    writer.writeStartElement( "ElementVonStartTest" );
                    writer.writeAttribute( "name", "Max Mustermann" );
                    writer.writeEndElement();
                writer.writeEndElement();
            writer.writeEndDocument();
            writer.close();

        } catch (XMLStreamException ex) {
            this.logger.addToLogFile(this.getClass().getSimpleName(), "Fehler in XML-Aufbereitung!" + ex, LogLevel.LoggingLevel.ERROR);
        } catch (FileNotFoundException ex) {
            this.logger.addToLogFile(this.getClass().getSimpleName(), "Fehler in Datei!" + ex, LogLevel.LoggingLevel.ERROR);
        }

    }
}
