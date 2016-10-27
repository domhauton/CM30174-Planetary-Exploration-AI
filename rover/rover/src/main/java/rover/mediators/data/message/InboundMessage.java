package rover.mediators.data.message;

/**
 * Created by dominic on 24/10/16.
 */
public class InboundMessage {
  private final String message;


  public InboundMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public String toString() {
    return getMessage();
  }
}
