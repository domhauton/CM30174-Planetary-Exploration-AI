package rover.model.scanning;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import util.Pair;

/**
 * Created by dominic on 25/11/16.
 */
public class ScanResult {
  private static double COLLECT_EFFORT_IMPORTANCE = 0.7;
  private static double MOVE_TO_SCAN_EFFORT_IMPORTANCE = 0.7;

  private final GridPos scanPos;
  private final int scanRange;

  ScanResult(GridPos scanPos, final int scanRange) {
    this.scanPos = scanPos;
    this.scanRange = scanRange;
  }

  private Pair<HashSet<GridPos>, HashSet<GridPos>> scanRasterizer() {
    return scanRasterizer(scanPos, scanRange);
  }

  private Pair<HashSet<GridPos>, HashSet<GridPos>> scanRasterizer(GridPos scanPos, int scanRange) {
    HashSet<GridPos> newScanned = new HashSet<>();
    HashSet<GridPos> newPartial = new HashSet<>();
    int x = (scanRange * 2) - 1;
    int y = 0;
    int err = 0;

    while (x >= y) {
      newPartial.addAll(getMirroredValueSet(scanPos.getX(), scanPos.getY(), x, y));
      for (int tmpX = x - 1; tmpX >= y; tmpX--) {
        newScanned.addAll(getMirroredValueSet(scanPos.getX(), scanPos.getY(), tmpX, y));
      }
      y += 1;
      err += 1 + 2 * y;
      if (2 * (err - x) + 1 > 0) {
        x -= 1;
        err += 1 - 2 * x;
      }
    }
    return new Pair<>(newScanned, newPartial);
  }

  private Set<GridPos> getMirroredValueSet(int xCenter, int yCenter, int x, int y) {
    Set<GridPos> coordinateSet = new HashSet<>(8);
    coordinateSet.add(new GridPos(xCenter + x, yCenter + y));
    coordinateSet.add(new GridPos(xCenter + y, yCenter + x));
    coordinateSet.add(new GridPos(xCenter - y, yCenter + x));
    coordinateSet.add(new GridPos(xCenter - x, yCenter + y));
    coordinateSet.add(new GridPos(xCenter - x, yCenter - y));
    coordinateSet.add(new GridPos(xCenter - y, yCenter - x));
    coordinateSet.add(new GridPos(xCenter + y, yCenter - x));
    coordinateSet.add(new GridPos(xCenter + x, yCenter - y));
    return coordinateSet;
  }

  void applyScan(ScanMap scanMap) {
    Pair<HashSet<GridPos>, HashSet<GridPos>> positions = scanRasterizer();
    positions.getB().forEach(val -> scanMap.put(val.getX(), val.getY(), ScanState.PARTIAL));
    positions.getA().forEach(val -> scanMap.put(val.getX(), val.getY(), ScanState.SCANNED));
  }

  double discoveryChance(ScanMap scanMap) {
    return ((double) calculateScanSearchValue(scanMap) / (double) scanMap.unscannedValue());
  }

  Pair<ScanResult, Integer> findBestRandom(ScanMap scanMap,
                                           final GridPos roverPos,
                                           int movementDesire,
                                           int attempts) {
    HashSet<GridPos> previouslyTried = new HashSet<>();
    return IntStream.range(0, attempts)
            .boxed()
            .map((x) -> new GridPos(scanMap.normaliseCoordinate((int) (Math.random() * scanMap.getSize())), scanMap.normaliseCoordinate((int) (Math.random() * scanMap.getSize()))))
            .map((GridPos pos) -> new ScanResult(pos, scanRange))
            .map((ScanResult scan) -> new Pair<>(scan, scan.moveScanCollectDesirability(scanMap, roverPos, movementDesire)))
            .map(pair -> pair.getA().findBestNearby(scanMap, roverPos, movementDesire, pair.getB(), previouslyTried))
            .sorted((o1, o2) -> -o1.getB().compareTo(o2.getB()))
            .findFirst().orElse(new Pair<>(this, 0));
  }

  Pair<ScanResult, Integer> findBestNearby(final ScanMap scanMap,
                                           final GridPos roverPos,
                                           int movementDesire) {
    return findBestNearby(scanMap, roverPos, movementDesire, new HashSet<>());
  }

  private Pair<ScanResult, Integer> findBestNearby(
          final ScanMap scanMap,
          final GridPos roverPos,
          int movementDesire,
          HashSet<GridPos> previouslyTried) {
    return findBestNearby(
            scanMap,
            roverPos,
            movementDesire,
            moveScanCollectDesirability(scanMap, roverPos, movementDesire),
            previouslyTried);
  }

  private Pair<ScanResult, Integer> findBestNearby(
          final ScanMap scanMap,
          final GridPos roverPos,
          int movementDesire,
          int desireForCurrent,
          HashSet<GridPos> previouslyTried) {
    previouslyTried.add(scanPos);
    Pair<ScanResult, Integer> retPair = new Pair<>(this, desireForCurrent);
    Optional<Pair<ScanResult, Integer>> nextBest;
    do {
      nextBest = retPair.getA().gradientAscent(
              scanMap,
              roverPos,
              movementDesire,
              retPair.getB(),
              previouslyTried,
              1);
      retPair = nextBest.orElse(retPair);
    } while (nextBest.isPresent());
    return retPair;
  }

  private Optional<Pair<ScanResult, Integer>> gradientAscent(final ScanMap scanMap,
                                                             final GridPos roverPos,
                                                             int movementDesire,
                                                             int desireForCurrent,
                                                             Set<GridPos> previouslyTried,
                                                             int distance) {
    Optional<Pair<ScanResult, Integer>> bestPair = Stream.of(
            new GridPos(scanMap.normaliseCoordinate(scanPos.getX()), scanMap.normaliseCoordinate(scanPos.getY() + distance)),
            new GridPos(scanMap.normaliseCoordinate(scanPos.getX()), scanMap.normaliseCoordinate(scanPos.getY() - distance)),
            new GridPos(scanMap.normaliseCoordinate(scanPos.getX() - distance), scanMap.normaliseCoordinate(scanPos.getY())),
            new GridPos(scanMap.normaliseCoordinate(scanPos.getX() + distance), scanMap.normaliseCoordinate(scanPos.getY())),
            new GridPos(scanMap.normaliseCoordinate(scanPos.getX()), scanMap.normaliseCoordinate(scanPos.getY() + (distance + scanRange))),
            new GridPos(scanMap.normaliseCoordinate(scanPos.getX()), scanMap.normaliseCoordinate(scanPos.getY() - (distance + scanRange))),
            new GridPos(scanMap.normaliseCoordinate(scanPos.getX() - (distance + scanRange)), scanMap.normaliseCoordinate(scanPos.getY())),
            new GridPos(scanMap.normaliseCoordinate(scanPos.getX() + (distance + scanRange)), scanMap.normaliseCoordinate(scanPos.getY())))
            .filter(x -> !previouslyTried.contains(x))
            .peek(previouslyTried::add)
            .map((GridPos pos) -> new ScanResult(pos, scanRange))
            .map((ScanResult scan) -> new Pair<>(scan, scan.moveScanCollectDesirability(scanMap, roverPos, movementDesire)))
            .sorted((o1, o2) -> -o1.getB().compareTo(o2.getB()))
            .findFirst();
    return bestPair.isPresent() && bestPair.get().getB() > desireForCurrent ? bestPair : Optional.empty();
  }

  int calculateScanSearchValue(ScanMap scanMap) {
    Pair<HashSet<GridPos>, HashSet<GridPos>> positions = scanRasterizer();
    int partialValue = positions.getB().stream()
            .filter(val -> scanMap.get(val.getX(), val.getY()).getValue() < ScanState.PARTIAL.getValue())
            .mapToInt(val -> ScanState.PARTIAL.getValue() - scanMap.get(val.getX(), val.getY()).getValue())
            .sum();
    int scannedValue = positions.getA().stream()
            .filter(val -> scanMap.get(val.getX(), val.getY()).getValue() < ScanState.SCANNED.getValue())
            .mapToInt(val -> ScanState.SCANNED.getValue() - scanMap.get(val.getX(), val.getY()).getValue())
            .sum();
    return partialValue + scannedValue;
  }

  Integer moveScanCollectDesirability(ScanMap scanMap,
                                      final GridPos roverPos,
                                      int movementDesire) {
    double collectEffort = (scanPos.distanceToOrigin(scanMap.getSize()) / scanMap.getLongestTravelDistance())
            * COLLECT_EFFORT_IMPORTANCE;
    return (int) (moveScanDesirability(scanMap, roverPos, movementDesire) * (1 - collectEffort));
  }

  Integer moveScanDesirability(ScanMap scanMap,
                               final GridPos roverPos,
                               int movementDesire) {
    double moveEffort = (scanPos.distanceToPos(roverPos, scanMap.getSize()) / scanMap.getLongestTravelDistance())
            * MOVE_TO_SCAN_EFFORT_IMPORTANCE;
    double moveMultiplierAdjusted = movementDesire / scanMap.getLongestTravelDistance();
    moveMultiplierAdjusted = Math.min(moveMultiplierAdjusted, 1);
    moveEffort = moveEffort * (1 - moveMultiplierAdjusted);
    return (int) (scanDesirability(scanMap) * ((1 - moveEffort) / 4));
  }

  Integer scanDesirability(ScanMap scanMap) {
    Pair<HashSet<GridPos>, HashSet<GridPos>> positions = scanRasterizer();
    int partialValue = positions.getB().stream()
            .filter(val -> scanMap.get(val.getX(), val.getY()).getDesirable() < ScanState.PARTIAL.getDesirable())
            .mapToInt(val -> ScanState.PARTIAL.getDesirable() - scanMap.get(val.getX(), val.getY()).getDesirable())
            .sum();
    int scannedValue = positions.getA().stream()
            .filter(val -> scanMap.get(val.getX(), val.getY()).getDesirable() < ScanState.SCANNED.getDesirable())
            .mapToInt(val -> ScanState.SCANNED.getDesirable() - scanMap.get(val.getX(), val.getY()).getDesirable())
            .sum();
    return partialValue + scannedValue;
  }

  GridPos getScanPos() {
    return scanPos;
  }

  private int getScanRange() {
    return scanRange;
  }

  public String toString(ScanMap scanMap, GridPos roverPos, int moveSpeed) {
    return "ScanResult{" +
            "scanPos=" + scanPos +
            ", value=" + calculateScanSearchValue(scanMap) +
            ", desire=" + moveScanCollectDesirability(scanMap, roverPos, moveSpeed) +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ScanResult)) return false;

    ScanResult that = (ScanResult) o;

    return getScanRange() == that.getScanRange() && (getScanPos() != null
            ? getScanPos().equals(that.getScanPos()) : that.getScanPos() == null);

  }

  @Override
  public int hashCode() {
    int result = getScanPos() != null ? getScanPos().hashCode() : 0;
    result = 31 * result + getScanRange();
    return result;
  }
}
