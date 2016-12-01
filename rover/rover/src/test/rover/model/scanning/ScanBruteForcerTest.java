package rover.model.scanning;

import org.junit.Before;
import org.junit.Test;

import java.util.stream.Collectors;

import util.Pair;

/**
 * Created by dominic on 27/11/16.
 */
public class ScanBruteForcerTest {

  private final int MAP_SIZE = 20;
  private final int SCAN_RANGE = 10;
  private final int SCAN_RESOLUTION = 3;
  private final int MOVE_SPEED = 3;

  private ScanMap scanMap;

  @Before
  public void setUp() throws Exception {
    scanMap = new ScanMap(MAP_SIZE);
  }

  @Test
  public void emptyMapTestDesire() throws Exception {
    ScanBruteForcer scanBruteForcer = new ScanBruteForcer(MAP_SIZE, MAP_SIZE, SCAN_RANGE);
    System.out.println(scanBruteForcer.toStringScanDesire(scanMap));
  }

  @Test
  public void emptyMapTestValue() throws Exception {
    ScanBruteForcer scanBruteForcer = new ScanBruteForcer(MAP_SIZE, MAP_SIZE, SCAN_RANGE);
    System.out.println(scanBruteForcer.toStringValue(scanMap));
  }

  private void addMiddleScan() {
    GridPos gridCenter = new GridPos(MAP_SIZE/2, MAP_SIZE/2);
    ScanResult scanResult = new ScanResult(gridCenter, SCAN_RANGE);
    scanResult.applyScan(scanMap);
  }

  @Test
  public void singleScanMapTestValue() throws Exception {
    addMiddleScan();
    ScanBruteForcer scanBruteForcer = new ScanBruteForcer(MAP_SIZE, MAP_SIZE, SCAN_RANGE);
    System.out.println(scanBruteForcer.toStringValue(scanMap));
  }

//  @Test
//  public void singleScanMapTestDesire() throws Exception {
//    ScanBruteForcer scanBruteForcer = new ScanBruteForcer(MAP_SIZE, MAP_SIZE, SCAN_RANGE);
//    GridPos roverPos = new GridPos(4, 4);
//    GridPos depotPos = new GridPos(4, 4);
//    final int moveSpeed = 3;
//    addMiddleScan();
//    System.out.println(scanMap.toString());
//    System.out.println(scanBruteForcer.toStringMoveScanCollectDesire(scanMap, roverPos, moveSpeed));
//    System.out.println(scanBruteForcer
//            .getMostDesirable(scanMap, roverPos, moveSpeed, 4)
//            .stream()
//            .map(x -> x.toString(scanMap, roverPos, moveSpeed))
//            .collect(Collectors.joining("\n")));
//
//  }
//
//  @Test
//  public void fullAreaScanTest() throws Exception {
//    ScanBruteForcer scanBruteForcer = new ScanBruteForcer(MAP_SIZE, SCAN_RESOLUTION, SCAN_RANGE);
//    GridPos roverPos = new GridPos(4, 4);
//    int ctr = 0;
//    int totalMovement = 0;
//    while(scanMap.unscannedValue() > 0) {
//      //System.out.println(scanMap);
//      //System.out.println(scanBruteForcer.toStringMoveScanCollectDesire(scanMap, roverPos, depotPos, MOVE_SPEED));
//      ScanResult bestScan = scanBruteForcer.getMostDesirable(scanMap, roverPos, MOVE_SPEED, 1).get(0);
//      bestScan = bestScan.findBestNearby(scanMap, roverPos, MOVE_SPEED).getA();
//      System.out.println(bestScan.toString(scanMap, roverPos, MOVE_SPEED));
//      bestScan.applyScan(scanMap);
//      totalMovement += roverPos.distanceToPos(bestScan.getScanPos(), scanMap.getSize());
//      roverPos = bestScan.getScanPos();
//      ctr++;
//    }
//    System.out.println("Total scans: " + ctr);
//    System.out.println("Distance moved: " + totalMovement);
//  }
//
//  @Test
//  public void fullAreaGradientAscentTest() throws Exception {
//    ScanBruteForcer scanBruteForcer = new ScanBruteForcer(MAP_SIZE, SCAN_RESOLUTION, SCAN_RANGE);
//    GridPos roverPos = new GridPos(4, 4);
//    int ctr = 0;
//    int totalMovement = 0;
//    while(scanMap.unscannedValue() > 0) {
//      System.out.println("Unscanned worth: " + scanMap.unscannedValue());
//      Pair<ScanResult, Integer> currentBest = new ScanResult(roverPos, SCAN_RANGE).findBestNearby(scanMap, roverPos, MOVE_SPEED);
//      if(currentBest.getB() == 0) {
//        System.out.println("Had to randomise for next point!");
//        currentBest = currentBest.getA().findBestRandom(scanMap, roverPos, MOVE_SPEED, 20);
//      }
//      ScanResult bestScan;
//      if(currentBest.getB() == 0) {
//        System.out.println("Had to brute force for next point!");
//        bestScan = scanBruteForcer.getMostDesirable(scanMap, roverPos, MOVE_SPEED, 1).get(0);
//      } else {
//        bestScan = currentBest.getA();
//      }
//      System.out.println(bestScan.toString(scanMap, roverPos, MOVE_SPEED));
//      bestScan.applyScan(scanMap);
//      totalMovement += roverPos.distanceToPos(bestScan.getScanPos(), scanMap.getSize());
//      roverPos = bestScan.getScanPos();
//      ctr++;
//    }
//    System.out.println("Total scans: " + ctr);
//    System.out.println("Distance moved: " + totalMovement);
//  }
}