package rover.mediators;

/**
 * Created by dominic on 25/10/16.
 */
public class RoverActionException extends Exception{
  public RoverActionException() {
  }

  public RoverActionException(String s, Throwable throwable) {
    super(s, throwable);
  }
}
