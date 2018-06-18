/*
 * (C) VIF3 - Email: info@bbs2-leer.de
 * Diese Software kann frei genutzt werden. Auch gegen die Bearbeitung und
 * Aenderung des Quellcodes spricht nichts. Wer Verbesserungen vornimmt oder  
 * Fehler findet: Wir freuen uns ueber Feedback! Gerne auch in Form von Kritik.
 * Bitte diesen Vermerk nicht loeschen!
 */
package hwapp;
import hwapp.LogLevel.LoggingLevel;

/**
 *
 * @author Martin Lesch
 * @version 27.04.2018
 */
public class MainProgram {
     /**
     * @param args Argumente die aus der Kommandozeile uebergeben werden
     */
    public static void main(String[] args) {
        
        Logger logger = new Logger(LoggingLevel.WARNING, true, true , true, false, true, false);
        Tools tools = new Tools(logger);
        tools.setEnvrionment();
        
        MySQLCreate createDB = new MySQLCreate(logger);
        createDB.createDatabase();
        createDB.createTables();
        createDB = null;
        
        //ImportUser imU = new ImportUser(logger);
        //imU.getUserData();
        
    }
}
