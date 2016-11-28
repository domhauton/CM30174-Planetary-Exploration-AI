package rover.model.scanning;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

/**
 * Created by dominic on 25/11/16.
 */
public class ScanMapTest {

  private final int MAP_SIZE = 10;

  private ScanMap scanMap1;
  private ScanMap scanMap2;

  @Before
  public void setUp() {
    scanMap1 = new ScanMap(MAP_SIZE);
    scanMap2 = new ScanMap(MAP_SIZE);
  }

  @Test
  public void prefillAllMapTest() throws Exception {
    for(int x = 0; x < MAP_SIZE; x++) {
      for(int y = 0; y < MAP_SIZE; y++) {
        Assert.assertEquals(scanMap1.get(x, y), ScanState.UNKNOWN);
      }
    }
  }

  @Test
  public void putAndRetrieve() throws Exception {
    Assert.assertEquals(scanMap1.get(0, 0), ScanState.UNKNOWN);
    scanMap1.put(0, 0, ScanState.PARTIAL);
    Assert.assertEquals(scanMap1.get(0, 0), ScanState.PARTIAL);
    scanMap1.put(0, 0, ScanState.SCANNED);
    Assert.assertEquals(scanMap1.get(0, 0), ScanState.SCANNED);
  }

  @Test
  public void putOverflowAndRetrieve() throws Exception {
    Assert.assertEquals(scanMap1.get(0, 0), ScanState.UNKNOWN);
    scanMap1.put(MAP_SIZE, MAP_SIZE, ScanState.PARTIAL);
    Assert.assertEquals(scanMap1.get(0, 0), ScanState.PARTIAL);
    scanMap1.put(MAP_SIZE, MAP_SIZE, ScanState.SCANNED);
    Assert.assertEquals(scanMap1.get(0, 0), ScanState.SCANNED);
  }

  @Test
  public void putUnderflowAndRetrieve() throws Exception {
    Assert.assertEquals(scanMap1.get(MAP_SIZE-1, MAP_SIZE-1), ScanState.UNKNOWN);
    scanMap1.put(-1, -1, ScanState.PARTIAL);
    Assert.assertEquals(scanMap1.get(MAP_SIZE-1, MAP_SIZE-1), ScanState.PARTIAL);
    scanMap1.put(-1, -1, ScanState.SCANNED);
    Assert.assertEquals(scanMap1.get(MAP_SIZE-1, MAP_SIZE-1), ScanState.SCANNED);
  }

  @Test
  public void putAndRetrieveOverflow() throws Exception {
    Assert.assertEquals(scanMap1.get(MAP_SIZE, MAP_SIZE), ScanState.UNKNOWN);
    scanMap1.put(0, 0, ScanState.PARTIAL);
    Assert.assertEquals(scanMap1.get(MAP_SIZE, MAP_SIZE), ScanState.PARTIAL);
    scanMap1.put(0, 0, ScanState.SCANNED);
    Assert.assertEquals(scanMap1.get(MAP_SIZE, MAP_SIZE), ScanState.SCANNED);
  }

  @Test
  public void putAndRetrieveUnderflow() throws Exception {
    Assert.assertEquals(scanMap1.get(-1, -1), ScanState.UNKNOWN);
    scanMap1.put(MAP_SIZE-1, MAP_SIZE-1, ScanState.PARTIAL);
    Assert.assertEquals(scanMap1.get(-1, -1), ScanState.PARTIAL);
  }

  @Test
  public void putOverflow() throws Exception {
    scanMap1.put(0, 0, ScanState.PARTIAL);
    scanMap2.put(MAP_SIZE+1, MAP_SIZE, ScanState.PARTIAL);
    Assert.assertEquals(scanMap1, scanMap2);
  }

  @Test
  public void printTest() throws Exception {
    System.out.println(scanMap1.toString());
  }
}