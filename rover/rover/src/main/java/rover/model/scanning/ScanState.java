package rover.model.scanning;

/**
 * Created by dominic on 25/11/16.
 */
enum ScanState {
  SCANNED("S", 2),
  PARTIAL("P", 1),
  UNKNOWN("U", 0);

  private final String tile;
  private final int value;

  ScanState(String tile, int value) {
    this.tile = tile;
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  @Override
  public String toString() {
    return tile;
  }
}
