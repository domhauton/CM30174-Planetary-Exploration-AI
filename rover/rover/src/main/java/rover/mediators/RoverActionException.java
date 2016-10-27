package rover.mediators;

/**
 * Created by dominic on 25/10/16.
 */
public class RoverActionException extends Exception {

  RoverActionException(String s, Throwable throwable) {
    super(s, throwable);
  }
}
