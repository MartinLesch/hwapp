/*
 * Klasse MySQL fuer Verbindung und Kommunikation mit DB.
 */
package hwapp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Martin Lesch
 * @version 14.03.2018
 */
public class MySQL {

    private String host;
    private int port;
    private String database;
    private String username;
    private String password;
    private Connection connection;
    private Statement stmt = null;
    private int status;
    private hwapp.Logger logger;
    private Savepoint savepoint;

    public MySQL(Logger logger, String host, int port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.status = 0;
        this.logger = logger;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }
    
    public void setAutoCommit(boolean autoCommit) {
        try {
            this.connection.setAutoCommit(autoCommit);
        } catch (SQLException ex) {
            logger.addToLogFile(this.getClass().getSimpleName(), "Kann Automatischen Commit nicht auf den gewuenschten Wert setzen: " + autoCommit + " - " + ex, LogLevel.LoggingLevel.WARNING);
        }
    }
    public void commit(){
        try {
            connection.commit();
        } catch (SQLException ex) {
            logger.addToLogFile(this.getClass().getSimpleName(), "Fehler bei Commit. " + ex, LogLevel.LoggingLevel.WARNING);
        }
    }

    public void setSavepoint(String savepointName) {
        try {
            this.savepoint = this.connection.setSavepoint(savepointName);
        } catch (SQLException ex) {
            logger.addToLogFile(this.getClass().getSimpleName(), "Kann Savepoint nicht setzen: " + savepointName + " - " + ex, LogLevel.LoggingLevel.WARNING);
        }
    }
    
    public void rollback() {
        try {
            this.connection.rollback();
        } catch (SQLException ex) {
            logger.addToLogFile(this.getClass().getSimpleName(), "Rueckgaengig machen der letzten Transaktion nicht moeglich. " + ex, LogLevel.LoggingLevel.WARNING);
        }
    }
    
    public void rollback(String savepointName) {
        try {
          this.connection.rollback(this.savepoint);
        } catch (SQLException ex) {
            logger.addToLogFile(this.getClass().getSimpleName(), "Rueckgaengig machen der Transaktion bis Savepoint nicht moeglich."  + ex, LogLevel.LoggingLevel.WARNING);
        }
    }

    public void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + host + ":" + port + "/" + database + "?" + "user=" + username + "&" + "password=" + password);
            logger.addToLogFile(this.getClass().getSimpleName(), "Verbindung zu MySQL hergestellt." , LogLevel.LoggingLevel.INFO);
        } catch (SQLException ex) {
            logger.addToLogFile(this.getClass().getSimpleName(), "Fehler in Aufbau der Verbindung zu MySQL. " + ex, LogLevel.LoggingLevel.ERROR);
            //ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            logger.addToLogFile(this.getClass().getSimpleName(), "Fehler MySQL Treiber nicht gefunden. " + ex, LogLevel.LoggingLevel.ERROR);
            //ex.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (!this.connection.getAutoCommit()) {
                this.connection.commit();
            }
            connection.close();
            logger.addToLogFile(this.getClass().getSimpleName(), "Verbindung zu MySQL getrennt." , LogLevel.LoggingLevel.INFO);
        } catch (SQLException ex) {
            logger.addToLogFile(this.getClass().getSimpleName(), "Fehler beim trennen der Verbindung zu MySQL oder Commit." , LogLevel.LoggingLevel.ERROR);
            //ex.printStackTrace();
        }
    }

    public boolean isConnected() {
        try {
            return connection == null || connection.isClosed() ? false : true;
        } catch (SQLException ex) {
            logger.addToLogFile(this.getClass().getSimpleName(), "Fehler bei Pruefung auf bestehende Verbindung zu MySQL." , LogLevel.LoggingLevel.ERROR);
            //ex.printStackTrace();
        }
        return false;
    }

    public Connection getConnection() {
        return connection;
    }

    public void createDatabase(String database) {
        this.database = database;
        try {
            this.stmt = connection.createStatement();
            String sql = "CREATE DATABASE " + database;
            stmt.executeUpdate(sql);
            logger.addToLogFile(this.getClass().getSimpleName(), "Datenbank: " + this.database +  " wurde erfolgreich angelegt.", LogLevel.LoggingLevel.INFO);
        } catch (SQLException ex) {
            logger.addToLogFile(this.getClass().getSimpleName(), "Fehler bei Anlage der Datenbank: " + database, LogLevel.LoggingLevel.ERROR);
        } finally {
            //finally um die genutzeten resourccen zu schliessen
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                logger.addToLogFile(this.getClass().getSimpleName(), "Fehler beim beenden der Verbindung nach Anlage der Datenbank: " + database, LogLevel.LoggingLevel.WARNING);
                System.out.println("Fehler in createDatabase stmt->close");
            }// da kann man nichts tun
            /*try{
                if(connection!=null)
                 connection.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try */
        }
    }

    public void deleteAllRows(String tblName) {
        try {
            this.stmt = connection.createStatement();
        } catch (SQLException ex) {
            logger.addToLogFile(this.getClass().getSimpleName(), "Fehler beim Versuch ein Loeschen-Statement vorzubereiten. " , LogLevel.LoggingLevel.ERROR);
        }
        String sql = "DELETE FROM " + tblName;
        try {
            stmt.executeUpdate(sql);
            logger.addToLogFile(this.getClass().getSimpleName(), "Tabelleninhalt: " + tblName + " wurde geloescht." , LogLevel.LoggingLevel.INFO);
        } catch (SQLException ex) {
            logger.addToLogFile(this.getClass().getSimpleName(), "Fehler beim loeschen des Tabelleninhaltes. " + ex , LogLevel.LoggingLevel.ERROR);
        } finally {
            //finally um die genutzeten resourccen zu schliessen
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                logger.addToLogFile(this.getClass().getSimpleName(), "Fehler beim schliessen der Abfrage zum loeschen der Daten. " , LogLevel.LoggingLevel.ERROR);
            }
        }

    }
    
    public void deleteDatabase() {
        try {
            this.stmt = connection.createStatement();
        } catch (SQLException ex) {
            logger.addToLogFile(this.getClass().getSimpleName(), "Fehler beim Versuch ein Loeschen-Statement vorzubereiten. " , LogLevel.LoggingLevel.ERROR);
        }
        String sql = "DROP DATABASE " + database;
        try {
            stmt.executeUpdate(sql);
            logger.addToLogFile(this.getClass().getSimpleName(), "Datenbank: " + this.database + " wurde geloescht." , LogLevel.LoggingLevel.INFO);
        } catch (SQLException ex) {
            logger.addToLogFile(this.getClass().getSimpleName(), "Fehler beim loeschen der Datenbank. " , LogLevel.LoggingLevel.ERROR);
        } finally {
            //finally um die genutzeten resourccen zu schliessen
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                logger.addToLogFile(this.getClass().getSimpleName(), "Fehler beim schliessen der Abfrage zum loeschen der Datenbank. " , LogLevel.LoggingLevel.ERROR);
            }// da kann man nichts tun
            /*try{
                if(connection!=null)
                 connection.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try */
        }

    }

    public ResultSet doQuery(String query, Object... args) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(query);
            for (int i = 1; i <= args.length; i++) {
                preparedStatement.setObject(i, args[i - 1]);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet;
        } catch (SQLException ex) {
            logger.addToLogFile(this.getClass().getSimpleName(), "Fehler bei der Ausfuehrung einer Abfrage. " , LogLevel.LoggingLevel.WARNING);
            //ex.printStackTrace();
        }
        return null;
    }

    public void doUpdate(String query, Object... args) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(query);
            for (int i = 1; i <= args.length; i++) {
                preparedStatement.setObject(i, args[i - 1]);
            }

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException ex) {
            logger.addToLogFile(this.getClass().getSimpleName(), "Fehler bei der Ausfuehrung einer AenderungsAbfrage. " + ex , LogLevel.LoggingLevel.WARNING);
            //ex.printStackTrace();
        }
    }

    public String[][] changeResultSetToStringArray(ResultSet rs) {
        //erwartet ein ResultSet und gibt ein 2-dimensionales String Array zurueck
        ResultSetMetaData rsmd = null;
        int columnCount = this.getRowsInResultSet(rs);
        String[][] rowData = null;

        List rows = new ArrayList();
        try {
            while (rs.next()) {
                String[] row = new String[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getString(i);
                }
                rows.add(row);
            }
        } catch (SQLException ex) {
            logger.addToLogFile(this.getClass().getSimpleName(), "Fehler beim lesen des naechsten Datensatzes (cRSTSA)." , LogLevel.LoggingLevel.WARNING);
            //ex.printStackTrace();
        }
        try {
            rs.getStatement().close();
        } catch (SQLException ex) {
            logger.addToLogFile(this.getClass().getSimpleName(), "Fehler beim schliessen (cRSTSA). " , LogLevel.LoggingLevel.WARNING);
            //ex.printStackTrace();
        }
        rowData = (String[][]) rows.toArray(new String[rows.size()][columnCount]);
        return rowData;
    }

    public int getRowsInResultSet(ResultSet rs) {
        //erwartet ein ResultSet und gibt ein int (Anzahl Zeilen) zurueck
        ResultSetMetaData rsmd = null;
        int columnCount = 0;
        try {
            rsmd = rs.getMetaData();
        } catch (SQLException ex) {
            logger.addToLogFile(this.getClass().getSimpleName(), "Fehler beim holen von MetaDaten. " , LogLevel.LoggingLevel.WARNING);
            //ex.printStackTrace();
        }
        try {
            columnCount = rsmd.getColumnCount();
        } catch (SQLException ex) {
            logger.addToLogFile(this.getClass().getSimpleName(), "Fehler beim holen von Zeileneigenschaften. " , LogLevel.LoggingLevel.WARNING);
            //ex.printStackTrace();
        }
        return columnCount;
    }

    public Object[][] changeResultSetToObjectArray(ResultSet rs) {
        //erwartet ein ResultSet und gibt ein 2-dimensionales Object Array zurueck 
        ResultSetMetaData rsmd = null;
        int columnCount = this.getRowsInResultSet(rs);
        Object[][] rowObject = null;

        /*        try {
            rsmd = rs.getMetaData();
        } catch (SQLException ex) {
            System.out.println("Fehler getMetaData");
            ex.printStackTrace();
        }
        try {
            columnCount = rsmd.getColumnCount();
        } catch (SQLException ex) {
            System.out.println("Fehler getColumnCount");
            ex.printStackTrace();
        }
         */
        List rows = new ArrayList();
        try {
            while (rs.next()) {
                String[] row = new String[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getString(i);
                }
                rows.add(row);
            }
        } catch (SQLException ex) {
            logger.addToLogFile(this.getClass().getSimpleName(), "Fehler beim lesen des naechsten Datensatzes. (cRSTOA)" , LogLevel.LoggingLevel.WARNING);
            //ex.printStackTrace();
        }
        try {
            rs.getStatement().close();
        } catch (SQLException ex) {
            logger.addToLogFile(this.getClass().getSimpleName(), "Fehler beim schliessen. (cRSTOA)" , LogLevel.LoggingLevel.WARNING);
            //ex.printStackTrace();
        }
        rowObject = (String[][]) rows.toArray(new String[rows.size()][columnCount]);
        return rowObject;
    }

    public TableModel resultSetToTableModel(ResultSet rs) {
//    JTable t = new JTable(??.resultSetToTableModel(rs));
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            int numberOfColumns = metaData.getColumnCount();
            Vector columnNames = new Vector();

            // Get the column names
            for (int column = 0; column < numberOfColumns; column++) {
                columnNames.addElement(metaData.getColumnLabel(column + 1));
            }

            // Get all rows.
            Vector rows = new Vector();

            while (rs.next()) {
                Vector newRow = new Vector();

                for (int i = 1; i <= numberOfColumns; i++) {
                    newRow.addElement(rs.getObject(i));
                }

                rows.addElement(newRow);
            }

            return new DefaultTableModel(rows, columnNames);
        } catch (Exception e) {
            logger.addToLogFile(this.getClass().getSimpleName(), "Fehler beim aufbereiten eines Tabellenmodells." , LogLevel.LoggingLevel.WARNING);
            //e.printStackTrace();

            return null;
        }
    }

}
