/*
 * (C) VIF3 - Email: info@bbs2-leer.de
 * Diese Software kann frei genutzt werden. Auch gegen die Bearbeitung und
 * Aenderung des Quellcodes spricht nichts. Wer Verbesserungen vornimmt oder  
 * Fehler findet: Wir freuen uns ueber Feedback! Gerne auch in Form von Kritik.
 * Bitte diesen Vermerk nicht loeschen!
 */
import hwapp.DataHandler;
import hwapp.LogLevel;
import hwapp.Logger;

/**
 *
 * @author Martin Lesch
 * @version 18.05.2018
 */
public class TestDataHandler {
 
    public static void main(String[] args) {
        
        Logger logger = new Logger(LogLevel.LoggingLevel.INFO, true, true , true, false, true, false);
        
        DataHandler handDB = new DataHandler(logger, "localhost", 3306, "hwapp", "root", "");
        handDB.setNewUser("hwapp@bbs2leer.de", "Bbsuser", "Jeder", "geheim", 1);
        handDB.setNewUser("irgendwer@bbs2leer.de", "Test", "Test", "geheim1", 0);
        // folgende müssten falsch sein
        handDB.setNewUser("irgendwer@bbs2leer.de", "Test", "Test", "geheim1", 0); //doppelt
        handDB.setNewUser("falsch@bbs2leer.de", "Test", "Test", "geheim1", 3); //falsche DarfMMC
        
        String [][] usersArray = handDB.getAllUserAsStringArray();
        System.out.println("Länge: " + usersArray.length);
        for (int i=0; i<usersArray.length; i++){
            System.out.println("Benutzer Login Name: " + usersArray[i][1]);
        }
        String [][] devicesArray = handDB.getAllDevicesForThisUserAsStringArray(1);
        System.out.println("Länge: " + devicesArray.length);
        System.out.println("IMEI fuer User 1: " + devicesArray[0][1]);
       
    }
}
