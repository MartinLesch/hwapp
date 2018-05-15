/*
 * (C) Martin Lesch - Email: Martin@Familie-Lesch.de
 * Diese Software kann frei genutzt werden. Auch gegen die Bearbeitung des
 * Quellcodes spricht nichts. Wer Verbesserungen vornimmt - ich bin
 * dankbar fuer jede Idee oder Erweiterung! Gerne auch in Form von Kritik.
 * Bitte diesen Vermerk nicht loeschen!
 */
package hwapp;

/**
 *
 * @author Martin Lesch
 * @version 27.04.2018
 */
public class MySQLCreate {
    private String user;
    private String password;
    private String databaseName;
    private hwapp.Logger logger;
    
    
   /**
    * 
    * @param logger LoggerObjekt von Martin Lesch muss uebergeben werden
    */
    public MySQLCreate(hwapp.Logger logger){
        this.logger = logger;
        this.user = "root";
        this.password = "";
        this.databaseName = "hwapp";
    }
    
    public void createDatabase() {
        // Zur Anlage der Datenbank wird eine Verbindung auf die Database=mysql hergestellt
        // User, Passwort und Datenbankname derzeit noch STATISCH - ggf. im Konstruktor anpassen
        MySQL mySQLInit = new MySQL(this.logger, "localhost", 3306, "mysql", this.user, this.password);
        mySQLInit.connect();
        mySQLInit.createDatabase(databaseName);
        mySQLInit.disconnect();
        mySQLInit = null;
    }

    public void createTables() {
        MySQL mySQLCreateTables = new MySQL(this.logger, "localhost", 3306, this.databaseName, this.user, this.password);
        mySQLCreateTables.connect();

        mySQLCreateTables.doUpdate("CREATE TABLE IF NOT EXISTS `users` "
                + "(`userID` int(10) unsigned NOT NULL AUTO_INCREMENT, "
                + "`userLogin` tinytext COLLATE utf8_unicode_ci DEFAULT NULL, "
                + "`userName` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL, "
                + "`userVorname` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL, "
                + "`userPasswort` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL, "
    //            + "`userIMEI` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL, "
                + "`userDarfMMC` tinyint unsigned DEFAULT NULL, "
                + "PRIMARY KEY (`userID`) )"
                );

        mySQLCreateTables.doUpdate("CREATE TABLE IF NOT EXISTS `devices` "
                + "(`deviceID` int(10) unsigned NOT NULL AUTO_INCREMENT, "
                + "`deviceIMEI` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL, "
                + "`deviceUserID` int(10) unsigned DEFAULT NULL, "
                + "PRIMARY KEY (`deviceID`) )"
                );

        mySQLCreateTables.doUpdate("CREATE TABLE IF NOT EXISTS `status` "
                + "(`statusID` int(10) unsigned NOT NULL AUTO_INCREMENT, "
                + "`statusName` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL, "
                + "PRIMARY KEY (`statusID`) )"
                );

        mySQLCreateTables.doUpdate("CREATE TABLE IF NOT EXISTS `prioritaeten` "
                + "(`prioritaetID` int(10) unsigned NOT NULL AUTO_INCREMENT, "
                + "`prioritaetName` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL, "
                + "PRIMARY KEY (`prioritaetID`) )"
                );

        mySQLCreateTables.doUpdate("CREATE TABLE IF NOT EXISTS `merkmale` "
                + "(`merkmalID` int(10) unsigned NOT NULL AUTO_INCREMENT, "
                + "`merkmalName` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL, "
                + "PRIMARY KEY (`merkmalID`) )"
                );

        mySQLCreateTables.doUpdate("CREATE TABLE IF NOT EXISTS `submerkmale` "
                + "(`submerkmalID` int(10) unsigned NOT NULL AUTO_INCREMENT, "
                + "`submerkmalName` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL, "
                + "PRIMARY KEY (`submerkmalID`) )"
                );

        mySQLCreateTables.doUpdate("CREATE TABLE IF NOT EXISTS `projekte` "
                + "(`projektID` int(10) unsigned NOT NULL DEFAULT 1, "
                + "`projektName` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL, "
                + "PRIMARY KEY (`projektID`) )"
                );

        mySQLCreateTables.doUpdate("CREATE TABLE IF NOT EXISTS `tickets` "
                + "(`ticketID` int(10) unsigned NOT NULL AUTO_INCREMENT, "
                + "`ticketTitel` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL, "
                + "`ticketBeschreibung` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL, "
                + "`ticketStart` datetime DEFAULT NULL, "
                + "`ticketEnde` datetime DEFAULT NULL, "
                + "`ticketTimeStamp` datetime DEFAULT NULL, "
                + "`ticketDelete` tinyint unsigned DEFAULT NULL, "
                + "`userID` int(10) unsigned NOT NULL, "
                + "`projektID` int(10) unsigned NOT NULL, "
                + "`statusID` int(10) unsigned NOT NULL, "
                + "`prioritaetID` int(10) unsigned NOT NULL, "
                + "PRIMARY KEY (`ticketID`), "
                + "KEY `USERID-FK` (`userID`), "
                + "KEY `PROJEKTID-FK` (`projektID`), "
                + "KEY `STATUSID-FK` (`statusID`), "
                + "KEY `PRIORITAET-FK` (`prioritaetID`), "
                + "CONSTRAINT `USERID-FK` FOREIGN KEY (`userID`) REFERENCES `users` (`userID`), "
                + "CONSTRAINT `PROJEKTID-FK` FOREIGN KEY (`projektID`) REFERENCES `projekte` (`projektID`), "
                + "CONSTRAINT `STATUSID-FK` FOREIGN KEY (`statusID`) REFERENCES `status` (`statusID`), "
                + "CONSTRAINT `PRIORITAETID-FK` FOREIGN KEY (`prioritaetID`) REFERENCES `prioritaeten` (`prioritaetID`) )"
                );

        mySQLCreateTables.doUpdate("CREATE TABLE IF NOT EXISTS `zuordnungmerkmaletickets` "
                + "(`merkmalID` int(10) unsigned NOT NULL, "
                + "`submerkmalID` int(10) unsigned NOT NULL, "
                + "`ticketID` int(10) unsigned NOT NULL, "
                + "KEY `MERKMALID-FK` (`merkmalID`), "
                + "KEY `SUBMERKMALID-FK` (`submerkmalID`), "
                + "KEY `TICKETID-FK` (`ticketID`), "
                + "CONSTRAINT `MERKMALID-FK` FOREIGN KEY (`merkmalID`) REFERENCES `merkmale` (`merkmalID`), "
                + "CONSTRAINT `SUBMERKMALID-FK` FOREIGN KEY (`submerkmalID`) REFERENCES `submerkmale` (`submerkmalID`), "
                + "CONSTRAINT `TICKETID-FK` FOREIGN KEY (`ticketID`) REFERENCES `tickets` (`ticketID`) )"
                );

        mySQLCreateTables.doUpdate("CREATE TABLE IF NOT EXISTS `kategorien` "
                + "(`kategorieID` int(10) unsigned NOT NULL AUTO_INCREMENT, "
                + "`kategorieName` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL, "
                + "PRIMARY KEY (`kategorieID`) )"
                );

        mySQLCreateTables.doUpdate("CREATE TABLE IF NOT EXISTS `subkategorien` "
                + "(`subkategorieID` int(10) unsigned NOT NULL AUTO_INCREMENT, "
                + "`subkategorieName` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL, "
                + "PRIMARY KEY (`subkategorieID`) )"
                );

        mySQLCreateTables.doUpdate("CREATE TABLE IF NOT EXISTS `fotos` "
                + "(`fotoID` int(10) unsigned NOT NULL AUTO_INCREMENT, "
                + "`fotoImage` longblob DEFAULT NULL, "
                + "`fotoLink` varchar(255) DEFAULT NULL, "
                + "PRIMARY KEY (`fotoID`) )"
                );

        mySQLCreateTables.doUpdate("CREATE TABLE IF NOT EXISTS `zuordnungfotostickets` "
                + "(`ticketID` int(10) unsigned NOT NULL, "
                + "`fotoID` int(10) unsigned NOT NULL, "
                + "KEY `FOTOFID-FK` (`fotoID`), "
                + "KEY `TICKETFID-FK` (`ticketID`), "
                + "CONSTRAINT `FOTOFID-FK` FOREIGN KEY (`fotoID`) REFERENCES `fotos` (`fotoID`), "
                + "CONSTRAINT `TICKETFID-FK` FOREIGN KEY (`ticketID`) REFERENCES `tickets` (`ticketID`) )"
                );

        mySQLCreateTables.doUpdate("CREATE TABLE IF NOT EXISTS `zuordnungkategorientickets` "
                + "(`kategorieID` int(10) unsigned NOT NULL, "
                + "`subkategorieID` int(10) unsigned NOT NULL, "
                + "`ticketID` int(10) unsigned NOT NULL, "
                + "KEY `KATEGORIETID-FK` (`kategorieID`), "
                + "KEY `SUBKATEGORIETID-FK` (`subkategorieID`), "
                + "KEY `TICKETTID-FK` (`ticketID`), "
                + "CONSTRAINT `KATEGORIETID-FK` FOREIGN KEY (`kategorieID`) REFERENCES `kategorien` (`kategorieID`), "
                + "CONSTRAINT `SUBKATEGORIETID-FK` FOREIGN KEY (`subkategorieID`) REFERENCES `subkategorien` (`subkategorieID`), "
                + "CONSTRAINT `TICKETTID-FK` FOREIGN KEY (`ticketID`) REFERENCES `tickets` (`ticketID`) )"
                );

        mySQLCreateTables.doUpdate("CREATE TABLE IF NOT EXISTS `zuordnungkategorienusers` "
                + "(`kategorieID` int(10) unsigned NOT NULL, "
                + "`subkategorieID` int(10) unsigned NOT NULL, "
                + "`userID` int(10) unsigned NOT NULL, "
                + "KEY `KATEGORIEUID-FK` (`kategorieID`), "
                + "KEY `SUBKATEGORIEUID-FK` (`subkategorieID`), "
                + "KEY `USERUID-FK` (`userID`), "
                + "CONSTRAINT `KATEGORIEUID-FK` FOREIGN KEY (`kategorieID`) REFERENCES `kategorien` (`kategorieID`), "
                + "CONSTRAINT `SUBKATEGORIEUID-FK` FOREIGN KEY (`subkategorieID`) REFERENCES `subkategorien` (`subkategorieID`), "
                + "CONSTRAINT `USERUID-FK` FOREIGN KEY (`userID`) REFERENCES `users` (`userID`) )"
                );

        mySQLCreateTables.doUpdate("CREATE TABLE IF NOT EXISTS `antworten` "
                + "(`antwortID` int(10) unsigned NOT NULL AUTO_INCREMENT, "
                + "`antwortTitel` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL, "
                + "`antwortBeschreibung` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL, "
                + "`antwortTimeStamp` datetime DEFAULT NULL, "
                + "`antwortDelete` tinyint unsigned DEFAULT NULL, "
                + "`userID` int(10) unsigned NOT NULL, "
                + "`ticketID` int(10) unsigned NOT NULL, "
                + "`antwortHilfreich` tinyint unsigned DEFAULT NULL, "
                + "PRIMARY KEY (`antwortID`), "
                + "KEY `USERIDA-FK` (`userID`), "
                + "KEY `TICKETAID-FK` (`ticketID`), "
                + "CONSTRAINT `USERIDA-FK` FOREIGN KEY (`userID`) REFERENCES `users` (`userID`), "
                + "CONSTRAINT `TICKETIDA-FK` FOREIGN KEY (`ticketID`) REFERENCES `tickets` (`ticketID`) )"
                );
        
        mySQLCreateTables.disconnect();
        mySQLCreateTables = null;
    }

}
