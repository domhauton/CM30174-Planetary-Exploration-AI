package rover.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import rover.model.action.primitives.RoverCollect;
import rover.model.action.primitives.RoverDeposit;
import rover.model.action.primitives.RoverMove;
import rover.model.action.primitives.RoverScan;
import rover.model.action.routine.RoutineType;
import rover.model.action.routine.RoverRoutine;
import rover.model.collection.ItemManager;
import rover.model.collection.Resource;
import rover.model.communication.CommunicationManager;
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
  private ScanManager scanManager;
  private ItemManager itemManager;
  private CommunicationManager commMan;

  public IntentionGenerator(ItemManager itemManager, ScanManager scanManager, CommunicationManager commMan) {
    log = LoggerFactory.getLogger("AGENT");
    this.itemManager = itemManager;
    this.scanManager = scanManager;
    this.commMan = commMan;
  }

  public RoverRoutine getNextBestRoutine(RoverInfo roverInfo) {
    List<Supplier<Optional<RoverRoutine>>> routineStack = Arrays.asList(
            () -> getDepositRoutine(roverInfo),
            () -> getEarlyReturnToBaseRoutine(roverInfo),
            () -> getCollectRoutine(roverInfo),
            () -> getNextBestScan(roverInfo));

    Iterator<Supplier<Optional<RoverRoutine>>> routineStackIterator = routineStack.iterator();

    Optional<RoverRoutine> bestRoutineOptional = Optional.empty();
    while(!bestRoutineOptional.isPresent() && routineStackIterator.hasNext()) {
      bestRoutineOptional = routineStackIterator.next().get();
    }

    RoverRoutine bestRoutine = bestRoutineOptional.orElse(getIdleRoutine());
    Double energyRemainingAfterRoutine = roverInfo.getRoverStateInfo().getEnergy()
            - bestRoutine.getTotalCost().getEnergy();

    // Emergency return to base check
    RoverRoutine returnAndDepositRoutine = getReturnAndDepositRoutine(roverInfo);

    Double energyToDropPayloadAtBase = returnAndDepositRoutine.getTotalCost().getEnergy();
    if (roverInfo.getCurrentPayload() != 0 && energyRemainingAfterRoutine < energyToDropPayloadAtBase) {
      return returnAndDepositRoutine;
    }
    if(bestRoutine.getTotalCost().getEnergy() > roverInfo.getRoverStateInfo().getEnergy()) {
      return getIdleRoutine();
    }
    return bestRoutine;
  }

  private RoverRoutine getReturnAndDepositRoutine(RoverInfo roverInfo) {
    RoverRoutine depositRoutine = new RoverRoutine(RoutineType.COLLECT, Double.MAX_VALUE);
    depositRoutine.addAction(new RoverMove(roverInfo, commMan));
    for (int i = roverInfo.getCurrentPayload(); i > 0; i--) {
      depositRoutine.addAction(new RoverDeposit(roverInfo, commMan));
    }
    return depositRoutine;
  }

  private Optional<RoverRoutine> getDepositRoutine(RoverInfo roverInfo) {
    if (roverInfo.shouldDeposit()) {
      RoverRoutine depositRoutine = new RoverRoutine(RoutineType.COLLECT, Double.MAX_VALUE);
      for (int i = roverInfo.getCurrentPayload(); i > 0; i--) {
        depositRoutine.addAction(new RoverDeposit(roverInfo, commMan));
      }
      return Optional.of(depositRoutine);
    } else {
      return Optional.empty();
    }
  }

  private Optional<RoverRoutine> getEarlyReturnToBaseRoutine(RoverInfo roverInfo) {
    boolean isRoverFull = roverInfo.isRoverFull();
    boolean allItemsCollected = (itemManager.getTotalItemsCollected() == roverInfo.getScenarioInfo().getTotalWorldResources());
    if (isRoverFull || allItemsCollected) {
      return Optional.of(getReturnToBaseRoutine(roverInfo));
    } else {
      return Optional.empty();
    }
  }

  private RoverRoutine getReturnToBaseRoutine(RoverInfo roverInfo) {
    RoverRoutine roverRoutine = new RoverRoutine(RoutineType.COLLECT, Double.MAX_VALUE);
    roverRoutine.addAction(new RoverMove(roverInfo, commMan));
    return roverRoutine;
  }

  private RoverRoutine getIdleRoutine() {
    return new RoverRoutine(RoutineType.IDLE, 0.0);
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
    if (bestItemOptional.isPresent()) {
      Resource bestItem = bestItemOptional.get();
      log.info("Found item to collect: {}", bestItem);
      Double bestItemDesire = bestItem.getDesirability(
              roverInfo.getPosition(),
              roverInfo.getScenarioInfo().getSize(),
              roverInfo.getAttributes().getMaxSpeed(),
              cargoSpaceRemaining);
      RoverRoutine roverCollectRoutine = new RoverRoutine(RoutineType.COLLECT, bestItemDesire);
      if (roverInfo.getPosition().getDistanceTo(bestItem.getCoordinate(), roverInfo.getScenarioInfo().getSize()) > 0.03) {
        roverCollectRoutine.addAction(new RoverMove(roverInfo, roverInfo.getPosition(), bestItem.getCoordinate(), commMan));
      }
      for (int i = Math.min(roverInfo.getAttributes().getMaxLoad(), bestItem.getCount()); i > 0; i--) {
        roverCollectRoutine.addAction(new RoverCollect(roverInfo, commMan, bestItem, itemManager));
      }
      log.info("Found best collection routine");
      return Optional.of(roverCollectRoutine);
    } else {
      log.info("No items to collect!");
      return Optional.empty();
    }
  }

  private Optional<RoverRoutine> getNextBestScan(RoverInfo roverInfo) {
    log.info("Finding best available scan location.");
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
    if (!bestScanCoordinates.equals(roverInfo.getPosition())) {
      roverRoutine.addAction(new RoverMove(roverInfo, bestScanCoordinates, commMan));
    }
    roverRoutine.addAction(
            new RoverScan(roverInfo,
                    commMan,
                    scanManager,
                    roverInfo.getAttributes().getScanRange(),
                    bestScanResult));
    if (roverRoutine.getValue() > 0) {
      return Optional.of(roverRoutine);
    } else {
      return Optional.empty();
    }
  }

  public void setScanManager(ScanManager scanManager) {
    this.scanManager = scanManager;
  }
}
