/*
 * (C) VIF3 - Email: info@bbs2-leer.de
 * Diese Software kann frei genutzt werden. Auch gegen die Bearbeitung und
 * Aenderung des Quellcodes spricht nichts. Wer Verbesserungen vornimmt oder  
 * Fehler findet: Wir freuen uns ueber Feedback! Gerne auch in Form von Kritik.
 * Bitte diesen Vermerk nicht loeschen!
 */

import hwapp.DataHandler;
import hwapp.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author bbsuser
 */
public class DatahandlerTest {

    public DatahandlerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void test1() {
        hwapp.Logger logger = new hwapp.Logger(hwapp.LogLevel.LoggingLevel.INFO, true, false, true, false, true, false);
        DataHandler datahandler = new DataHandler(logger, "test", 80, "database", "leschiBaby", "");

        //assertTrue(true);
        //fail("geht nicht");
        
        assertNotNull(datahandler);
        //datahandler.deleteUser(1);
        
        assertEquals(1,2);
        
        
    }

}
