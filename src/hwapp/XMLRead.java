/*
 * Klasse XMLREADER.
 */
package hwapp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 *
 * @author Martin Lesch
 * @version 08.03.2018
 */
public class XMLRead {
    private String fileName;
    // Beispiel: "xyzdateiname.XML"
    private Logger logger;
    private Tools tools;
    
public XMLRead(Logger logger, Tools tools){
    this.logger = logger;
    this.tools = tools;
}
public void readFileXML (String fileName) {
    this.fileName = fileName;
    try {
        // XMLRead erzeugen
        logger.addToLogFile(this.getClass().getSimpleName(), "Lese XML-Datei ein ...  (Datei: " + fileName + ")", LogLevel.LoggingLevel.INFO);
        XMLReader xmlReader = XMLReaderFactory.createXMLReader();
        Charset inputCharset = Charset.forName("ISO-8859-1");
        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(
                                new FileInputStream(this.fileName), inputCharset));
        InputSource inputSource = new InputSource(reader);

        // DTD kann optional übergeben werden
        // inputSource.setSystemId("nnnnnnnnnnnn.dtd");

        // XMLContentHandler wird übergeben
        xmlReader.setContentHandler(new XMLContentHandler(logger, tools));

        // Parsen wird gestartet
        xmlReader.parse(inputSource);
        
    } catch (FileNotFoundException e) {
        logger.addToLogFile(this.getClass().getSimpleName(), "Datei nicht vorhanden!", LogLevel.LoggingLevel.ERROR);
      //e.printStackTrace();
    } catch (IOException e) {
        logger.addToLogFile(this.getClass().getSimpleName(), "Datei kann nicht geoeffnet werden!", LogLevel.LoggingLevel.ERROR);
      //e.printStackTrace();
    } catch (SAXException e) {
        logger.addToLogFile(this.getClass().getSimpleName(), "XML-Parser fehlgeschlagen!", LogLevel.LoggingLevel.ERROR);
      //e.printStackTrace();
    }
}

}
