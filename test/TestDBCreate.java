
import hwapp.LogLevel;
import hwapp.Logger;
import hwapp.MySQLCreate;
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
public class TestDBCreate {
         /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        
        Logger logger = new Logger(LogLevel.LoggingLevel.INFO, true, true , true, false, true, false);
        Tools tools = new Tools(logger);
        
        MySQLCreate createDB = new MySQLCreate(logger);
        createDB.createDatabase();
        createDB.createTables();
        createDB = null;
        
    }
}
