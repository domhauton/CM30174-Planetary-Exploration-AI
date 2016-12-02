package rover.model.action.primitives;

import rover.mediators.RoverFacade;
import rover.model.communication.CommunicationManager;
import rover.model.communication.ontology.inform.CollectionPlanned;
import rover.model.communication.ontology.inform.ScanComplete;
import rover.model.communication.ontology.inform.ScanPlanned;
import rover.model.maplocation.Coordinate;
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
                   CommunicationManager communicationManager,
                   ScanManager scanManager,
                   double scanPower,
                   ScanResult scanResult) {
    super(roverInfo, communicationManager);
    this.scanPower = scanPower;
    this.scanResult = scanResult;
    this.scanManager = scanManager;
  }

  @Override
  public ActionCost getActionCost() {
    return new ActionCost(
            (10.0*scanPower)/roverInfo.getAttributes().getScanRange(),
            5);
  }

  @Override
  public void selected() {
    Coordinate scanCoordinate = scanManager.getRealScanCoordinates(scanResult);
    String message = new ScanPlanned()
            .generateCommand((int) scanPower, scanCoordinate.getX(), scanCoordinate.getY());
    communicationManager.sendAll(message);
  }

  @Override
  public void execute(RoverFacade roverFacade) {
    roverFacade.scan(scanPower);
  }

  @Override
  public void complete() {
    scanManager.applyScan(scanResult);
    Coordinate scanCoordinate = scanManager.getRealScanCoordinates(scanResult);
    String message = new ScanComplete()
            .generateCommand((int) scanPower, scanCoordinate.getX(), scanCoordinate.getY());
    communicationManager.sendAll(message);
  }

  @Override
  public void failed() {
    // Nothing to change if failed
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
