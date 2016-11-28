package rover.model;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import rover.RoverAttributes;
import rover.mediators.data.RoverStateInfo;
import rover.mediators.data.ScenarioInfo;
import rover.mediators.data.update.item.ScannerItemType;
import rover.model.maplocation.Coordinate;
import rover.model.maplocation.ItemManager;
import rover.model.scanning.ScanManager;

/**
 * Created by dominic on 27/10/16.
 */
public class RoverInfo {
  private static final int SCAN_RESOLUTION = 2;

  private final RoverAttributes attributes;
  private Coordinate coordinate;
  private Queue<ScannerItemType> payload;
  private ScenarioInfo scenarioInfo;
  private RoverStateInfo roverStateInfo;
  private ScanManager scanManager;

  public RoverInfo(RoverAttributes attributes, RoverStateInfo roverStateInfo, ScenarioInfo scenarioInfo) {
    setScenarioInfo(scenarioInfo);
    setRoverStateInfo(roverStateInfo);
    this.attributes = attributes;
    coordinate = new Coordinate(0, 0);
    payload = new LinkedBlockingQueue<>();
  }

  public RoverAttributes getAttributes() {
    return attributes;
  }

  public void adjustPosition(double xOffset, double yOffset) {
    coordinate = coordinate.moveCoordinate(
            xOffset,
            yOffset,
            scenarioInfo.getWidth(),
            scenarioInfo.getHeight());
  }

  public void setScenarioInfo(ScenarioInfo scenarioInfo) {
    this.scenarioInfo = scenarioInfo;
    scanManager = new ScanManager(scenarioInfo.getWidth(), attributes.getScanRange(), SCAN_RESOLUTION);
  }

  public ScenarioInfo getScenarioInfo() {
    return scenarioInfo;
  }

  public RoverStateInfo getRoverStateInfo() {
    return roverStateInfo;
  }

  public void setRoverStateInfo(RoverStateInfo roverStateInfo) {
    this.roverStateInfo = roverStateInfo;
  }

  public Coordinate getPosition() {
    return coordinate;
  }

  public void addPayload(ScannerItemType scannerItemType) {
    payload.add(scannerItemType);
  }

  public ScannerItemType removePayload() {
    return payload.remove();
  }

  public ScanManager getScanManager() {
    return scanManager;
  }
}
