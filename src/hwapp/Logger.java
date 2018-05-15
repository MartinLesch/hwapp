/*
 * Logger von Martin Lesch (martin@familie-lesch.de) in Java
 * Benoetigt die Klasse LogLevel (enum fuer Logging - Level)
 * Ausgabe erfolgt auf Konsole, in GUI, in JPaneDialog und txt-Datei
 * Benutzung ist frei. Ergaenzungen und Aenderungen sind erlaubt.
 * Bitte diese Notizen nicht loeschen!
 */
package hwapp;

import hwapp.LogLevel.LoggingLevel;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author Martin Lesch
 * @version 24.02.2018
 */
public class Logger  {

    public File file;
    private FileWriter fw;
    private String fileName;
    private String filePath;
    private LoggingLevel logLevel;       // 0=debug, ... 5=fatal
    private LoggingLevel msgLevel;
    private String msgClass;
    private String msgMessage;
    private boolean withLogFile;
    private boolean appendLogFile;
    private boolean withTimeStamp;
    private boolean showGuiDialog;
    private boolean showConsoleOutput;
    private boolean stopRunIfError;
    private JLabel lblApp;

/**
 * 
 * @param logLevel LoggingLevel ab dem Nachrichten ausgegeben werden sollen ... (import LogLevel.java) DEBUG/INFO/WARNING/ERROR/FATAL
 * @param withLogFile Wenn TRUE wird eine Logging-Datei (Text) im Arbeitsverzeichnis erstellt
 * @param appendLogFile Wenn TRUE wird die Logging-Datei ergaenzt, sonst neu erstellt und damit die evtl. vorhandene Datei ueberschrieben
 * @param withTimeStamp Wenn TRUE wird die Nachricht um eine Zeitangabe ergaenzt
 * @param showGuiDialog Wenn TRUE wird die Nachricht in einem Pop-Up-Fenster angezeigt
 * @param showConsoleOutput Wenn TRUE wird die Nachricht in der Konsole ausgegeben
 * @param stopRunIfError Wenn TRUE wird die Anwendung bei einem ERROR oder FATAL beendet (!KILL!)
 */
    public Logger(LoggingLevel logLevel, boolean withLogFile, boolean appendLogFile, boolean withTimeStamp, boolean showGuiDialog, boolean showConsoleOutput, boolean stopRunIfError) {
        this.logLevel = logLevel;
        this.withLogFile = withLogFile;
        this.appendLogFile = appendLogFile;
        this.withTimeStamp = withTimeStamp;
        this.showConsoleOutput = showConsoleOutput;
        this.showGuiDialog = showGuiDialog;
        this.stopRunIfError = stopRunIfError;
        this.lblApp = null;
        this.fileName = "log_" + System.getProperty("user.name") + "_" + this.getSystemDate() + ".txt";
        this.filePath = System.getProperty("user.dir");
        //System.out.println(filePath + fileName);
        //file = new File(filePath + "\" + System.getProperty("file.seperator") + fileName);
        this.file = new File(filePath + "\\" + fileName);
        this.addToLogFile(this.getClass().getSimpleName(), " ** Logdatei geoeffnet! Datei: " + this.file.getName(), LoggingLevel.INFO);
        this.addToLogFile(this.getClass().getSimpleName(), " ** Log-Level = " + this.logLevel.toGerman() 
                                                         + " ** mit Log-Datei = " + this.withLogFile 
                                                         + " ** Logdatei ergaenzen = " + this.appendLogFile 
                                                         + " ** mit Zeitangabe = " + this.withTimeStamp
                                                         + " ** mit Gui-PopUp = " + this.showGuiDialog
                                                         + " ** mit Konsolen-Ausgabe = " + this.showConsoleOutput
                                                         + " ** bei Error exit = " + this.stopRunIfError
                                                         , LoggingLevel.INFO);
        if (this.appendLogFile) {
            if (!this.withLogFile) {
                this.addToLogFile(this.getClass().getSimpleName(), " Ergaenzung Log-Datei gewaehlt aber es wird keine Log-Datei gewuenscht ?", LoggingLevel.WARNING);
                this.appendLogFile = false;
            }
        }
    }
  
    public void setLogLevelInfo(){ this.logLevel = LoggingLevel.INFO; }
    public void setLogLevelDebug(){ this.logLevel = LoggingLevel.DEBUG; }
    public void setLogLevelWarning(){ this.logLevel = LoggingLevel.WARNING; }
    public void setLogLevelError(){ this.logLevel = LoggingLevel.ERROR; }
    public void setLogLevelFatal(){ this.logLevel = LoggingLevel.FATAL; }
    
    private void showDialogMsg(String msgText, LoggingLevel level) {
        if (this.showGuiDialog) {
            this.msgLevel = level;
            if (this.msgLevel.compareTo(this.logLevel) >= 0) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane optionPane;
                switch (this.msgLevel){
                case INFO:
                    optionPane = new JOptionPane(msgText, JOptionPane.INFORMATION_MESSAGE);
                    break;
                case DEBUG:
                    optionPane = new JOptionPane(msgText, JOptionPane.INFORMATION_MESSAGE);
                    break;
                case WARNING:
                    optionPane = new JOptionPane(msgText, JOptionPane.WARNING_MESSAGE);
                    break;
                case ERROR:
                    optionPane = new JOptionPane(msgText, JOptionPane.ERROR_MESSAGE);
                    break;
                case FATAL:
                    optionPane = new JOptionPane(msgText, JOptionPane.ERROR_MESSAGE);
                    break;
                default:
                    optionPane = new JOptionPane(msgText, JOptionPane.QUESTION_MESSAGE);
                    break;
                }
                JDialog dialog = optionPane.createDialog(level.toGerman());
                dialog.setAlwaysOnTop(true);
                dialog.setVisible(true);
            }
        }
    }

    private void showSystemOut(String msgText) {
        if (this.showConsoleOutput) {
        if (this.msgLevel.compareTo(this.logLevel) >= 0) {
            System.out.println(msgText);
        }}
    }
/**
 * @remark Oeffnet "Notepad.exe" mit der aktuellen Logging-Datei.
 */
    public void openLogFileWithNotepad() {
        if (this.withLogFile) {
            try {
                Runtime.getRuntime().exec("notepad.exe \"" + file.getCanonicalPath() + "\"");
            } catch (IOException ex) {
                String msgText = LoggingLevel.ERROR + "(Logger-003) Fehler in Ausfuehrung notepad.exe! " +  ex;
                this.showSystemOut(msgText);
                this.showDialogMsg(msgText, LoggingLevel.ERROR);
            }
        } else {
            this.addToLogFile(this.getClass().getSimpleName(), "(006) - Es wurde keine Log-Datei gewuenscht!", LoggingLevel.WARNING);
        }
    }

    private void closeLogFile() {
        try {
            fw.flush();
            fw.close();
        } catch (IOException e) {
            String msgText = LoggingLevel.ERROR.toGerman() + " (Logger-002) Fehler beim schliessen der LogDatei! " +  e;
            this.showSystemOut(msgText);
            this.showDialogMsg(msgText, LoggingLevel.ERROR);
        }
    }

    private void setFeedbackToGui() {
        // gui updates und info
        if (this.lblApp != null) {
            this.lblApp.setText("[" + this.msgLevel.name() + "] - " + this.msgMessage);
        }
    }

/**
 * 
 * @param msgClass Name der Ursprungsklasse als Text
 * @param msgMessage Textlicher Inhalt der auszugebenden Nachricht/Information
 * @param msgLevel LoggingLevel (import LogLevel.java) DEBUG/INFO/WARNING/ERROR/FATAL
 */
    public void addToLogFile(String msgClass, String msgMessage, LoggingLevel msgLevel) {
        this.msgLevel = msgLevel;
        this.msgClass = msgClass;
        this.msgMessage = msgMessage;
        String msgText = constructMsg();
        if (this.msgLevel.compareTo(this.logLevel) >= 0) {
          try {
            this.showSystemOut(msgText);
            this.showDialogMsg(msgText, this.msgLevel);
            if (this.withLogFile){
            this.openLogFile();
                this.fw.append(msgText);
                this.fw.append(System.getProperty("line.separator"));
                this.closeLogFile();
            }
            this.setFeedbackToGui();
            if (this.stopRunIfError) {
                if (this.msgLevel == LoggingLevel.ERROR || this.msgLevel == LoggingLevel.FATAL) {
                    this.showDialogMsg("Die Applikation wird nun beendet! Klicken Sie NICHT auf OK falls Sie noch Informationen aus dem aktuellen Kontext benoetigen!", this.msgLevel);
                    System.exit(-1);
                }
            }
          } catch (IOException ex1) {
            msgText = LoggingLevel.ERROR.toGerman() + " (Logger-004) Fehler beim erweitern und aendern der LogDatei! " + ex1;
            this.showSystemOut(msgText);
            this.showDialogMsg(msgText, LoggingLevel.ERROR);
          }
        }
    }
    
    private String constructMsg(){
        String msgText = " ";
        if (this.withTimeStamp) {
            msgText = this.getTime() + " " ;
        } 
        msgText = msgText + "[" + this.msgClass + "] [" + this.msgLevel.name() + "] - " + this.msgMessage;
        return msgText;
    }
    
    private void openLogFile() {
        //
        try {
            if (this.appendLogFile){
            fw = new FileWriter(file, true);
            } else {
            fw = new FileWriter(file);
            }
        } catch (IOException e) {
            String msgText =  LoggingLevel.ERROR.toGerman() + " (Logger-001) Fehler beim erstellen und oeffnen der LogDatei! " + e;
            this.showSystemOut(msgText);
            this.showDialogMsg(msgText, LoggingLevel.ERROR);
        }
    }

/**
 * 
 * @param lblSendTo Label-Feld aus der aufrufenden GUI als JLabel - Ueber dieses Element erfolgt ggf. die rueckgabe der Nachricht
 */
    public void setLblToSendTo(JLabel lblSendTo) {
        this.lblApp = lblSendTo;
    }

    private String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String timeNow = sdf.format(new Date());
        return timeNow;
    }

    private String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String dateNow = sdf.format(new Date());
        return dateNow;
    }

    private String getSystemDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateNow = sdf.format(new Date());
        return dateNow;
    }

}
