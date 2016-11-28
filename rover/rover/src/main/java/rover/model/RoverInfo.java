package rover.model;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import rover.RoverAttributes;
import rover.mediators.data.ScenarioInfo;
import rover.mediators.data.update.item.ScannerItemType;
import rover.model.maplocation.Coordinate;
import rover.model.maplocation.ItemManager;

/**
 * Created by dominic on 27/10/16.
 */
public class RoverInfo {
  private final RoverAttributes attributes;
  private Coordinate coordinate;
  private Queue<ScannerItemType> payload;
  private ItemManager itemManager;
  private ScenarioInfo scenarioInfo;

  public RoverInfo(RoverAttributes attributes) {
    this.attributes = attributes;
    coordinate = CoordinateFactory.buildCartesian(0, 0);
    itemManager = new ItemManager();
    payload = new LinkedBlockingQueue<>();
    scenarioInfo = new ScenarioInfo(
            Integer.MAX_VALUE,
            Integer.MAX_VALUE,
            false,
            Integer.MAX_VALUE,
            1);
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

  public Coordinate getPosition() {
    return coordinate;
  }

  public ScenarioInfo getScenarioInfo() {
    return scenarioInfo;
  }

  public void addPayload(ScannerItemType scannerItemType) {
    payload.add(scannerItemType);
  }

  public ScannerItemType removePayload() {
    return payload.remove();
  }
}
