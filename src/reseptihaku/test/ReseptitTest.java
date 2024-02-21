package reseptihaku.test;
// Generated by ComTest BEGIN
import static org.junit.Assert.*;
import org.junit.*;
import reseptihaku.*;
// Generated by ComTest END

/**
 * Test class made by ComTest
 * @version 2024.02.21 14:41:50 // Generated by ComTest
 *
 */
@SuppressWarnings({ "all" })
public class ReseptitTest {



  // Generated by ComTest BEGIN
  /** testReseptit44 */
  @Test
  public void testReseptit44() {    // Reseptit: 44
    Reseptit reseptit = new Reseptit(); 
    assertEquals("From: Reseptit line: 46", "0|reseptit.dat", reseptit.toString()); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testSetTiedostoNimi61 */
  @Test
  public void testSetTiedostoNimi61() {    // Reseptit: 61
    Reseptit reseptit = new Reseptit(); 
    assertEquals("From: Reseptit line: 63", "0|reseptit.dat", reseptit.toString()); 
    reseptit.setTiedostoNimi(""); 
    assertEquals("From: Reseptit line: 66", "0|reseptit.dat", reseptit.toString()); 
    reseptit.setTiedostoNimi("salaiset_reseptit.txt"); 
    assertEquals("From: Reseptit line: 69", "0|salaiset_reseptit.txt", reseptit.toString()); 
    reseptit.setTiedostoNimi(null); 
    assertEquals("From: Reseptit line: 72", "0|salaiset_reseptit.txt", reseptit.toString()); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testLisaa113 */
  @Test
  public void testLisaa113() {    // Reseptit: 113
    Reseptit reseptit = new Reseptit(); 
    Resepti resepti1 = reseptit.lisaa("Mustikkapiirakka"); 
    Resepti resepti2 = reseptit.lisaa("Lihapiirakka"); 
    Resepti resepti3 = reseptit.lisaa("Kinkkupiirakka"); 
    assertEquals("From: Reseptit line: 118", "3|reseptit.dat", reseptit.toString()); 
    assertEquals("From: Reseptit line: 120", true, reseptit.anna(0).equals(resepti1));
    assertEquals("From: Reseptit line: 121", true, reseptit.anna(1).equals(resepti2)); 
    assertEquals("From: Reseptit line: 122", true, reseptit.anna(2).equals(resepti3)); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testLisaa140 */
  @Test
  public void testLisaa140() {    // Reseptit: 140
    Reseptit reseptit = new Reseptit(); 
    Resepti mustikkapiirakka = new Resepti("Mustikkapiirakka"); 
    reseptit.lisaa(mustikkapiirakka); 
    assertEquals("From: Reseptit line: 145", "1|reseptit.dat", reseptit.toString()); 
    reseptit.lisaa(new Resepti()); 
    assertEquals("From: Reseptit line: 148", "2|reseptit.dat", reseptit.toString()); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testAnna178 */
  @Test
  public void testAnna178() {    // Reseptit: 178
    Reseptit reseptit = new Reseptit(); 
    Resepti resepti1 = reseptit.lisaa("Mustikkapiirakka"); 
    Resepti resepti2 = reseptit.lisaa("Lihapiirakka"); 
    Resepti resepti3 = reseptit.lisaa("Kinkkupiirakka"); 
    assertEquals("From: Reseptit line: 183", "3|reseptit.dat", reseptit.toString()); 
    assertEquals("From: Reseptit line: 185", true, reseptit.anna(-1) == null); 
    assertEquals("From: Reseptit line: 186", true, reseptit.anna(0) == resepti1); 
    assertEquals("From: Reseptit line: 187", true, reseptit.anna(1) == resepti2); 
    assertEquals("From: Reseptit line: 188", true, reseptit.anna(2) == resepti3); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testVaihdaResepti214 */
  @Test
  public void testVaihdaResepti214() {    // Reseptit: 214
    Reseptit reseptit = new Reseptit(); 
    Resepti resepti1 = reseptit.lisaa("Mustikkapiirakka"); 
    Resepti resepti2 = reseptit.lisaa("Lihapiirakka"); 
    Resepti resepti3 = reseptit.lisaa("Kinkkupiirakka"); 
    assertEquals("From: Reseptit line: 219", true, reseptit.anna(1) == resepti2); 
    Resepti resepti4 = new Resepti("Kasvispiirakka"); 
    reseptit.vaihdaResepti(resepti4, resepti2); 
    reseptit.vaihdaResepti(resepti2, resepti4); 
    assertEquals("From: Reseptit line: 225", true, reseptit.anna(1) == resepti4); 
    reseptit.vaihdaResepti(resepti2, resepti1); 
    assertEquals("From: Reseptit line: 228", true, reseptit.anna(1) == resepti4); 
    reseptit.vaihdaResepti(resepti1, resepti1); 
    assertEquals("From: Reseptit line: 231", true, reseptit.anna(0) == resepti1); 
    reseptit.vaihdaResepti(resepti3, resepti1); 
    assertEquals("From: Reseptit line: 234", true, reseptit.anna(2) == resepti1); 
    reseptit.vaihdaResepti(resepti3, null); 
    assertEquals("From: Reseptit line: 237", true, reseptit.anna(2) == resepti1); 
    reseptit.vaihdaResepti(null, resepti1); 
    assertEquals("From: Reseptit line: 240", true, reseptit.anna(2) == resepti1); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testPoista296 */
  @Test
  public void testPoista296() {    // Reseptit: 296
    Reseptit reseptit = new Reseptit(); 
    Resepti resepti1 = reseptit.lisaa("Mustikkapiirakka"); 
    Resepti resepti2 = reseptit.lisaa("Lihapiirakka"); 
    Resepti resepti3 = reseptit.lisaa("Kinkkupiirakka"); 
    assertEquals("From: Reseptit line: 301", "3|reseptit.dat", reseptit.toString()); 
    reseptit.poista(resepti2); 
    assertEquals("From: Reseptit line: 304", "2|reseptit.dat", reseptit.toString()); 
    reseptit.poista(null); 
    assertEquals("From: Reseptit line: 307", "2|reseptit.dat", reseptit.toString()); 
    reseptit.poista(resepti1); 
    assertEquals("From: Reseptit line: 310", "1|reseptit.dat", reseptit.toString()); 
    reseptit.poista(resepti1); 
    assertEquals("From: Reseptit line: 313", "1|reseptit.dat", reseptit.toString()); 
    reseptit.poista(resepti3); 
    assertEquals("From: Reseptit line: 316", "0|reseptit.dat", reseptit.toString()); 
    reseptit.poista(resepti1); 
    assertEquals("From: Reseptit line: 319", "0|reseptit.dat", reseptit.toString()); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testClone626 */
  @Test
  public void testClone626() {    // Reseptit: 626
    Reseptit reseptit = new Reseptit(); 
    reseptit.lisaa(new Resepti("Mustikkapiirakka")); 
    reseptit.lisaa(new Resepti("Juustokakku")); 
    assertEquals("From: Reseptit line: 630", "2|reseptit.dat", reseptit.toString()); 
    Reseptit reseptitKopio = reseptit.clone(); 
    assertEquals("From: Reseptit line: 633", "2|reseptit.dat", reseptitKopio.toString()); 
    reseptit.setTiedostoNimi("herkut.txt"); 
    assertEquals("From: Reseptit line: 636", "2|herkut.txt", reseptit.toString()); 
    assertEquals("From: Reseptit line: 637", "2|reseptit.dat", reseptitKopio.toString()); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testEquals659 */
  @Test
  public void testEquals659() {    // Reseptit: 659
    Reseptit reseptit1 = new Reseptit(); 
    Reseptit reseptit2 = new Reseptit(); 
    assertEquals("From: Reseptit line: 662", true, reseptit1.equals(reseptit2)); 
    assertEquals("From: Reseptit line: 663", true, reseptit2.equals(reseptit1)); 
    reseptit1.setTiedostoNimi("jälkiruoat.txt"); 
    assertEquals("From: Reseptit line: 666", false, reseptit1.equals(reseptit2)); 
    reseptit2.setTiedostoNimi("jälkiruoat.txt"); 
    assertEquals("From: Reseptit line: 669", true, reseptit1.equals(reseptit2)); 
    reseptit1.lisaa(new Resepti("Mustikkapiirakka")); 
    reseptit1.lisaa(new Resepti("Suklaakakku")); 
    assertEquals("From: Reseptit line: 673", false, reseptit1.equals(reseptit2)); 
    reseptit2.lisaa(new Resepti("Mustikkapiirakka")); 
    reseptit2.lisaa(new Resepti("Suklaakakku")); 
    assertEquals("From: Reseptit line: 677", true, reseptit1.equals(reseptit2)); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testHashCode702 */
  @Test
  public void testHashCode702() {    // Reseptit: 702
    Reseptit reseptit1 = new Reseptit(); 
    Reseptit reseptit2 = new Reseptit(); 
    assertEquals("From: Reseptit line: 705", true, reseptit1.hashCode() == reseptit2.hashCode()); 
    reseptit1.setTiedostoNimi("jälkiruoat.txt"); 
    assertEquals("From: Reseptit line: 708", false, reseptit1.hashCode() == reseptit2.hashCode()); 
    reseptit2.setTiedostoNimi("jälkiruoat.txt"); 
    assertEquals("From: Reseptit line: 711", true, reseptit1.hashCode() == reseptit2.hashCode()); 
    reseptit1.lisaa(new Resepti("Mustikkapiirakka")); 
    reseptit1.lisaa(new Resepti("Suklaakakku")); 
    assertEquals("From: Reseptit line: 715", false, reseptit1.hashCode() == reseptit2.hashCode()); 
    reseptit2.lisaa(new Resepti("Mustikkapiirakka")); 
    reseptit2.lisaa(new Resepti("Suklaakakku")); 
    assertEquals("From: Reseptit line: 719", true, reseptit1.hashCode() == reseptit2.hashCode()); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testToString740 */
  @Test
  public void testToString740() {    // Reseptit: 740
    Reseptit reseptit = new Reseptit(); 
    assertEquals("From: Reseptit line: 742", "0|reseptit.dat", reseptit.toString()); 
  } // Generated by ComTest END
}