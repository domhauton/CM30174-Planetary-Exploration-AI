package rover.model.scanning;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by dominic on 27/11/16.
 */
public class ScanOptimiser {
  private ScanResult[][] scanResults;

  public ScanOptimiser(int xSize, int ySize, int scanRange) {
    scanResults = new ScanResult[xSize][ySize];
    for (int x = 0; x < xSize; x++) {
      for (int y = 0; y < ySize; y++) {
        scanResults[x][y] = new ScanResult(x, y, scanRange);
      }
    }
  }

  public String toStringDesire(final ScanMap scanMap) {
    return toStringValue((ScanResult scanRes) -> scanRes.scanDesirability(scanMap));
  }

  public String toStringValue(final ScanMap scanMap) {
    return toStringValue((ScanResult scanRes) -> scanRes.calculateScanSearchValue(scanMap));
  }

  private String toStringValue(final Function<ScanResult, Integer> valueExtractor) {
    return "ScanMap{ \n" +
            Stream.of(scanResults)
                    .map(x -> Stream.of(x)
                            .map(valueExtractor)
                            .map(val -> String.format("%04d", val))
                            .collect(Collectors.joining("|")))
                    .collect(Collectors.joining("\n")) +
            "\n}";
  }
}
