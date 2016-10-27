package rover.mediators.data.message;

/**
 * Created by dominic on 26/10/16.
 *
 * A message targeted directly at another user.
 */
public class OutboundUserMessage extends OutboundMessage{
  private final String targetUserId;

  public OutboundUserMessage(String targetUserId, String message) {
    super(message);
    this.targetUserId = targetUserId;
  }

  public String getTargetUserId() {
    return targetUserId;
  }
}
