
import hwapp.LogLevel;
import hwapp.Logger;
import hwapp.Tools;
import hwapp.XMLWrite;

/*
 * (C) VIF3 - Email: info@bbs2-leer.de
 * Diese Software kann frei genutzt werden. Auch gegen die Bearbeitung und
 * Aenderung des Quellcodes spricht nichts. Wer Verbesserungen vornimmt oder  
 * Fehler findet: Wir freuen uns ueber Feedback! Gerne auch in Form von Kritik.
 * Bitte diesen Vermerk nicht loeschen!
 */

/**
 *
 * @author Computer
 */
public class TestXMLWrite {
    
    
    public static void main(String[] args) {
        
        Logger logger = new Logger(LogLevel.LoggingLevel.INFO, true, true , true, false, true, false);
        Tools tools = new Tools(logger);
 
        XMLWrite testXML = new XMLWrite(logger, tools);
        testXML.writeTestXML("beispiel.xml");
        testXML = null;

    }
}