package rover.mediators.message;

/**
 * Created by dominic on 26/10/16.
 */
public abstract class OutboundMessage {
  private final String message;

  public OutboundMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
