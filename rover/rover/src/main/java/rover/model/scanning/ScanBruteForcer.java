package rover.model.scanning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import util.Pair;

/**
 * Created by dominic on 27/11/16.
 */
public class ScanBruteForcer {

  private ScanResult[][] scanResults;
  private int resolution;
  private Logger logger;

  public ScanBruteForcer(int size, int resolution, int scanRange) throws IllegalArgumentException {
    logger = LoggerFactory.getLogger("AGENT");
    if( ((Math.sqrt(2)/2) * resolution) > scanRange) {
      logger.error("Logger resolution too low for scan range.");
    }
    this.resolution = resolution;
    int relevantSize = posToOptimiserRes(size);
    scanResults = new ScanResult[relevantSize][relevantSize];
    for (int x = 0; x < relevantSize; x++) {
      for (int y = 0; y < relevantSize; y++) {
        GridPos scanPos = new GridPos(x * resolution, y * resolution);
        scanResults[x][y] = new ScanResult(scanPos, scanRange);
      }
    }
  }

  public List<ScanResult> getMostDesirable(final ScanMap scanMap,
                                           final GridPos roverPos,
                                           final int movementDesire,
                                           final int limit) {
    HashSet<GridPos> usedGridPos = new HashSet<>();
    return Stream.of(scanResults)
            .flatMap(Stream::of)
            //.map((ScanResult scan) -> scan.findBestNearby(scanMap, roverPos, movementDesire, usedGridPos))
            .map((ScanResult scan) -> new Pair<>(scan, scan.moveScanCollectDesirability(scanMap, roverPos, movementDesire)))
            .sorted((o1, o2) -> -o1.getB().compareTo(o2.getB()))
            .map(Pair::getA)
            .limit(limit)
            .collect(Collectors.toList());
  }

  private int posToOptimiserRes(int pos) {
    return pos / resolution;
  }

  String toStringMoveScanCollectDesire(final ScanMap scanMap,
                                              GridPos roverPos,
                                              int moveEffort) {
    return toStringValue((ScanResult scanRes) ->
            scanRes.moveScanCollectDesirability(scanMap, roverPos, moveEffort));
  }

  String toStringMoveScanDesire(final ScanMap scanMap, GridPos roverPos, int moveEffort) {
    return toStringValue((ScanResult scanRes) ->
            scanRes.moveScanDesirability(scanMap, roverPos, moveEffort));
  }

  String toStringScanDesire(final ScanMap scanMap) {
    return toStringValue((ScanResult scanRes) -> scanRes.scanDesirability(scanMap));
  }

  String toStringValue(final ScanMap scanMap) {
    return toStringValue((ScanResult scanRes) -> scanRes.calculateScanSearchValue(scanMap));
  }

  private String toStringValue(final Function<ScanResult, Number> valueExtractor) {
    return "ScanBruteForcer{ \n" +
            Stream.of(scanResults)
                    .map(x -> Stream.of(x)
                            .map(valueExtractor)
                            .map(Number::intValue)
                            .map(val -> String.format("%04d", val))
                            .collect(Collectors.joining("|")))
                    .collect(Collectors.joining("\n")) +
            "\n}";
  }
}
