package rover.model.communication.ontology.inform;

import java.util.Optional;

import rover.model.communication.MessageReceiver;
import rover.model.communication.ontology.MessageParser;

/**
 * Created by dominic on 01/12/16.
 */
public class ActionBid implements MessageParser {
  private static int REQUIRED_LENGTH = 2;
  public static String PHRASE = "BID_FOR_ACTION";

  public boolean isMessageType(String[] message) {
    return isMessageType(message, REQUIRED_LENGTH, PHRASE);
  }

  public Optional<Runnable> parse(String[] message, MessageReceiver messageReceiver) {
    return Optional.of(() -> messageReceiver.receiveActionBid(
            Double.parseDouble(message[1])));
  }

  @Override
  public String getMessageRoot() {
    return Inform.PHRASE + " " + PHRASE;
  }

  public String generateCommand(Double bidValue) {
    return String.format("%s %f", getMessageRoot(), bidValue);
  }
}
