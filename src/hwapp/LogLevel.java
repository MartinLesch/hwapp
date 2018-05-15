/*
 * Klasse: LogLevel (Enum fuer Logger)
 */
package hwapp;

/**
 *
 * @author Martin Lesch
 * @version 08.03.2018
 */
public class LogLevel {
    public enum LoggingLevel {
        INFO("Informatiom"), DEBUG("Debug"), WARNING("Warnung"), ERROR("Fehler"), FATAL("Schwerwiegender Fehler");
        private String value;
        private LoggingLevel(String value){
            this.value = value;
        }
        public String toGerman() {
            return this.value;
        }
    }
}

