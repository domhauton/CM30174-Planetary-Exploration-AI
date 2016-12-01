package rover.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Optional;

import rover.model.action.primitives.RoverAction;
import rover.model.action.primitives.RoverCollect;
import rover.model.action.primitives.RoverDeposit;
import rover.model.action.primitives.RoverMove;
import rover.model.action.routine.RoutineType;
import rover.model.action.routine.RoverRoutine;
import rover.model.action.primitives.RoverScan;
import rover.model.collection.ItemManager;
import rover.model.collection.Resource;
import rover.model.maplocation.Coordinate;
import rover.model.roverinfo.RoverInfo;
import rover.model.scanning.ScanManager;
import rover.model.scanning.ScanResult;

/**
 * Created by dominic on 28/11/16.
 */
public class IntentionGenerator {
  private static final int SCAN_RESOLUTION = 2;

  private Logger log;
  private HashMap<Integer, ScanManager> scanManagers;
  private ItemManager itemManager;

  public IntentionGenerator(ItemManager itemManager) {
    log = LoggerFactory.getLogger("AGENT");
    scanManagers = new HashMap<>(2);
    this.itemManager = itemManager;
  }

  public RoverAction getNextBestAction(RoverInfo roverInfo) {
    double distanceToBase = roverInfo.getDistanceToBase();
    if(distanceToBase < 0.05 && roverInfo.getCurrentPayload() != 0) {
      return new RoverDeposit(roverInfo);
    }

    int remainingCargoSpace = roverInfo.getAttributes().getMaxLoad() - roverInfo.getCurrentPayload();
    RoverMove returnToBase = new RoverMove(roverInfo, Coordinate.ORIGIN);
    if((roverInfo.getAttributes().getMaxLoad() != 0 && remainingCargoSpace == 0)
            || (itemManager.getTotalItemsCollected() == roverInfo.getScenarioInfo().getTotalWorldResources())){
      return returnToBase;
    }

    RoverRoutine nextBestRoutine = getCollectRoutine(roverInfo)
            .orElseGet(() -> getNextBestScan(roverInfo));
    // Emergency return to base check
    Double energyRemainingAfterRoutine = roverInfo.getRoverStateInfo().getEnergy().orElse(0.0)
            - nextBestRoutine.getTotalCost().getEnergy();
    RoverDeposit roverDeposit = new RoverDeposit(roverInfo);
    Double energyToDropPayloadAtBase = returnToBase.getActionCost().getEnergy() +
            roverDeposit.getActionCost().getEnergy()*roverInfo.getCurrentPayload();
    if(roverInfo.getCurrentPayload() != 0 && energyRemainingAfterRoutine < energyToDropPayloadAtBase) {
      return returnToBase;
    }
    return nextBestRoutine.getFirstAction();
  }

  private Optional<RoverRoutine> getCollectRoutine(RoverInfo roverInfo) {
    log.info("Finding best collect routine.");
    final int cargoSpaceRemaining = roverInfo.getAttributes().getMaxLoad()
            - roverInfo.getCurrentPayload();
    Optional<Resource> bestItemOptional = itemManager.getMostDesirable(
            roverInfo.getAttributes().getMaxSpeed(),
            cargoSpaceRemaining,
            roverInfo.getPosition(),
            roverInfo.getScenarioInfo().getSize(),
            roverInfo.getAttributes().getCargoType());
    if(bestItemOptional.isPresent()) {
      Resource bestItem = bestItemOptional.get();
      log.info("Found item to collect: {}", bestItem);
      Double bestItemDesire = bestItem.getDesirability(
              roverInfo.getPosition(),
              roverInfo.getScenarioInfo().getSize(),
              roverInfo.getAttributes().getMaxSpeed(),
              cargoSpaceRemaining);
      RoverRoutine roverCollectRoutine = new RoverRoutine(RoutineType.COLLECT, bestItemDesire);
      if(roverInfo.getPosition().getDistanceTo(bestItem.getCoordinate(), roverInfo.getScenarioInfo().getSize()) > 0.03) {
        roverCollectRoutine.addAction(new RoverMove(roverInfo, roverInfo.getPosition(), bestItem.getCoordinate()));
      }
      for(int i = Math.min(roverInfo.getAttributes().getMaxLoad(), bestItem.getCount()); i > 0; i--) {
        roverCollectRoutine.addAction(new RoverCollect(roverInfo, bestItem, itemManager));
      }
      log.info("Found best collection routine");
      return Optional.of(roverCollectRoutine);
    } else {
      log.info("No items to collect!");
      return Optional.empty();
    }
  }

  private RoverRoutine getNextBestScan(RoverInfo roverInfo) {
    log.info("Finding best available scan location.");
    ScanManager scanManager = scanManagers.computeIfAbsent(
                    roverInfo.getScenarioInfo().getSize(),
                    x -> new ScanManager(x, roverInfo.getAttributes().getScanRange(), SCAN_RESOLUTION));
    ScanResult bestScanResult = scanManager.getNextBestScanQuick(roverInfo.getPosition(),
            roverInfo.getAttributes().getMaxSpeed(), roverInfo.getAttributes().getScanRange());
    Coordinate bestScanCoordinates = scanManager.getRealScanCoordinates(bestScanResult);
    log.info("Rover: {},{} Action: {},{}. Discovery chance: {}",
            roverInfo.getPosition().getX(),
            roverInfo.getPosition().getY(),
            bestScanCoordinates.getX(),
            bestScanCoordinates.getY(),
            scanManager.getDiscoveryChance(bestScanResult));
    RoverRoutine roverRoutine = new RoverRoutine(RoutineType.SCAN,
            scanManager.getDiscoveryChance(bestScanResult));
    if(!bestScanCoordinates.equals(roverInfo.getPosition())){
      roverRoutine.addAction(new RoverMove(roverInfo, bestScanCoordinates));
    }
    roverRoutine.addAction(
            new RoverScan(roverInfo,
                    scanManager,
                    roverInfo.getAttributes().getScanRange(),
                    bestScanResult));
    return roverRoutine;
  }
}
