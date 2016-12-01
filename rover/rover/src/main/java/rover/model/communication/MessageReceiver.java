package rover.model.communication;

import rover.mediators.data.update.item.ResourceType;
import rover.model.collection.ItemManager;
import rover.model.collection.Resource;
import rover.model.maplocation.Coordinate;
import rover.model.roverinfo.RoverInfo;
import rover.model.scanning.ScanManager;

/**
 * Created by dominic on 01/12/16.
 */
public class MessageReceiver {
  private ScanManager scanManager;
  private final RoverInfo roverInfo;
  private final ItemManager itemManager;

  public MessageReceiver(ScanManager scanManager, ItemManager itemManager, RoverInfo roverInfo) {
    this.scanManager = scanManager;
    this.roverInfo = roverInfo;
    this.itemManager = itemManager;
  }

  public void applyScanPlanned(Integer scanRange, double x, double y) {
    Coordinate coordinate = new Coordinate(x, y, roverInfo.getScenarioInfo().getSize());
    scanManager.applyExternalScanPlanned(coordinate, scanRange);
  }

  public void applyScanComplete(Integer scanRange, double x, double y) {
    Coordinate coordinate = new Coordinate(x, y, roverInfo.getScenarioInfo().getSize());
    scanManager.applyExternalScanComplete(coordinate, scanRange);
  }

  public void clearPlannedScans() {
    scanManager.revertToConfirmedMap();
  }

  public void clearPlannedCollects() {
    itemManager.revertPlannedCollections();
  }

  public void newSolidResourceFound(Integer count, double x, double y) {
    Coordinate coordinate = new Coordinate(x, y, roverInfo.getScenarioInfo().getSize());
    itemManager.foundResource(new Resource(ResourceType.SOLID, coordinate), count);
  }

  public void newLiquidResourceFound(Integer count, double x, double y) {
    Coordinate coordinate = new Coordinate(x, y, roverInfo.getScenarioInfo().getSize());
    itemManager.foundResource(new Resource(ResourceType.LIQUID, coordinate), count);
  }

  public void setScanManager(ScanManager scanManager) {
    this.scanManager = scanManager;
  }
}
