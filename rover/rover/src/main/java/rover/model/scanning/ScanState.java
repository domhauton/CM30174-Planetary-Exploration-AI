package rover.model.scanning;

/**
 * Created by dominic on 25/11/16.
 */
enum ScanState {
  SCANNED("S", 2, 2),
  PARTIAL("P", 1, -5),
  UNKNOWN("U", 0, 0);

  private final String tile;
  private final int value;
  private final int desirable;

  ScanState(String tile, int value, int desirable) {
    this.tile = tile;
    this.value = value;
    this.desirable = desirable;
  }

  public int getValue() {
    return value;
  }

  public int getDesirable() {
    return desirable;
  }

  @Override
  public String toString() {
    return tile;
  }
}
