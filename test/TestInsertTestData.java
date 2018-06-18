
import hwapp.LogLevel;
import hwapp.Logger;
import hwapp.MySQL;

/*
 * (C) VIF3 - Email: info@bbs2-leer.de
 * Diese Software kann frei genutzt werden. Auch gegen die Bearbeitung und
 * Aenderung des Quellcodes spricht nichts. Wer Verbesserungen vornimmt oder  
 * Fehler findet: Wir freuen uns ueber Feedback! Gerne auch in Form von Kritik.
 * Bitte diesen Vermerk nicht loeschen!
 */

/**
 *
 * @author Martin Lesch
 * @version 15.05.2018
 */
public class TestInsertTestData {

    public static void main(String[] args) {
        // Einfuegen von Testdaten in die SQL-Datenbank "hwapp"
        String passwort = "";
        String user = "root";
        String dbn = "hwapp";
        
        Logger logger = new Logger(LogLevel.LoggingLevel.INFO, true, true , true, false, true, false);
        //Tools tools = new Tools(logger);
        
        MySQL mySQL = new MySQL(logger, "localhost", 3306, dbn, user, passwort);
        mySQL.connect();
        mySQL.setAutoCommit(false);
        
        mySQL.doUpdate("INSERT INTO users VALUES (NULL, 'mm@xyz.de', 'Mustermann', 'Max', 'geheim', 1)");
        mySQL.doUpdate("INSERT INTO users VALUES (NULL, 'mm@xyz.de', 'Musterfrau', 'Maria', 'passwort', 0)");

        // IMEI aus User entfernt, eigene Tabelle mit IMEI
        mySQL.doUpdate("INSERT INTO devices VALUES (NULL, '1234567890ABCDEFGHIJKLMNOPQERSTUVWXYZ', 1)");
        mySQL.doUpdate("INSERT INTO devices VALUES (NULL, 'abcdefghijklmnopqrstuvwxyzÄÖÜäöüß²#+-.,<', 2)");

        mySQL.doUpdate("INSERT INTO status VALUES (NULL, 'offen')");
        mySQL.doUpdate("INSERT INTO status VALUES (NULL, 'erledigt')");
        mySQL.doUpdate("INSERT INTO status VALUES (NULL, 'wiki')");

        mySQL.doUpdate("INSERT INTO prioritaeten VALUES (NULL, 'rot')");
        mySQL.doUpdate("INSERT INTO prioritaeten VALUES (NULL, 'gelb')");
        mySQL.doUpdate("INSERT INTO prioritaeten VALUES (NULL, 'gruen')");
        
        mySQL.doUpdate("INSERT INTO merkmale VALUES (NULL, 'Halle')");
        mySQL.doUpdate("INSERT INTO merkmale VALUES (NULL, 'Gebaeude')");
        mySQL.doUpdate("INSERT INTO merkmale VALUES (NULL, 'Geschoss')");

        mySQL.doUpdate("INSERT INTO submerkmale VALUES (NULL, 'Lagerhalle')");
        mySQL.doUpdate("INSERT INTO submerkmale VALUES (NULL, 'Maschinenhalle')");
        mySQL.doUpdate("INSERT INTO submerkmale VALUES (NULL, 'Produktionshalle')");
        mySQL.doUpdate("INSERT INTO submerkmale VALUES (NULL, 'A-Gebäude')");
        mySQL.doUpdate("INSERT INTO submerkmale VALUES (NULL, 'B-Gebäude')");
        mySQL.doUpdate("INSERT INTO submerkmale VALUES (NULL, 'Erdgeschoss')");
        mySQL.doUpdate("INSERT INTO submerkmale VALUES (NULL, 'Obergeschoss')");
        mySQL.doUpdate("INSERT INTO submerkmale VALUES (NULL, 'Keller')");
        
        mySQL.setSavepoint("afterSubMerkmale");
        
        mySQL.doUpdate("INSERT INTO projekte VALUES (1, 'Testprojekt XYZ')");
        mySQL.doUpdate("INSERT INTO projekte VALUES (2, 'Musterprojekt ABC')");

        mySQL.doUpdate("INSERT INTO tickets VALUES (NULL, 'Tor defekt', 'Testbeschreibung Tor geht nicht mehr auf', '2018-05-01', NULL, NULL, 0, 1, 1, 1, 2)");
        mySQL.doUpdate("INSERT INTO tickets VALUES (NULL, 'Motor defekt', 'Testbeschreibung Motor tut nicht mehr', '2018-05-01', '2018-05-02 11:11:11', NULL, 0, 2, 2, 3, 3)");

        mySQL.doUpdate("INSERT INTO zuordnungmerkmaletickets VALUES (1, 1, 1)");
        mySQL.doUpdate("INSERT INTO zuordnungmerkmaletickets VALUES (2, 4, 1)");
        mySQL.doUpdate("INSERT INTO zuordnungmerkmaletickets VALUES (3, 6, 1)");
        mySQL.doUpdate("INSERT INTO zuordnungmerkmaletickets VALUES (1, 1, 2)");
        mySQL.doUpdate("INSERT INTO zuordnungmerkmaletickets VALUES (3, 7, 2)");

        mySQL.doUpdate("INSERT INTO kategorien VALUES (NULL, 'Tortyp')");
        mySQL.doUpdate("INSERT INTO kategorien VALUES (NULL, 'Antrieb')");
        mySQL.doUpdate("INSERT INTO kategorien VALUES (NULL, 'Hersteller')");

        mySQL.doUpdate("INSERT INTO subkategorien VALUES (NULL, 'Rolltor')");
        mySQL.doUpdate("INSERT INTO subkategorien VALUES (NULL, 'Fluegeltor')");
        mySQL.doUpdate("INSERT INTO subkategorien VALUES (NULL, 'Elektrisch')");
        mySQL.doUpdate("INSERT INTO subkategorien VALUES (NULL, 'Manuell')");
        mySQL.doUpdate("INSERT INTO subkategorien VALUES (NULL, 'A-Hersteller')");
        mySQL.doUpdate("INSERT INTO subkategorien VALUES (NULL, 'B-Hersteller')");

        mySQL.doUpdate("INSERT INTO zuordnungkategorientickets VALUES (1, 1, 1)");
        mySQL.doUpdate("INSERT INTO zuordnungkategorientickets VALUES (2, 3, 1)");
        mySQL.doUpdate("INSERT INTO zuordnungkategorientickets VALUES (3, 5, 1)");
        mySQL.doUpdate("INSERT INTO zuordnungkategorientickets VALUES (1, 1, 2)");
        mySQL.doUpdate("INSERT INTO zuordnungkategorientickets VALUES (3, 6, 2)");

        mySQL.doUpdate("INSERT INTO fotos VALUES (NULL, NULL, 'C://test1.jpg')");
        //mySQL.doUpdate("INSERT INTO fotos VALUES (NULL, LOAD_FILE('C:\test.png'), 'C://test.png')");
        //mySQL.doUpdate("INSERT INTO fotos VALUES (NULL, 0x89504E470D0A1A0A0000000D494844520000001000000010080200000090916836000000017352474200AECE1CE90000000467414D410000B18F0BFC6105000000097048597300000EC300000EC301C76FA8640000001E49444154384F6350DAE843126220493550F1A80662426C349406472801006AC91F1040F796BD0000000049454E44AE426082, 'C://test2.jpg')");

        mySQL.doUpdate("INSERT INTO zuordnungfotostickets VALUES (1, 1)");

        mySQL.doUpdate("INSERT INTO zuordnungkategorienusers VALUES (1, 1, 1)");
        mySQL.doUpdate("INSERT INTO zuordnungkategorienusers VALUES (2, 3, 1)");
        mySQL.doUpdate("INSERT INTO zuordnungkategorienusers VALUES (3, 5, 1)");
        mySQL.doUpdate("INSERT INTO zuordnungkategorienusers VALUES (1, 1, 2)");
        mySQL.doUpdate("INSERT INTO zuordnungkategorienusers VALUES (1, 2, 2)");

        mySQL.doUpdate("INSERT INTO antworten VALUES (NULL, 'Antwort zu Ticket 2', 'Motor braucht Oel', NULL, 0, 1, 2, 1)");
        
        mySQL.rollback("afterSubMerkmale");
        mySQL.commit();
        mySQL.setAutoCommit(true);
   
        mySQL.disconnect();
        mySQL = null;
    }
}
