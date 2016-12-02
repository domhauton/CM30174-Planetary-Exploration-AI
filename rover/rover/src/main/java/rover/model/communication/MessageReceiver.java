package rover.model.communication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rover.controller.BidCollector;
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
  private Logger logger;
  private ScanManager scanManager;
  private RoverInfo roverInfo;
  private final BidCollector bidCollector;
  private final ItemManager itemManager;

  public MessageReceiver(ScanManager scanManager, ItemManager itemManager, RoverInfo roverInfo, BidCollector bidCollector) {
    this.logger = LoggerFactory.getLogger("AGENT");
    this.scanManager = scanManager;
    this.roverInfo = roverInfo;
    this.itemManager = itemManager;
    this.bidCollector = bidCollector;
  }

  public void applyScanPlanned(Integer scanRange, double x, double y) {
    Coordinate coordinate = new Coordinate(x, y, roverInfo.getScenarioInfo().getSize());
    scanManager.applyExternalScanPlanned(coordinate, scanRange);
    logger.info("PROCESSED MESSAGE SCAN PLAN.");
  }

  public void applyScanComplete(Integer scanRange, double x, double y) {
    Coordinate coordinate = new Coordinate(x, y, roverInfo.getScenarioInfo().getSize());
    scanManager.applyExternalScanComplete(coordinate, scanRange);
    logger.info("PROCESSED MESSAGE SCAN COMPLETE.");
  }

  public void clearPlannedScans() {
    scanManager.revertToConfirmedMap();
    logger.info("PROCESSED MESSAGE CLEAR SCAN PLAN.");
  }

  public void clearPlannedCollects() {
    itemManager.revertPlannedCollections();
    logger.info("PROCESSED MESSAGE CLEAR COLLECT PLAN.");
  }

  public void newSolidResourceFound(Integer count, double x, double y) {
    Coordinate coordinate = new Coordinate(x, y, roverInfo.getScenarioInfo().getSize());
    itemManager.foundResource(new Resource(ResourceType.SOLID, coordinate), count);
    logger.info("PROCESSED MESSAGE NEW SOLID FOUND.");
  }

  public void newLiquidResourceFound(Integer count, double x, double y) {
    Coordinate coordinate = new Coordinate(x, y, roverInfo.getScenarioInfo().getSize());
    itemManager.foundResource(new Resource(ResourceType.LIQUID, coordinate), count);
    logger.info("PROCESSED MESSAGE NEW LIQUID FOUND.");
  }

  public void collectionPlanned(double x, double y) {
    Coordinate coordinate = new Coordinate(x, y, roverInfo.getScenarioInfo().getSize());
    itemManager.itemCollectPlanned(coordinate);
    logger.info("PROCESSED MESSAGE COLLECTION PLAN.");
  }

  public void collectionComplete(double x, double y) {
    Coordinate coordinate = new Coordinate(x, y, roverInfo.getScenarioInfo().getSize());
    itemManager.itemCollected(coordinate);
    logger.info("PROCESSED MESSAGE COLLECTION COMPLETE.");
  }

  public void processCollectionFailed(double x, double y) {
    Coordinate coordinate = new Coordinate(x, y, roverInfo.getScenarioInfo().getSize());
    itemManager.itemNonExistent(coordinate);
    logger.info("PROCESSED MESSAGE COLLECTION FAILED.");
  }

  public void receiveActionBid(double x) {
    bidCollector.acceptExternalBid(x);
  }

  void setScanManager(ScanManager scanManager) {
    this.scanManager = scanManager;
    logger.info("SET NEW SCAN MANAGER MESSAGE PROCESSOR.");
  }

  public void setRoverInfo(RoverInfo roverInfo) {
    this.roverInfo = roverInfo;
  }
}
