/*
 * (C) VIF3 - Email: info@bbs2-leer.de
 * Diese Software kann frei genutzt werden. Auch gegen die Bearbeitung und
 * Aenderung des Quellcodes spricht nichts. Wer Verbesserungen vornimmt oder  
 * Fehler findet: Wir freuen uns ueber Feedback! Gerne auch in Form von Kritik.
 * Bitte diesen Vermerk nicht loeschen!
 */
package hwapp;

/**
 *
 * @author Martin Lesch
 * @version 03.05.2018
 */
public class ImportUser {
    String passwort = "";
    String user = "root";
    String dbn = "userimport";
    String host = "10.5.101.5";
    hwapp.Logger logger;
    MySQL mySQL;

    public ImportUser (Logger logger) {
        this.logger = logger;
        
    }
        
    public void getUserData() {
        // !! Achtung in mysql\bin in der my.ini den Wert bin-address die loopback ip in "0.0.0.0" ändern
        System.out.println("Host: " + this.host + ", Datenbank: " + this.dbn + ", User: " + this.user + ", Passwort: " + this.passwort);
        this.mySQL = new MySQL(this.logger, this.host, 3306, this.dbn, this.user, this.passwort);
        this.mySQL.connect();
        if (mySQL.isConnected()) {
            System.out.println("Verbindung zu externen MYSQL? " + mySQL.isConnected());
            mySQL.disconnect();
        } else {
            System.out.println(" .... das ging schief ....");
        }
        
    }
}
