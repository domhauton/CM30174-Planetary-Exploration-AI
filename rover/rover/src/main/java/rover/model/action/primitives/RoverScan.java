package rover.model.action.primitives;

import rover.mediators.RoverFacade;
import rover.model.roverinfo.RoverInfo;
import rover.model.scanning.ScanManager;
import rover.model.scanning.ScanResult;

/**
 * Created by dominic on 23/11/16.
 */
public class RoverScan extends RoverAction{

  private double scanPower;
  private ScanResult scanResult;
  private ScanManager scanManager;

  public RoverScan(RoverInfo roverInfo,
                   ScanManager scanManager,
                   double scanPower,
                   ScanResult scanResult) {
    super(roverInfo);
    this.scanPower = scanPower;
    this.scanResult = scanResult;
  }

  @Override
  public ActionCost getActionCost() {
    return new ActionCost(
            (10.0*scanPower)/roverInfo.getAttributes().getScanRange(),
            5);
  }

  @Override
  public void execute(RoverFacade roverFacade) {
    roverFacade.scan(scanPower);
  }

  @Override
  public void complete() {
    scanManager.applyScan(scanResult);
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
