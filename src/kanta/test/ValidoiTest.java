package kanta.test;
// Generated by ComTest BEGIN
import java.util.List;
import java.util.ArrayList;
import static org.junit.Assert.*;
import org.junit.*;
import static kanta.Validoi.*;
// Generated by ComTest END

/**
 * Test class made by ComTest
 * @version 2024.02.21 14:42:48 // Generated by ComTest
 *
 */
@SuppressWarnings({ "all" })
public class ValidoiTest {



  // Generated by ComTest BEGIN
  /** testOnkoNimiTallennettavissa23 */
  @Test
  public void testOnkoNimiTallennettavissa23() {    // Validoi: 23
    assertEquals("From: Validoi line: 24", true, onkoNimiTallennettavissa("Mustikkakakku")); 
    assertEquals("From: Validoi line: 25", true, onkoNimiTallennettavissa("Ääkköset ja välilyönnit")); 
    assertEquals("From: Validoi line: 26", true, onkoNimiTallennettavissa("1234567890")); 
    assertEquals("From: Validoi line: 27", false, onkoNimiTallennettavissa("Mustikkakakku|1|2|3|4")); 
    assertEquals("From: Validoi line: 28", false, onkoNimiTallennettavissa("Mustikkakakku*")); 
    assertEquals("From: Validoi line: 29", true, onkoNimiTallennettavissa("Mustikkakakku/")); 
    assertEquals("From: Validoi line: 30", false, onkoNimiTallennettavissa("")); 
    assertEquals("From: Validoi line: 31", false, onkoNimiTallennettavissa(null)); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testOnkoDuplikaatteja54 */
  @Test
  public void testOnkoDuplikaatteja54() {    // Validoi: 54
    List<String> nimet = new ArrayList<String>(); 
    nimet.addAll(List.of("Pekka", "Pirkko", "Pekka", "Joonas", "Pasi")); 
    assertEquals("From: Validoi line: 59", true, onkoDuplikaatteja(nimet)); 
    nimet.remove(0); 
    assertEquals("From: Validoi line: 62", false, onkoDuplikaatteja(nimet)); 
    nimet.add("Pasi"); 
    assertEquals("From: Validoi line: 65", true, onkoDuplikaatteja(nimet)); 
    nimet.remove(0); 
    nimet.remove(0); 
    nimet.remove(0); 
    assertEquals("From: Validoi line: 70", true, onkoDuplikaatteja(nimet)); 
    nimet.remove(0); 
    assertEquals("From: Validoi line: 73", false, onkoDuplikaatteja(nimet)); 
    assertEquals("From: Validoi line: 74", false, nimet.isEmpty()); 
    nimet.remove(0); 
    assertEquals("From: Validoi line: 77", false, onkoDuplikaatteja(nimet)); 
    assertEquals("From: Validoi line: 78", true, nimet.isEmpty()); 
  } // Generated by ComTest END
}