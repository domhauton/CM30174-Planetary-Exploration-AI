package rover.model.scanning;

import rover.model.maplocation.Coordinate;
import util.Pair;

/**
 * Created by dominic on 24/11/16.
 */
public class ScanManager {
  private final int gridPerUnit;
  private final ScanMap scanMap;
  private final ScanBruteForcer scanBruteForcer;

  public ScanManager(int realMapWidth, int realScanRange, int gridPerUnit) {
    this.gridPerUnit = gridPerUnit;
    int gridSize = real2Grid(realMapWidth);
    scanMap = new ScanMap(gridSize);
    scanBruteForcer = new ScanBruteForcer(gridSize, gridPerUnit, real2Grid(realScanRange));
  }

  public ScanResult getNextBest(Coordinate realRoverCoordinate, int realMoveSpeed) {
    int gridMoveSpeed = real2Grid(realMoveSpeed);
    GridPos roverPos = real2Grid(realRoverCoordinate);
    ScanResult bestScan = scanBruteForcer.getMostDesirable(scanMap, roverPos, gridMoveSpeed, 1)
            .get(0);
    return bestScan.findBestNearby(scanMap, roverPos, gridMoveSpeed).getA();
  }

  public ScanResult getNextBestScanQuick(Coordinate realRoverCoordinate, int realMoveSpeed, int realScanRange) {
    int gridMoveSpeed = real2Grid(realMoveSpeed);
    int gridScanRange = real2Grid(realScanRange);
    GridPos roverPos = real2Grid(realRoverCoordinate);
    Pair<ScanResult, Integer> currentBest = new ScanResult(roverPos, gridScanRange)
            .findBestNearby(scanMap, roverPos, gridMoveSpeed);
    if (currentBest.getB() == 0) {
      currentBest = currentBest.getA().findBestRandom(scanMap, roverPos, gridMoveSpeed, 40);
    }
    if (currentBest.getB() == 0) {
      return getNextBest(realRoverCoordinate, realMoveSpeed);
    } else {
      return currentBest.getA();
    }
  }

  private GridPos real2Grid(Coordinate real) {
    return new GridPos((int) Math.floor(real.getX() * gridPerUnit),
            (int) Math.floor(real.getY() * gridPerUnit));
  }

  private int real2Grid(int real) {
    return gridPerUnit * real;
  }

  public Coordinate grid2Real(GridPos grid) {
    return new Coordinate((double) (grid.getX() / gridPerUnit), (double) (grid.getY() / gridPerUnit));
  }
}
