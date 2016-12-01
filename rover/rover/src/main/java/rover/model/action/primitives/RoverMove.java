package rover.model.action.primitives;

import rover.mediators.RoverFacade;
import rover.model.maplocation.Coordinate;
import rover.model.roverinfo.RoverInfo;

/**
 * Created by dominic on 23/11/16.
 */
public class RoverMove extends RoverAction {
  private double xOffset;
  private double yOffset;
  private double speed;

  public RoverMove(RoverInfo roverInfo) {
    this(roverInfo, Coordinate.ORIGIN);
  }

  public RoverMove(RoverInfo roverInfo,
                   Coordinate targetCoordinate) {
    this(roverInfo, roverInfo.getPosition(), targetCoordinate);
  }

  public RoverMove(RoverInfo roverInfo,
                   Coordinate startCoordinate,
                   Coordinate targetCoordinate) {
    this(
            roverInfo,
            Coordinate.distanceToMove(
                    startCoordinate.getX(),
                    targetCoordinate.getX(),
                    roverInfo.getScenarioInfo().getSize()
            ),
            Coordinate.distanceToMove(
                    startCoordinate.getY(),
                    targetCoordinate.getY(),
                    roverInfo.getScenarioInfo().getSize()
            ),
            roverInfo.getAttributes().getMaxSpeed()
    );
  }

  private RoverMove(RoverInfo roverInfo,
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
            distance / speed);
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

    return Double.compare(roverMove.xOffset, xOffset) == 0
            && Double.compare(roverMove.yOffset, yOffset) == 0
            && Double.compare(roverMove.speed, speed) == 0;

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
