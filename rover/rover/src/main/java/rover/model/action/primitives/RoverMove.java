package rover.model.action.primitives;

import rover.Rover;
import rover.mediators.RoverFacade;
import rover.model.RoverInfo;
import rover.model.maplocation.Coordinate;

/**
 * Created by dominic on 23/11/16.
 */
public class RoverMove extends RoverAction {
  private double xOffset;
  private double yOffset;
  private double speed;

  public RoverMove(RoverFacade roverFacade,
                   RoverInfo roverInfo,
                   Coordinate targetCoordinate) {
    this(roverInfo, roverInfo.getPosition(), targetCoordinate);
  }

  public RoverMove(RoverInfo roverInfo,
                    Coordinate startCoordinate,
                    Coordinate targetCoordinate){
    this(
            roverInfo,
            distanceToMove(
                    startCoordinate.getX(),
                    targetCoordinate.getX(),
                    roverInfo.getScenarioInfo().getWidth()
            ),
            distanceToMove(
                    startCoordinate.getY(),
                    targetCoordinate.getY(),
                    roverInfo.getScenarioInfo().getHeight()
            ),
            roverInfo.getAttributes().getMaxSpeed()
    );
  }

  public RoverMove(RoverInfo roverInfo,
                   double xOffset,
                   double yOffset,
                   double speed) {
    super(roverInfo);
    this.xOffset = xOffset;
    this.yOffset = yOffset;
    this.speed = speed;
  }

  @Override
  public ActionCost getActionCost() {
    double distance = Math.sqrt((xOffset * xOffset) + (yOffset * yOffset));
    return new ActionCost(
            (2.0 * distance) / roverInfo.getAttributes().getMaxSpeed(),
            distance * speed * 3);
  }

  @Override
  public void execute(RoverFacade roverFacade) {
    roverFacade.move(xOffset, yOffset, speed);
  }

  @Override
  public void complete() {
    roverInfo.adjustPosition(xOffset, yOffset);
  }

  @Override
  public void failed() {
    //TODO Shouldn't really fail to move, check max move speed and energy
  }

  private static double distanceToMove(double oldPosition, double targetPosition, double mapSize) {
    double halfMapSize = mapSize/2;
    return ((((targetPosition-oldPosition)+halfMapSize)%mapSize)-halfMapSize);
  }

  @Override
  public String toString() {
    return "RoverMove{" +
            "xOffset=" + xOffset +
            ", yOffset=" + yOffset +
            ", speed=" + speed +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof RoverMove)) return false;

    RoverMove roverMove = (RoverMove) o;

    if (Double.compare(roverMove.xOffset, xOffset) != 0) return false;
    if (Double.compare(roverMove.yOffset, yOffset) != 0) return false;
    return Double.compare(roverMove.speed, speed) == 0;

  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    temp = Double.doubleToLongBits(xOffset);
    result = (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(yOffset);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(speed);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }
}
