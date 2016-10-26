package rover.mediators.message;

/**
 * Created by dominic on 26/10/16.
 *
 * An output message directed at the user's team.
 */
public class OutboundTeamMessage extends OutboundMessage {
  public OutboundTeamMessage(String message) {
    super(message);
  }
}
