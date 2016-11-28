package rover.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rover.model.action.primitives.RoverAction;
import rover.model.action.primitives.RoverMove;
import rover.model.action.primitives.RoverScan;
import rover.model.maplocation.Coordinate;
import rover.model.scanning.ScanManager;
import rover.model.scanning.ScanResult;

/**
 * Created by dominic on 28/11/16.
 */
public class IntentionGenerator {
  private Logger log;

  public IntentionGenerator() {
    log = LoggerFactory.getLogger("AGENT");
  }

  public RoverAction getNextBestAction(RoverInfo roverInfo) {
    return getNextBestScan(roverInfo);
  }

  private RoverAction getNextBestScan(RoverInfo roverInfo) {
    ScanManager scanManager = roverInfo.getScanManager();
    ScanResult bestScanResult = scanManager.getNextBest(roverInfo.getPosition(),
            roverInfo.getAttributes().getMaxSpeed());
    Coordinate bestScanCoordinates = scanManager.getRealScanCoordinates(bestScanResult);
    log.info("Rover: {},{} Scan: {},{}. Discovery chance: {}",
            roverInfo.getPosition().getX(),
            roverInfo.getPosition().getY(),
            bestScanCoordinates.getX(),
            bestScanCoordinates.getY(),
            scanManager.getDiscoveryChance(bestScanResult));
    if(bestScanCoordinates.equals(roverInfo.getPosition())){
      return new RoverScan(roverInfo, roverInfo.getAttributes().getScanRange(), bestScanResult);
    } else {
      return new RoverMove(roverInfo, roverInfo.getPosition(), bestScanCoordinates);
    }
  }
}
