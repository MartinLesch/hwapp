/*
 * (C) VIF3 - Email: info@bbs2-leer.de
 * Diese Software kann frei genutzt werden. Auch gegen die Bearbeitung und
 * Aenderung des Quellcodes spricht nichts. Wer Verbesserungen vornimmt oder  
 * Fehler findet: Wir freuen uns ueber Feedback! Gerne auch in Form von Kritik.
 * Bitte diesen Vermerk nicht loeschen!
 */
package hwapp;
    
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Martin Lesch
 * @version 12.06.2018
 */
public class DataHandler {
    private String host;
    private int port;
    private String databaseName;
    private String user;
    private String password;
    private hwapp.Logger logger;
    private MySQL mySQL;
    private hwapp.Tools tools;

  /**
    * @param logger Es ist zwingend ein Logger erforderlich
    * @param host HOST-IP oder gueltiger Verbindungsname (eg. localhost) zum MySQL-Server
    * @param port Kommunikationsport fuer Verbindung zum MySQL-Server (eg. 3306)
    * @param databaseName Name der vorhandenen Datenbank fuer Verwaltung der hwapp-Daten
    * @param user Benutzername fuer den Login bei der MySQL Datenbank
    * @param password Passwort fuer den Login bei der MySQL Datenbank
    */
    public DataHandler( hwapp.Logger logger, 
                        String host, 
                        int port, 
                        String databaseName, 
                        String user, 
                        String password         )
    {
        this.logger = logger;
        this.host = host;
        if (port < 1) {
            this.logger.addToLogFile(this.getClass().getSimpleName(),"Ungueltiger Port? " + this.port, LogLevel.LoggingLevel.WARNING);
        }
        this.port = port;
        this.databaseName = databaseName;        
        this.user = user;
        this.password = password;
        
        this.mySQL = new MySQL(this.logger, this.host, this.port, this.databaseName, this.user, this.password);
        this.logger.addToLogFile(this.getClass().getSimpleName(), "Daten Handler wurde initialisiert. Host: " + this.host +" - Datenbank: " + this.databaseName + " - Benutzer: " + this.user, LogLevel.LoggingLevel.DEBUG);
        this.tools = new Tools(this.logger);
        
    }

  /**
    * @param userLogin Login-Name des Benutzers (eg. Email-Adresse)
    * @param userName Nachname des Benutzers
    * @param userVorname Vorname des Benutzers
    * @param userPassword Password fuer Login in MySQL Datenbank fuer den Benutzer
    * @param userDarfMMC Darf der Benutzer die ManagementKonsole starten? 0 = nein, 1 = ja
    * @return gibt true zurueck, wenn der Benutzer erfolgreich angelegt wurde
    */
    public boolean setNewUser(String userLogin, String userName, String userVorname, String userPassword, int userDarfMMC){
        boolean feedback = true;
        this.logger.addToLogFile(this.getClass().getSimpleName(), "Neuen Benutzer anlegen ... User: " + userLogin , LogLevel.LoggingLevel.DEBUG);
        if (userDarfMMC >1 || userDarfMMC <0){
            this.logger.addToLogFile(this.getClass().getSimpleName(), "Neuer Benutzer nicht angelegt ... DarfMMC falscher Wert: " + userDarfMMC , LogLevel.LoggingLevel.WARNING);
            feedback = false;
        }
        if (feedback){
          this.mySQL.connect();
            if(this.mySQL.isConnected()){
                if (!this.getUserExists(userLogin)) {
                    this.mySQL.doUpdate("INSERT INTO users VALUES (NULL, '" + userLogin + "', '" + userName + "', '" + userVorname + "', '" + userPassword +"', " + userDarfMMC +")");
                    this.setUserGrantPrivileges(userLogin, userPassword);
                    this.mySQL.disconnect();
                } else {
                    this.logger.addToLogFile(this.getClass().getSimpleName(), "Neuer Benutzer nicht angelegt ... Ein Benutzer mit diesem Login existiert bereits!", LogLevel.LoggingLevel.WARNING);
                    feedback = false;
                }
            } else {
                this.logger.addToLogFile(this.getClass().getSimpleName(), "Neuer Benutzer nicht angelegt ... Konnektion zur Datenbank nicht gelungen.", LogLevel.LoggingLevel.ERROR);
                feedback = false;
            }
        }
        return feedback;
    }

  /**
    * @return Es werden alle Datensaetze der Tabelle users in einem ResultSet zuruck gegeben.
    */
    public ResultSet getAllUser(){
        ResultSet rs = null;
        this.logger.addToLogFile(this.getClass().getSimpleName(), "Lese alle Datensaetze der Tabelle users ..." , LogLevel.LoggingLevel.DEBUG);
        this.mySQL.connect();
            if(this.mySQL.isConnected()){
                rs = this.mySQL.doQuery("SELECT * FROM users");
                this.mySQL.disconnect();
            } else {
                this.logger.addToLogFile(this.getClass().getSimpleName(), "Lesen aller Datensaetze user nicht moeglich ... Konnektion zur Datenbank nicht gelungen.", LogLevel.LoggingLevel.ERROR);
            }
        return rs;
    }

    /**
    * @return Es werden alle Datensaetze der Tabelle users in einem StringArray zuruck gegeben.
    */
    public String[][] getAllUserAsStringArray(){
        String [][] resultString = null;
        this.logger.addToLogFile(this.getClass().getSimpleName(), "Lese alle Datensaetze der Tabelle users ... und gebe Sie als StringArray zurueck." , LogLevel.LoggingLevel.DEBUG);
        this.mySQL.connect();
            if(this.mySQL.isConnected()){
                resultString = this.mySQL.changeResultSetToStringArray(this.mySQL.doQuery("SELECT * FROM users"));
                this.mySQL.disconnect();
            } else {
                this.logger.addToLogFile(this.getClass().getSimpleName(), "Lesen aller Datensaetze user nicht moeglich ... Konnektion zur Datenbank nicht gelungen.", LogLevel.LoggingLevel.ERROR);
            }
        return resultString;
    }
    
   /**
    * @param userLogin Login-Name des Benutzers (eg. Email-Adresse)
    * @return gibt true zurueck, wenn mindestens ein Benutzer mit dem Login-Namen existiert
    */
    private boolean getUserExists(String userLogin){
        boolean feedback = false;
        int counter = 0;
        try {
            ResultSet rs = null;
            //this.logger.addToLogFile(this.getClass().getSimpleName(), "Pruefen ob Benutzer vorhanden.", LogLevel.LoggingLevel.DEBUG);
            rs = this.mySQL.doQuery("SELECT COUNT(*) as count FROM users WHERE userLogin = '" + userLogin +"'");
            
            while (rs.next()) {
                counter = rs.getInt("count");
                if (counter >= 1) {
                    feedback = true;
                    //this.logger.addToLogFile(this.getClass().getSimpleName(), "Benutzer existiert! (nicht angelegt) " + counter, LogLevel.LoggingLevel.DEBUG);
                } else {
                    feedback = false;
                    //this.logger.addToLogFile(this.getClass().getSimpleName(), "Benutzer neu (wird angelegt) ... " , LogLevel.LoggingLevel.DEBUG);
                }
            }   
        } catch (SQLException ex) {
            this.logger.addToLogFile(this.getClass().getSimpleName(), "Benutzer pruefen, Fehler in Abfrage (ResultSet)! ", LogLevel.LoggingLevel.ERROR);
        }
        return feedback;
    }
  
    /**
    * @param userLogin Login-Name des Benutzers (eg. Email-Adresse)
    * @param userPassword Password fuer Login in MySQL Datenbank fuer den Benutzer
    */
    private void setUserGrantPrivileges(String userLogin, String userPassword){
        this.mySQL.doUpdate("CREATE USER "
                            + "'" + userLogin + "'@'%' "
                            //+ "IDENTIFIED VIA mysql_native_password USING " // hash-41 via SELECT PASSWORD("password")
                            + "IDENTIFIED BY " //Clear-text-passwort
                            + "'" + userPassword + "'"
                            + ";");
        this.mySQL.doUpdate("GRANT USAGE ON *.* TO "
                            + "'" + userLogin + "'@'%' "
                            + "REQUIRE NONE WITH MAX_QUERIES_PER_HOUR 0 MAX_CONNECTIONS_PER_HOUR 0 MAX_UPDATES_PER_HOUR 0 MAX_USER_CONNECTIONS 0"
                            + ";");
        this.mySQL.doUpdate("GRANT ALL PRIVILEGES ON  "
                            + this.databaseName + ".* TO "
                            + "'" + userLogin + "'@'%'"
                            + " WITH GRANT OPTION;");
    }

    /**
    * @param userID Login-Name des Benutzers (eg. Email-Adresse)
    * @return Gibt alle devices fuer einen User als Sring Array zurueck
    */
    public String[][] getAllDevicesForThisUserAsStringArray(int userID) {
        String [][] resultString = null;
        this.logger.addToLogFile(this.getClass().getSimpleName(), "Lese devices Datensaetze fuer user = " + userID + " ... und gebe Sie als StringArray zurueck." , LogLevel.LoggingLevel.DEBUG);
        this.mySQL.connect();
            if(this.mySQL.isConnected()){
                resultString = this.mySQL.changeResultSetToStringArray(this.mySQL.doQuery("SELECT * FROM devices WHERE deviceUserID = " + userID));
                this.mySQL.disconnect();
            } else {
                this.logger.addToLogFile(this.getClass().getSimpleName(), "Lesen in devices (UserID) nicht moeglich ... Verbindung zur Datenbank nicht gelungen.", LogLevel.LoggingLevel.ERROR);
            }
        return resultString;
    }

    /**
    * @param IMEI IMEI des Mobilgeraetes
    * @return Gibt den UserID zurueck der zu dieser IMEI gehoert
    */
    public int getUserForThisIMEI(String IMEI) {
        String resultString [][] = null;
        int resultInt;
        this.logger.addToLogFile(this.getClass().getSimpleName(), "Lese IMEI = " + IMEI + " ... und gebe den UserID zurueck." , LogLevel.LoggingLevel.DEBUG);
        this.mySQL.connect();
            if(this.mySQL.isConnected()){
                resultString = this.mySQL.changeResultSetToStringArray(this.mySQL.doQuery("SELECT deviceUserId FROM devices WHERE deviceIMEI = " + IMEI));
               this.mySQL.disconnect();
            } else {
                this.logger.addToLogFile(this.getClass().getSimpleName(), "Lesen in devices (IMEI) nicht moeglich ... Verbindung zur Datenbank nicht gelungen.", LogLevel.LoggingLevel.ERROR);
            }
        resultInt = tools.tryParseInt(resultString[0][0], "getUserForThisIMEI");
        return resultInt;
    }
    
    /**
    * @param ticketTitel Titel des Tickets (max. 20)
    * @param ticketBeschreibung Beschreibung des Tickets (max. 256)
    * @param ticketStart Startdatum als String mit Inhalt Date
    * @param ticketEnde Endedatum als String mit Inhalt Date
    * @param userID ID fuer die Tabelle User (>0)
    * @param projektID ID fuer die Tabelle Projekte
    * @param statusID ID fuer die Tabelle Statie
    * @param prioritaetID ID fuer die Tabelle Prioritaeten
    * @return gibt true zurueck, wenn das Ticket erfolgreich angelegt wurde
    */
    public boolean setNewTicket(String ticketTitel, String ticketBeschreibung, String ticketStart, String ticketEnde, int userID, int projektID, int statusID, int prioritaetID){
        boolean feedback = true;

        this.logger.addToLogFile(this.getClass().getSimpleName(), "Neues Ticket anlegen ... Titel: " + ticketTitel , LogLevel.LoggingLevel.DEBUG);
        if (userID <1 || projektID <1 || prioritaetID <1 || statusID <1){
            this.logger.addToLogFile(this.getClass().getSimpleName(), "Neues Ticket nicht angelegt ... Es fehlen Angaben. ", LogLevel.LoggingLevel.WARNING);
            feedback = false;
        }
        if (feedback){
          this.mySQL.connect();
            if(this.mySQL.isConnected()){
                    this.mySQL.doUpdate("INSERT INTO tickets VALUES (NULL, '" + ticketTitel + "', '" + ticketBeschreibung + "', '" + tools.tryParseDateYyyyMMddToDateTime(ticketStart, "setNewTicket-Start") + "', '" + tools.tryParseDateYyyyMMddToDateTime(ticketEnde,"setNewTicket-Ende") +"', " + tools.getTimeStamp("setNewTicket-TimeStamp") + ", " + 0 + ", " + userID + ", " + projektID + ", " + statusID + ", " + prioritaetID + ")");
                    this.mySQL.disconnect();
                }  else {
                this.logger.addToLogFile(this.getClass().getSimpleName(), "Neues Ticket nicht angelegt ... Konnektion zur Datenbank nicht gelungen.", LogLevel.LoggingLevel.ERROR);
                feedback = false;
            }
        }
        return feedback;
    }

    /**
    * @param antwortTitel Titel der Antwort (max. 20)
    * @param antwortBeschreibung Beschreibung der Antwort (max. 256)
    * @param userID ID fuer die Tabelle User (>0)
    * @param ticketID ID fuer die Tabelle Tickets
    * @return gibt true zurueck, wenn die Antwort erfolgreich angelegt wurde
    */
    public boolean setNewTicket(String antwortTitel, String antwortBeschreibung, int userID, int ticketID){
        boolean feedback = true;
        this.logger.addToLogFile(this.getClass().getSimpleName(), "Neue Antwort anlegen ... Titel: " + antwortTitel , LogLevel.LoggingLevel.DEBUG);
        if (userID <1 || ticketID <1){
            this.logger.addToLogFile(this.getClass().getSimpleName(), "Neue Antwort nicht angelegt ... Es fehlen Angaben. ", LogLevel.LoggingLevel.WARNING);
            feedback = false;
        }
        if (feedback){
          this.mySQL.connect();
            if(this.mySQL.isConnected()){
                    this.mySQL.doUpdate("INSERT INTO antworten VALUES (NULL, '" + antwortTitel + "', '" + antwortBeschreibung + "', '" + tools.getTimeStamp("setNewAnswer-TimeStamp") + ", " + 0 + ", " + userID + ", " + ticketID + ", " + 0 + ")");
                    this.mySQL.disconnect();
                }  else {
                this.logger.addToLogFile(this.getClass().getSimpleName(), "NeueAntwort nicht angelegt ... Konnektion zur Datenbank nicht gelungen.", LogLevel.LoggingLevel.ERROR);
                feedback = false;
            }
        }
        return feedback;
    }
    
    /**
    * @param userID userID des zu loeschenden Users
    * @return gibt true zurueck, wenn der Benutzer geloescht wurde
    */
    private boolean deleteUser(int userID){
        boolean feedback = false;
        //Pruefungen fehlen noch
        this.mySQL.connect();
        this.mySQL.doUpdate("delete FROM users WHERE userID = '" + userID +"'");
        this.mySQL.disconnect();
        //this.logger.addToLogFile(this.getClass().getSimpleName(), "Benutzer loeschen, Fehler in Abfrage! ", LogLevel.LoggingLevel.ERROR);
        feedback = true;
        return feedback;
    }
    /**
    * @param answerID ID der zu loeschenden Antwort
    * @return gibt true zurueck, wenn die Antwort geloescht wurde
    */
    private boolean deleteAnswer(int answerID){
        boolean feedback = false;
        //Pruefungen fehlen noch
        this.mySQL.connect();
        this.mySQL.doUpdate("delete FROM antworten WHERE antwortID = '" + answerID +"'");
        this.mySQL.disconnect();
        //this.logger.addToLogFile(this.getClass().getSimpleName(), "Antwort loeschen, Fehler in Abfrage! ", LogLevel.LoggingLevel.ERROR);
        feedback = true;
        return feedback;
    }
    /**
    * @param ticketID ID des zu loeschenden Tickets
    * @return gibt true zurueck, wenn das Ticket geloescht wurde
    */
    private boolean deleteTicket(int ticketID){
        boolean feedback = false;
        //Pruefungen fehlen noch
        this.mySQL.connect();
        this.mySQL.doUpdate("delete FROM tickets WHERE ticketID = '" + ticketID +"'");
        this.mySQL.disconnect();
        //this.logger.addToLogFile(this.getClass().getSimpleName(), "Ticket loeschen, Fehler in Abfrage! ", LogLevel.LoggingLevel.ERROR);
        feedback = true;
        return feedback;
    }
    /**
    * @return Es werden alle Datensaetze der Tabelle tickets in einem ResultSet zuruck gegeben.
    */
    public ResultSet getAllTickets(){
        ResultSet rs = null;
        this.logger.addToLogFile(this.getClass().getSimpleName(), "Lese alle Datensaetze der Tabelle tickets ..." , LogLevel.LoggingLevel.DEBUG);
        this.mySQL.connect();
            if(this.mySQL.isConnected()){
                rs = this.mySQL.doQuery("SELECT * FROM tickets");
                this.mySQL.disconnect();
            } else {
                this.logger.addToLogFile(this.getClass().getSimpleName(), "Lesen aller Datensaetze tickets nicht moeglich ... Konnektion zur Datenbank nicht gelungen.", LogLevel.LoggingLevel.ERROR);
            }
        return rs;
    }

}
