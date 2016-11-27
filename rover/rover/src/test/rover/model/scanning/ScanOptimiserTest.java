package rover.model.scanning;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by dominic on 27/11/16.
 */
public class ScanOptimiserTest {

  private int MAP_SIZE = 10;
  private int SCAN_RANGE = 4;

  private ScanMap scanMap;

  @Before
  public void setUp() throws Exception {
    scanMap = new ScanMap(MAP_SIZE, MAP_SIZE);
  }

  @Test
  public void emptyMapTestDesire() throws Exception {
    ScanOptimiser scanOptimiser = new ScanOptimiser(MAP_SIZE, MAP_SIZE, SCAN_RANGE);
    System.out.println(scanOptimiser.toStringDesire(scanMap));
  }

  @Test
  public void emptyMapTestValue() throws Exception {
    ScanOptimiser scanOptimiser = new ScanOptimiser(MAP_SIZE, MAP_SIZE, SCAN_RANGE);
    System.out.println(scanOptimiser.toStringValue(scanMap));
  }

  private void addMiddleScan() {
    ScanResult scanResult = new ScanResult(MAP_SIZE/2, MAP_SIZE/2, SCAN_RANGE);
    scanResult.applyScan(scanMap);
  }

  @Test
  public void singleScanMapTestValue() throws Exception {
    addMiddleScan();
    ScanOptimiser scanOptimiser = new ScanOptimiser(MAP_SIZE, MAP_SIZE, SCAN_RANGE);
    System.out.println(scanOptimiser.toStringValue(scanMap));
  }

  @Test
  public void singleScanMapTestDesire() throws Exception {
    addMiddleScan();
    ScanOptimiser scanOptimiser = new ScanOptimiser(MAP_SIZE, MAP_SIZE, SCAN_RANGE);
    System.out.println(scanMap);
    System.out.println(scanOptimiser.toStringDesire(scanMap));
    ScanResult scanResult = new ScanResult(3, 2, SCAN_RANGE);
    scanResult.applyScan(scanMap);
    System.out.println(scanMap);
  }
}