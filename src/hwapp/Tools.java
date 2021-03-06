/*
 * Klasse: Tools - kleine Helferlein
 */
package hwapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import hwapp.LogLevel.LoggingLevel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.logging.Level;

/**
 *
 * @author Martin Lesch
 * @version 08.03.2018
 */
public class Tools {

    Logger logger;

    public Tools(Logger logger) {
        this.logger = logger;
    }
    
    public void setEnvrionment(){
        ProcessBuilder pb = new ProcessBuilder("CMD.exe", "/C", "SET"); 
        pb.redirectErrorStream(true);
        Map<String,String> env = pb.environment();
        //String path = env.get("Path") + ";C:\\xyz\\abc";
        //env.put("Path", path);
        env.put("GLOBAL general_log", "ON");
        Process process = null;
        try {
            process = pb.start();
        } catch (IOException ex) {
                logger.addToLogFile(this.getClass().getSimpleName(), "Setzen Globale Environment Variable fehlgeschlagen. " +ex , LoggingLevel.ERROR);
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        try {
            while ((line = in.readLine()) != null)
            {
                System.out.println(line);
            }
        } catch (IOException ex) {
                logger.addToLogFile(this.getClass().getSimpleName(), "Ausgabe der Environment-Variablen fehlgeschlagen. " +ex , LoggingLevel.WARNING);
        }
    }

    public String getTimeStamp(String fromWhere) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String dateTime = sdf.format(date);
        return dateTime;
    }

    public Date tryParseDateYyyyMMdd(String dateAsString, String fromWhere) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        if (dateAsString != null){
            try {
                date = sdf.parse(dateAsString);
            } catch (ParseException e) {
                //e.printStackTrace();
                logger.addToLogFile(this.getClass().getSimpleName(), "Umwandlung zu Datum 'yyyyMMdd' fehlgeschlagen: " + fromWhere + " Inhalt: " + dateAsString, LoggingLevel.ERROR);
                //System.out.println("Umwandlung zu Datum 'yyyyMMdd' fehlgeschlagen: " + fromWhere + " Inhalt: " + dateAsString);
            }
        }
        return date;
    }

    public String tryParseDateYyyyMMddToDateTime(String dateAsString, String fromWhere) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateTime = null;
        Date date = null;
        if (dateAsString != null){
            try {
                date = sdf.parse(dateAsString);
            } catch (ParseException e) {
                //e.printStackTrace();
                logger.addToLogFile(this.getClass().getSimpleName(), "Umwandlung zu Datum 'yyyyMMdd' fehlgeschlagen: " + fromWhere + " Inhalt: " + dateAsString, LoggingLevel.ERROR);
                //System.out.println("Umwandlung zu Datum 'yyyyMMdd' fehlgeschlagen: " + fromWhere + " Inhalt: " + dateAsString);
            }
        }
        dateTime = dateToDateTime(date);
        return dateTime;
    }
    
    public String dateToDateTime(Date date){
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (date == null) {
            return "0000-00-00 00:00:00";
        } else {
            String dateTime = sdf.format(date);
            return dateTime;
        }
    }

    public Date tryParseDateYMDwithHyphen(String dateAsString, String fromWhere) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(dateAsString);
        } catch (ParseException e) {
            //e.printStackTrace();
            logger.addToLogFile(this.getClass().getSimpleName(), "Umwandlung zu Datum 'yyyy-MM-dd' fehlgeschlagen: " + fromWhere + " Inhalt: " + dateAsString, LoggingLevel.ERROR);
            //System.out.println("Umwandlung zu Datum 'yyyy-MM-dd' fehlgeschlagen: " + fromWhere + " Inhalt: " + dateAsString);
        }
        return date;
    }

    public int tryParseInt(String parseString, String fromWhere) {
        try {
            return Integer.parseInt(parseString);
        } catch (NumberFormatException e) {
            logger.addToLogFile(this.getClass().getSimpleName(), "Umwandlung zu Zahl fehlgeschlagen: " + fromWhere + " Inhalt: " + parseString, LoggingLevel.INFO);
            //System.out.println("Umwandlung zu Zahl fehlgeschlagen: " + fromWhere + " Inhalt: " + parseString);
            return 0;
        }
    }


    public double tryParseDouble(String parseString, String fromWhere) {
        try {
            return Double.parseDouble(parseString);
        } catch (NumberFormatException e) {
            logger.addToLogFile(this.getClass().getSimpleName(), "Umwandlung zu Fliesskomma-Zahl fehlgeschlagen: " + fromWhere + " Inhalt: " + parseString, LoggingLevel.INFO);
            //System.out.println("Umwandlung zu Double fehlgeschlagen: " + fromWhere + " Inhalt: " + parseString);
            return 0.0;
        }
    }

    public String tryParseString(double parseDouble, String fromWhere) {
        try {
            return String.valueOf(parseDouble);
        } catch (NumberFormatException e) {
            logger.addToLogFile(this.getClass().getSimpleName(), "Umwandlung Fliesskomma-Zahl zu String fehlgeschlagen: " + fromWhere + " Inhalt: " + parseDouble, LoggingLevel.INFO);
            //System.out.println("Umwandlung zu Double fehlgeschlagen: " + fromWhere + " Inhalt: " + parseString);
            return null;
        }
    }

}
