package rover.model.scanning;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by dominic on 25/11/16.
 */
public class ScanResultTest {

  private static int MAP_SIZE = 15;

  private ScanMap scanMap1;

  @Before
  public void setUp() throws Exception {
    scanMap1 = new ScanMap(MAP_SIZE);
  }

  @Test
  public void testBasicScanApply() throws Exception {
    ScanResult scanResult = new ScanResult(new GridPos(0, 0), 1);
    scanResult.applyScan(scanMap1);
    Assert.assertEquals(scanMap1.get(0, 0), ScanState.SCANNED);

    Assert.assertEquals(scanMap1.get(-1, 0), ScanState.PARTIAL);
    Assert.assertEquals(scanMap1.get(1, 0), ScanState.PARTIAL);
    Assert.assertEquals(scanMap1.get(0, -1), ScanState.PARTIAL);
    Assert.assertEquals(scanMap1.get(0, 1), ScanState.PARTIAL);

    Assert.assertEquals(scanMap1.get(-2, 0), ScanState.UNKNOWN);
    Assert.assertEquals(scanMap1.get(2, 0), ScanState.UNKNOWN);
    Assert.assertEquals(scanMap1.get(0, -2), ScanState.UNKNOWN);
    Assert.assertEquals(scanMap1.get(0, 2), ScanState.UNKNOWN);
  }

  @Test
  public void testBasicValuation() throws Exception {
    ScanResult scanResult = new ScanResult(new GridPos(0, 0), 1);
    scanResult.applyScan(scanMap1);
    Assert.assertEquals(0, scanResult.calculateScanSearchValue(scanMap1));
  }

  @Test
  public void testBasicValuationLarge() throws Exception {
    ScanResult scanResult = new ScanResult(new GridPos(2, 2), 2);
    scanResult.applyScan(scanMap1);
    Assert.assertEquals(0, scanResult.calculateScanSearchValue(scanMap1));
  }

  @Test
  public void testBasicValuationOffSet() throws Exception {
    testBasicValuationLarge();
    ScanResult scanResult = new ScanResult(new GridPos(3, 2), 2);
    scanResult.applyScan(scanMap1);
    Assert.assertEquals(0, scanResult.calculateScanSearchValue(scanMap1));
  }

  @Test
  public void testDesireCalculation() throws Exception {
    ScanResult scanResult1 = new ScanResult(new GridPos(3, 3), 3);
    ScanResult scanResult4 = new ScanResult(new GridPos(3, 6), 3);
    ScanResult scanResult5 = new ScanResult(new GridPos(3, 7), 3);
    ScanResult scanResult6 = new ScanResult(new GridPos(3, 7), 3);
    scanResult1.applyScan(scanMap1);
    Assert.assertTrue(scanResult4.scanDesirability(scanMap1) > scanResult5.scanDesirability(scanMap1));
    Assert.assertTrue(scanResult4.scanDesirability(scanMap1) > scanResult6.scanDesirability(scanMap1));
    scanResult4.applyScan(scanMap1);
  }
}