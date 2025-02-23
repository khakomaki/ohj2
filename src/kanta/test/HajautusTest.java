package kanta.test;
// Generated by ComTest BEGIN
import static org.junit.Assert.*;
import org.junit.*;
import static kanta.Hajautus.*;
// Generated by ComTest END

/**
 * Test class made by ComTest
 * @version 2024.02.21 14:41:59 // Generated by ComTest
 *
 */
@SuppressWarnings({ "all" })
public class HajautusTest {



  // Generated by ComTest BEGIN
  /** testHajautus25 */
  @Test
  public void testHajautus25() {    // Hajautus: 25
    assertEquals("From: Hajautus line: 26", -28, hajautus(-1, 3)); 
    assertEquals("From: Hajautus line: 27", 3, hajautus(0, 3)); 
    assertEquals("From: Hajautus line: 28", 34, hajautus(1, 3)); 
    assertEquals("From: Hajautus line: 29", 1057, hajautus(34, 3)); 
    assertEquals("From: Hajautus line: 30", 32770, hajautus(1057, 3)); 
    assertEquals("From: Hajautus line: 32", 209410, hajautus(6755, 5)); 
    assertEquals("From: Hajautus line: 33", 6491713, hajautus(209410, 3)); 
    assertEquals("From: Hajautus line: 34", 209408, hajautus(6755, 3)); 
    assertEquals("From: Hajautus line: 35", 6491653, hajautus(209408, 5)); 
    assertEquals("From: Hajautus line: 37", 91236, hajautus(2943, 3)); 
    assertEquals("From: Hajautus line: 38", 91236, hajautus(2942, 34)); 
    assertEquals("From: Hajautus line: 39", 91236, hajautus(2944, -28)); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testHajautus59 */
  @Test
  public void testHajautus59() {    // Hajautus: 59
    String sana1 = new String("vasara"); 
    String sana2 = new String("naula"); 
    String sana3 = new String("vasara"); 
    assertEquals("From: Hajautus line: 63", false, hajautus(1, sana1) == hajautus(1, sana2)); 
    assertEquals("From: Hajautus line: 64", true, hajautus(1, sana1) == hajautus(1, sana3)); 
    int hajautusluku1 = hajautus(1, "pallo"); 
    hajautusluku1 = hajautus(hajautusluku1, "pullo"); 
    int hajautusluku2 = hajautus(1, "pullo"); 
    hajautusluku2 = hajautus(hajautusluku2, "pallo"); 
    assertEquals("From: Hajautus line: 70", false, hajautusluku1 == hajautusluku2); 
  } // Generated by ComTest END
}