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
    RoverRoutine returnAndDepositRoutine = getReturnToBaseRoutine(roverInfo)
            .orElse(getDepositRoutine(roverInfo)
                    .orElse(new RoverRoutine(RoutineType.IDLE, Double.MIN_VALUE/2)));


    Double energyToDropPayloadAtBase =  getReturnToBaseRoutine(roverInfo).orElse(new RoverRoutine(RoutineType.IDLE, 0.0)).getTotalCost().getEnergy()
            + (roverInfo.getAttributes().getMaxLoad() * 5);
    if (roverInfo.getCurrentPayload() != 0 && energyRemainingAfterRoutine < energyToDropPayloadAtBase) {
      return returnAndDepositRoutine;
    }
    if(bestRoutine.getTotalCost().getEnergy() > roverInfo.getRoverStateInfo().getEnergy()) {
      return getIdleRoutine();
    }
    return bestRoutine;
  }

  private Optional<RoverRoutine> getDepositRoutine(RoverInfo roverInfo) {
    if (roverInfo.shouldDeposit()) {
      RoverRoutine depositRoutine = new RoverRoutine(RoutineType.COLLECT, (Double.MAX_VALUE/2.0) + (roverInfo.getAttributes().getMaxLoad() * 10));
      for (int i = roverInfo.getCurrentPayload(); i > 0; i--) {
        depositRoutine.addAction(new RoverDeposit(roverInfo, commMan));
      }
      log.info("ROVER DECISION - DEPOSIT CURRENT - POSITION {}", roverInfo.getPosition());
      return Optional.of(depositRoutine);
    } else {
      return Optional.empty();
    }
  }

  private Optional<RoverRoutine> getEarlyReturnToBaseRoutine(RoverInfo roverInfo) {
    boolean isRoverFull = roverInfo.isRoverFull() && roverInfo.getAttributes().getMaxLoad() != 0;
    boolean allItemsCollected = (itemManager.getTotalItemsCollected() == roverInfo.getScenarioInfo().getTotalWorldResources());
    if (isRoverFull || allItemsCollected) {
      log.info("ROVER DECISION - SHOULD RETURN TO BASE - POSITION {}", roverInfo.getPosition());
      return getReturnToBaseRoutine(roverInfo);
    } else {
      return Optional.empty();
    }
  }

  private Optional<RoverRoutine> getReturnToBaseRoutine(RoverInfo roverInfo) {
    if(roverInfo.getDistanceToBase() < 0.05) {
      return Optional.empty();
    } else {
      log.info("ROVER DECISION - RETURN TO BASE - POSITION {}", roverInfo.getPosition());
      RoverRoutine roverRoutine = new RoverRoutine(RoutineType.COLLECT, (Double.MAX_VALUE/2.0) + (roverInfo.getCurrentPayload() * 10) );
      roverRoutine.addAction(new RoverMove(roverInfo, commMan));
      return Optional.of(roverRoutine);
    }

  }

  private RoverRoutine getIdleRoutine() {
    return new RoverRoutine(RoutineType.IDLE, Double.MIN_VALUE/2.0);
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
      Double bestItemDesire = bestItem.getDesirability(
              roverInfo.getPosition(),
              roverInfo.getScenarioInfo().getSize(),
              roverInfo.getAttributes().getMaxSpeed(),
              cargoSpaceRemaining);

      RoverRoutine roverCollectRoutine = new RoverRoutine(RoutineType.COLLECT, bestItemDesire * Math.pow(roverInfo.getAttributes().getMaxLoad(), 3));
      if (roverInfo.getPosition().getDistanceTo(bestItem.getCoordinate(), roverInfo.getScenarioInfo().getSize()) > 0.05) {
        roverCollectRoutine.addAction(new RoverMove(roverInfo, bestItem.getCoordinate(), commMan));
      }
      for (int i = Math.min(cargoSpaceRemaining, bestItem.getCount()); i > 0; i--) {
        roverCollectRoutine.addAction(new RoverCollect(roverInfo, commMan, bestItem, itemManager));
      }
      log.info("ROVER DECISION - COLLECT {} - POSITION {}", bestItem, roverInfo.getPosition());
      return Optional.of(roverCollectRoutine);
    } else {
      log.info("No items to collect!");
      return Optional.empty();
    }
  }

  private Optional<RoverRoutine> getNextBestScan(RoverInfo roverInfo) {
    log.info("Finding best available scan location.");
    if(roverInfo.getAttributes().getScanRange() == 0) {
      return Optional.empty();
    }
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
      log.info("ROVER DECISION - SCAN {} - POSITION {}", scanManager.getRealScanCoordinates(bestScanResult), roverInfo.getPosition());
      return Optional.of(roverRoutine);
    } else {
      return Optional.empty();
    }
  }

  public void setScanManager(ScanManager scanManager) {
    this.scanManager = scanManager;
  }
}
