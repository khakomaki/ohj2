package reseptihaku.test;
// Generated by ComTest BEGIN
import static org.junit.Assert.*;
import org.junit.*;
import reseptihaku.*;
// Generated by ComTest END

/**
 * Test class made by ComTest
 * @version 2024.02.21 14:41:20 // Generated by ComTest
 *
 */
@SuppressWarnings({ "all" })
public class OsioTest {



  // Generated by ComTest BEGIN
  /** testOsio32 */
  @Test
  public void testOsio32() {    // Osio: 32
    Osio pizzapohja = new Osio(2, "Pizzapohja"); 
    assertEquals("From: Osio line: 34", "2|Pizzapohja", pizzapohja.toString()); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testOsio51 */
  @Test
  public void testOsio51() {    // Osio: 51
    Osio pizzapohja = new Osio("Pizzapohja"); 
    assertEquals("From: Osio line: 53", "-1|Pizzapohja", pizzapohja.toString()); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testOsio68 */
  @Test
  public void testOsio68() {    // Osio: 68
    Osio osio = new Osio(); 
    assertEquals("From: Osio line: 70", "-1|Osion nimi", osio.toString()); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testSetUusiNimi127 */
  @Test
  public void testSetUusiNimi127() {    // Osio: 127
    Osio kakkupohja = new Osio(8, "Kakkupohja"); 
    assertEquals("From: Osio line: 129", "8|Kakkupohja", kakkupohja.toString()); 
    kakkupohja.setUusiNimi(""); 
    assertEquals("From: Osio line: 132", "8|Kakkupohja", kakkupohja.toString()); 
    kakkupohja.setUusiNimi("Piirakkapohja"); 
    assertEquals("From: Osio line: 135", "8|Piirakkapohja", kakkupohja.toString()); 
    kakkupohja.setUusiNimi(null); 
    assertEquals("From: Osio line: 138", "8|Piirakkapohja", kakkupohja.toString()); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testGetNimi154 */
  @Test
  public void testGetNimi154() {    // Osio: 154
    Osio kakkupohja = new Osio(8, "Kakkupohja"); 
    assertEquals("From: Osio line: 156", "Kakkupohja", kakkupohja.getNimi()); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testParse291 */
  @Test
  public void testParse291() {    // Osio: 291
    Osio osio = new Osio(); 
    osio.parse("1|1|Muropohja"); 
    assertEquals("From: Osio line: 294", true, osio.toString().endsWith("|Muropohja")); 
    osio.parse("  5   |  2    |       Pizzapohja   "); 
    assertEquals("From: Osio line: 297", true, osio.toString().endsWith("|Pizzapohja")); 
    osio.parse("1|Muropohja"); 
    assertEquals("From: Osio line: 300", true, osio.toString().endsWith("|Pizzapohja")); 
    osio.parse("1 1 Muropohja"); 
    assertEquals("From: Osio line: 303", true, osio.toString().endsWith("|Pizzapohja")); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testClone414 */
  @Test
  public void testClone414() {    // Osio: 414
    Osio sampylat = new Osio("Sämpylät"); 
    sampylat.lisaaAinesosa("vehnäjauhoja", "8dl"); 
    sampylat.lisaaAinesosa("maito", "4dl"); 
    sampylat.lisaaAinesosa("kuivahiiva", "1ps"); 
    sampylat.lisaaOhje(new Ohje("Lämmitä maito 42-asteiseksi")); 
    sampylat.lisaaOhje(new Ohje("Sekoita kuivahiiva maitoon")); 
    sampylat.lisaaOhje(new Ohje("Sekoita jauhot maitoon")); 
    Osio sampylaKopio = sampylat.clone(); 
    assertEquals("From: Osio line: 424", true, sampylaKopio.equals(sampylat)); 
    assertEquals("From: Osio line: 425", true, sampylaKopio.toString().equals(sampylat.toString())); 
    assertEquals("From: Osio line: 426", true, sampylat.annaOsionAinesosat().equals(sampylaKopio.annaOsionAinesosat())); 
    assertEquals("From: Osio line: 427", true, sampylat.annaOsionOhjeet().equals(sampylaKopio.annaOsionOhjeet())); 
    sampylat.lisaaOhje("Anna kohota liinan alla"); 
    assertEquals("From: Osio line: 430", false, sampylaKopio.equals(sampylat)); 
    assertEquals("From: Osio line: 431", false, sampylat.annaOsionOhjeet().equals(sampylaKopio.annaOsionOhjeet())); 
    sampylaKopio.lisaaOhje("Anna kohota liinan alla"); 
    assertEquals("From: Osio line: 434", true, sampylaKopio.equals(sampylat)); 
    assertEquals("From: Osio line: 435", true, sampylat.annaOsionOhjeet().equals(sampylaKopio.annaOsionOhjeet())); 
    sampylat.setUusiNimi("Sämpylä"); 
    assertEquals("From: Osio line: 438", false, sampylaKopio.toString().equals(sampylat.toString())); 
    sampylaKopio.setUusiNimi("Sämpylä"); 
    assertEquals("From: Osio line: 441", true, sampylaKopio.toString().equals(sampylat.toString())); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testEquals461 */
  @Test
  public void testEquals461() {    // Osio: 461
    Osio pohja1 = new Osio(1, "Kakkupohja"); 
    Osio pohja2 = new Osio(1, "Kakkupohj"); 
    Osio pohja3 = new Osio(2, "Kakkupohja"); 
    assertEquals("From: Osio line: 466", false, pohja1.equals(pohja2)); 
    assertEquals("From: Osio line: 467", false, pohja1.equals(pohja3)); 
    assertEquals("From: Osio line: 468", false, pohja2.equals(pohja3)); 
    pohja2.setUusiNimi("Kakkupohja"); 
    assertEquals("From: Osio line: 471", true, pohja1.equals(pohja2)); 
    assertEquals("From: Osio line: 472", false, pohja2.equals(pohja3)); 
    assertEquals("From: Osio line: 473", true, pohja2.equals(pohja1)); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testHashCode492 */
  @Test
  public void testHashCode492() {    // Osio: 492
    Osio pohja1 = new Osio(1, "Kakkupohja"); 
    Osio pohja2 = new Osio(1, "Kakkupohj"); 
    Osio pohja3 = new Osio(2, "Kakkupohja"); 
    assertEquals("From: Osio line: 497", false, pohja1.hashCode() == pohja2.hashCode()); 
    assertEquals("From: Osio line: 498", false, pohja1.hashCode() == pohja3.hashCode()); 
    assertEquals("From: Osio line: 499", false, pohja2.hashCode() == pohja3.hashCode()); 
    pohja2.setUusiNimi("Kakkupohja"); 
    assertEquals("From: Osio line: 502", true, pohja1.hashCode() == pohja2.hashCode()); 
    assertEquals("From: Osio line: 503", false, pohja1.hashCode() == pohja3.hashCode()); 
  } // Generated by ComTest END
}