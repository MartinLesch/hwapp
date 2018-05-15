/*
 * Klasse: XMLContentHandler
 */
package hwapp;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 *
 * @author Martin Lesch
 * @version 21.02.2018
 */
public class XMLContentHandler implements ContentHandler {
  
  //private ArrayList<KlassenName> arrayListName = new ArrayList<KlassenName>();
  
  private String currentValue;
  //private KlassenName VariablenName2;
  
  private int id = 1;
  Logger logger;
  Tools tools;
  
  public XMLContentHandler(Logger logger, Tools tools){
      this.logger = logger;
      this.tools = tools;
  }
  
  @Override
  public void characters(char[] ch, int start, int length) throws SAXException {
    this.currentValue = new String(ch, start, length);
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
    if (localName.equalsIgnoreCase("NameXMLTag")) {
      //variablenName2 = new KlassenName();
      //KlassenName.setId(this.id);
      id++;
      //KlasseName.setXYZ(tools.tryParseInt(atts.getValue("NameXMLTagAtribut"), "AttributName"));
    }
  }

   @Override
   public void endElement(String uri, String localName, String qName) throws SAXException {

    // XMLTAG innerhalb von NameXMLTag
    if (localName.equalsIgnoreCase("NameXMLTag2")) {
      //KlassenName.setXYZ2(currentValue);
    }
    
    // NameXMLTag End-Tag erreicht
    if (localName.equalsIgnoreCase("NameXMLTag")) {
      //arrayListName.add(KlassenName);
      //logger.addToLogFile(this.getClass().getSimpleName(), KlassenName.toString(), LogLevel.LoggingLevel.INFO);
      //System.out.println(KlassenName);
    }
   }
   
  @Override
    public void endDocument() throws SAXException {}
  @Override
    public void endPrefixMapping(String prefix) throws SAXException {}
  @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {}
  @Override
    public void processingInstruction(String target, String data) throws SAXException {}
  @Override
    public void setDocumentLocator(Locator locator) {  }
  @Override
    public void skippedEntity(String name) throws SAXException {}
  @Override
    public void startDocument() throws SAXException {}
  @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {}
}
