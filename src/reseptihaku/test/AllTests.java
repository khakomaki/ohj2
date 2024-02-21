package reseptihaku.test;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import kanta.test.HajautusTest;
import kanta.test.TietueHallitsijaTest;
import kanta.test.VaihtoehtoAttribuuttiTest;

/**
 * @author hakom
 * @version 24 Oct 2023
 *
 */
@Suite
@SelectClasses({
        OsionAinesosaTest.class, OsionAinesosatTest.class, OsioTest.class,
        OsiotTest.class, ReseptiTest.class, ReseptitTest.class, OhjeTest.class, 
        OhjeetTest.class, HajautusTest.class, TietueHallitsijaTest.class, 
        VaihtoehtoAttribuuttiTest.class })
public class AllTests {
    // Ajetaan kaikki testit
}
