package rover.model.scanning;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by dominic on 25/11/16.
 */
public class ScanResultTest {

  private static int MAP_SIZE = 10;

  private ScanMap scanMap1;

  @Before
  public void setUp() throws Exception {
    scanMap1 = new ScanMap(MAP_SIZE, MAP_SIZE);
  }

  @Test
  public void testInsufficientScanApply() throws Exception {
    ScanResult scanResult = new ScanResult(0, 0, 1);
    scanResult.applyScan(scanMap1);
    Assert.assertEquals(scanMap1.get(0, 0), ScanState.PARTIAL);

    Assert.assertEquals(scanMap1.get(-1, 0), ScanState.UNKNOWN);
    Assert.assertEquals(scanMap1.get(1, 0), ScanState.UNKNOWN);
    Assert.assertEquals(scanMap1.get(0, -1), ScanState.UNKNOWN);
    Assert.assertEquals(scanMap1.get(0, 1), ScanState.UNKNOWN);
  }

  @Test
  public void testBasicScanApply() throws Exception {
    ScanResult scanResult = new ScanResult(0, 0, 2);
    scanResult.applyScan(scanMap1);
    Assert.assertEquals(scanMap1.get(0, 0), ScanState.SCANNED);

    Assert.assertEquals(scanMap1.get(-1, 0 ), ScanState.PARTIAL);
    Assert.assertEquals(scanMap1.get(1 , 0 ), ScanState.PARTIAL);
    Assert.assertEquals(scanMap1.get(0 , -1), ScanState.PARTIAL);
    Assert.assertEquals(scanMap1.get(0 , 1 ), ScanState.PARTIAL);

    Assert.assertEquals(scanMap1.get(-2, 0 ), ScanState.UNKNOWN);
    Assert.assertEquals(scanMap1.get(2 , 0 ), ScanState.UNKNOWN);
    Assert.assertEquals(scanMap1.get(0 , -2), ScanState.UNKNOWN);
    Assert.assertEquals(scanMap1.get(0 , 2 ), ScanState.UNKNOWN);
  }

  @Test
  public void testBasicValuation() throws Exception {
    ScanResult scanResult = new ScanResult(0, 0, 1);
    Assert.assertEquals(1, scanResult.calculateScanValue(scanMap1));
    scanResult.applyScan(scanMap1);
    Assert.assertEquals(0, scanResult.calculateScanValue(scanMap1));
  }

  @Test
  public void testBasicValuationLarge() throws Exception {
    ScanResult scanResult = new ScanResult(2, 2, 2);
    Assert.assertEquals(6, scanResult.calculateScanValue(scanMap1));
    scanResult.applyScan(scanMap1);
    Assert.assertEquals(0, scanResult.calculateScanValue(scanMap1));
  }

  @Test
  public void testBasicValuationOffSet() throws Exception {
    testBasicValuationLarge();
    ScanResult scanResult = new ScanResult(3, 2, 2);
    Assert.assertEquals(4, scanResult.calculateScanValue(scanMap1));
    scanResult.applyScan(scanMap1);
    Assert.assertEquals(0, scanResult.calculateScanValue(scanMap1));
  }
}