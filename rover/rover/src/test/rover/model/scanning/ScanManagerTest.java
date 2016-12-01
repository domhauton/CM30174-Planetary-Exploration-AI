package rover.model.scanning;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import rover.model.maplocation.Coordinate;

/**
 * Created by dominic on 01/12/16.
 */
public class ScanManagerTest {
  private static Integer REAL_SCAN_RANGE = 1;
  private static Integer REAL_MOVE_SPEED = 2;
  private static Integer REAL_MAP_SIZE = 10;

  private ScanManager scanManager;

  @Before
  public void setUp() throws Exception {
    scanManager = new ScanManager(REAL_MAP_SIZE, REAL_SCAN_RANGE, 1);
  }

  @Test
  public void testLocal() throws Exception {
    Coordinate scan1 = Coordinate.ORIGIN.moveCoordinate(5, 5, REAL_MAP_SIZE);
    ScanResult scanResult = scanManager.getNextBestScanQuick(scan1, REAL_MOVE_SPEED, REAL_SCAN_RANGE);
    scanManager.applyScan(scanResult);
    String firstPrint = scanManager.toString();
    Coordinate scan2 = Coordinate.ORIGIN.moveCoordinate(5, 5, REAL_MAP_SIZE);
    scanManager.applyExternalScanPlanned(scan2, 2);
    String externalPrint = scanManager.toString();
    scanManager.revertToConfirmedMap();
    String secondPrint = scanManager.toString();
    Assert.assertEquals("Reverted successfully", firstPrint, secondPrint);
    Assert.assertFalse("External not applied!", externalPrint.equals(firstPrint));
  }
}