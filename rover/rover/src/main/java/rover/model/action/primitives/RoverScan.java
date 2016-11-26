package rover.model.action.primitives;

import rover.mediators.RoverFacade;
import rover.model.RoverInfo;

/**
 * Created by dominic on 23/11/16.
 */
public class RoverScan extends RoverAction{

  private double scanPower;

  public RoverScan(RoverInfo roverInfo,
                   double scanPower) {
    super(roverInfo);
    this.scanPower = scanPower;
  }

  @Override
  public ActionCost getActionCost() {
    return new ActionCost(
            (10.0*scanPower)/roverInfo.getAttributes().getScanRange(),
            2);
  }

  @Override
  public void execute(RoverFacade roverFacade) {
    roverFacade.scan(scanPower);
  }

  @Override
  public void complete() {
    //TODO Add Scanned Areas
  }

  @Override
  public void failed() {
    //TODO Shouldn't really fail to scan. Check energy levels and maxScanRange
  }

  @Override
  public String toString() {
    return "RoverScan{" +
            "scanPower=" + scanPower +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof RoverScan)) return false;

    RoverScan roverScan = (RoverScan) o;

    return Double.compare(roverScan.scanPower, scanPower) == 0;

  }

  @Override
  public int hashCode() {
    long temp = Double.doubleToLongBits(scanPower);
    return (int) (temp ^ (temp >>> 32));
  }
}
