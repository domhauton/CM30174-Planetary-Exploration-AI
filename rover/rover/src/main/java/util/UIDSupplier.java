package util;

/**
 * Created by dominic on 30/11/16.
 */
public class UIDSupplier {
  private int current = 0;

  public synchronized Integer next() {
    return current++;
  }
}
