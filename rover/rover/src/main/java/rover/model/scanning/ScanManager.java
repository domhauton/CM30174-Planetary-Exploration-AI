package rover.model.scanning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rover.model.maplocation.Coordinate;
import util.Pair;

/**
 * Created by dominic on 24/11/16.
 */
public class ScanManager {
  private final int gridPerUnit;
  private final int gridSize;
  private final ScanBruteForcer scanBruteForcer;
  private final Logger log;
  private ScanMap scanMap;
  private ScanMap futureMap;

  public ScanManager(int realMapWidth, int realScanRange, int gridPerUnit) {
    log = LoggerFactory.getLogger("AGENT");
    this.gridPerUnit = gridPerUnit;
    this.gridSize = real2Grid(realMapWidth);
    log.info("Creating scan map of size {}x{}", realMapWidth, realMapWidth);
    scanMap = new ScanMap(gridSize);
    log.info("Creating brute forcer of size {}x{}", realMapWidth, realMapWidth);
    scanBruteForcer = new ScanBruteForcer(gridSize, gridPerUnit*5, real2Grid(realScanRange));
    revertToConfirmedMap();
  }

  public void revertToConfirmedMap() {
    futureMap = scanMap.clone();
  }

  public void applyExternalScanPlanned(Coordinate realScanCoordinate, Integer range) {
    ScanResult externalScanResult = new ScanResult(real2Grid(realScanCoordinate), real2Grid(range));
    externalScanResult.applyScan(futureMap);
  }

  public void applyExternalScanComplete(Coordinate realScanCoordinate, Integer range) {
    ScanResult externalScanResult = new ScanResult(real2Grid(realScanCoordinate), real2Grid(range));
    applyScan(externalScanResult);
  }

  public void applyScan(ScanResult scanResult) {
    log.info("Applying scan result to both maps");
    scanResult.applyScan(scanMap);
    scanResult.applyScan(futureMap);
  }

  public double getDiscoveryChance(ScanResult scanResult) {
    return scanResult.discoveryChance(futureMap);
  }

  public ScanResult getNextBest(Coordinate realRoverCoordinate, int realMoveSpeed) {
    int gridMoveSpeed = real2Grid(realMoveSpeed);
    GridPos roverPos = real2Grid(realRoverCoordinate);
    ScanResult bestScan = scanBruteForcer.getMostDesirable(futureMap, roverPos, gridMoveSpeed, 1)
            .get(0);
    return bestScan.findBestNearby(futureMap, roverPos, gridMoveSpeed).getA();
  }

  public ScanResult getNextBestScanQuick(Coordinate realRoverCoordinate, int realMoveSpeed, int realScanRange) {
    int gridMoveSpeed = real2Grid(realMoveSpeed);
    int gridScanRange = real2Grid(realScanRange);
    GridPos roverPos = real2Grid(realRoverCoordinate);
    log.info("Performing hill climb from current rover location");
    Pair<ScanResult, Integer> currentBest = new ScanResult(roverPos, gridScanRange)
            .findBestNearby(futureMap, roverPos, gridMoveSpeed);
    if(currentBest.getB() < 1000) {
      log.info("Performing hill climb from 10 random locations");
      Pair<ScanResult, Integer> randomBest = currentBest.getA().findBestRandom(futureMap, roverPos, gridMoveSpeed, 10);
      currentBest = currentBest.getB() > randomBest.getB() ? currentBest : randomBest;
    }
    if (currentBest.getB() == 0) {
      log.info("Brute forcing next best location.");
      return getNextBest(realRoverCoordinate, realMoveSpeed);
    } else {
      return currentBest.getA();
    }
  }

  public Coordinate getRealScanCoordinates(ScanResult scanResult) {
    return grid2Real(scanResult.getScanPos());
  }

  private GridPos real2Grid(Coordinate real) {
    return new GridPos((int) Math.floor(real.getX() * gridPerUnit),
            (int) Math.floor(real.getY() * gridPerUnit));
  }

  private int real2Grid(int real) {
    return gridPerUnit * real;
  }

  private Coordinate grid2Real(GridPos grid) {
    return new Coordinate((double) (grid.getX() / gridPerUnit), (double) (grid.getY() / gridPerUnit));
  }

  @Override
  public String toString() {
    return "ScanManager{" +
            "scanMap=" + scanMap +
            ", futureMap=" + futureMap +
            '}';
  }
}
