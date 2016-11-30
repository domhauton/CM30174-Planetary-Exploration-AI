package rover.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Optional;

import rover.model.action.primitives.RoverAction;
import rover.model.action.primitives.RoverMove;
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

  public IntentionGenerator() {
    log = LoggerFactory.getLogger("AGENT");
    scanManagers = new HashMap<>(2);
    itemManager = new ItemManager();
  }

  public RoverAction getNextBestAction(RoverInfo roverInfo) {
    //TODO If full of items. Drop off items at base.
    //TODO If collect exists. Try to collect.
    //TODO If nothing to collect try to scan.
    return getNextBestCollect(roverInfo)
            .orElseGet(() -> getNextBestScan(roverInfo).orElse());
    return getNextBestScan(roverInfo);
    //TODO Before selecting action, ensure enough energy left to return all payload to base.
  }

  public Optional<RoverAction> getNextBestCollect(RoverInfo roverInfo) {
    final int cargoSpaceRemaining = roverInfo.getAttributes().getMaxLoad()
            - roverInfo.getCurrentPayload();
    RoverRoutine roverRoutine = new RoverRoutine();
    //TODO dropOff+collect
    //TODO justCollect
    //TODO seeWhichIsWorthMore
    if(cargoSpaceRemaining == 0) {
    } else {
      Optional<Resource> optionalResource = itemManager.getMostDesirable(
              cargoSpaceRemaining,
              roverInfo.getPosition(),
              roverInfo.getScenarioInfo().getHeight(),
              roverInfo.getAttributes().getCargoType());
    }
  }

  private RoverRoutine getNextBestScan(RoverInfo roverInfo) {
    log.info("Finding best available scan location.");
    ScanManager scanManager = scanManagers.computeIfAbsent(
                    roverInfo.getScenarioInfo().getHeight(),
                    x -> new ScanManager(x, roverInfo.getAttributes().getScanRange(), SCAN_RESOLUTION));
    ScanResult bestScanResult = scanManager.getNextBestScanQuick(roverInfo.getPosition(),
            roverInfo.getAttributes().getMaxSpeed(), roverInfo.getAttributes().getScanRange());
    Coordinate bestScanCoordinates = scanManager.getRealScanCoordinates(bestScanResult);
    log.info("Rover: {},{} Scan: {},{}. Discovery chance: {}",
            roverInfo.getPosition().getX(),
            roverInfo.getPosition().getY(),
            bestScanCoordinates.getX(),
            bestScanCoordinates.getY(),
            scanManager.getDiscoveryChance(bestScanResult));
    RoverRoutine roverRoutine = new RoverRoutine(RoverRoutine.RoutineType.SCAN,
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
