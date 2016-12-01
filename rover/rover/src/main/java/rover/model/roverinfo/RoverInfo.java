package rover.model.roverinfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import rover.RoverAttributes;
import rover.Scenario;
import rover.mediators.data.RoverStateInfo;
import rover.mediators.data.ScenarioInfo;
import rover.model.maplocation.Coordinate;


/**
 * Created by dominic on 27/10/16.
 */
public class RoverInfo {

  private final Logger logger;

  private final RoverAttributes attributes;
  private final ScenarioInfo scenarioInfo;
  private final Scenario scenario;
  private Coordinate coordinate;
  private RoverStateInfo roverStateInfo;
  private int currentPayload;

  public RoverInfo(RoverAttributes attributes, Scenario scenario) {
    logger = LoggerFactory.getLogger("AGENT");
    this.attributes = attributes;
    this.scenario = scenario;
    resetRoverInfo();
    scenarioInfo = new ScenarioInfo(
            scenario.getSize(),
            scenario.isCompetitive(),
            scenario.getResourcePiles() * scenario.getResourcePileSize(),
            scenario.getId());
  }

  public RoverAttributes getAttributes() {
    return attributes;
  }

  public void adjustPosition(double xOffset, double yOffset) {
    coordinate = coordinate.moveCoordinate(
            xOffset,
            yOffset,
            scenarioInfo.getSize());
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

  public double getDistanceToBase() {
    return coordinate.getDistanceTo(Coordinate.ORIGIN, scenarioInfo.getSize());
  }

  public int getCurrentPayload() {
    return currentPayload;
  }

  public void addPayload() { currentPayload++; }

  public int removePayload() {
    return currentPayload--;
  }

  public boolean isRoverFull() {
    return remainingCargoSpace() == 0;
  }

  public boolean shouldDeposit() {
    return getDistanceToBase() < 0.05 && getCurrentPayload() != 0;
  }

  public int remainingCargoSpace() {
    return getAttributes().getMaxLoad() - getCurrentPayload();
  }

  public void resetRoverInfo() {
    logger.info("Resetting Rover Info");
    coordinate = Coordinate.ORIGIN;
    currentPayload = 0;
    setRoverStateInfo(new RoverStateInfo(
            Optional.of(0),
            Optional.of((double) scenario.getEnergy()),
            false,
            "dh499",
            "dh499-NA"));
  }
}
